package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.OrgToken;
import com.jzb.org.service.ProductService;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

/**
 * @Description: 产品控制层, 与前端对接
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 9:43
 */
@RequestMapping("/org")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrgToken orgToken;

    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptMapper deptMapper;

    @Autowired
    private OrgConfigProperties config;

    /**
     * 查询产品的信息
     *
     * @param param 用 kv 存储
     * @returngetEnterpriseData
     */
    @RequestMapping(value = "/getProductLsit", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductList(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            // 获取指定页记录
            List<Map<String, Object>> records = productService.getProductList(param);
            // 定义返回结果
            response = Response.getResponseSuccess();
            PageInfo page = new PageInfo();
            page.setList(records);

            // 当count为0时，获取总记录个数
            Object count = param.get("count");
            if (count != null && count.toString().equals("0")) {
                int size = productService.getProductTotal(param);
                page.setTotal(size > 0 ? size : records.size());
            }

            // 设置返回页数据
            response.setPageInfo(page);
        } catch (Exception e) {
            response = Response.getResponseError();
            e.printStackTrace();
        }
        return response;
    } // End getProductList

    /**
     * 写入数据到菜单表格模板并导出给用户
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createMenuExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createMenuExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportProductMenu.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            String plid = JzbDataType.getString(param.get("plid"));
            sheet.getRow(1).createCell(3).setCellValue(plid);
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportProductMenu.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * 读取菜单模板中的数据并存入数据库
     *
     * @param file
     * @author kuangbin
     */
    @RequestMapping(value = "/importMenuExcel", method = RequestMethod.POST)
    @CrossOrigin
    public Response importMenuExcel(@RequestBody MultipartFile file,
                                    @RequestHeader(value = "token") String token,
                                    @RequestParam(value = "cid") String cid) {
        Response result = new Response();
        try {
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("userinfo", userInfo);
            param.put("cid", cid);
            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);

            // 获取上传文件名称
            String fileName = file.getOriginalFilename();

            // 获取后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filepath = config.getImportPath() + "/" + batchId + suffix;
            param.put("address", filepath);
            param.put("batchid", batchId);
            param.put("status", "2");
            param.put("cname", fileName);
            try {
                // 保存文件到本地
                File intoFile = new File(filepath);
                intoFile.getParentFile().mkdirs();
                String address = intoFile.getCanonicalPath();
                file.transferTo(new File(address));
            } catch (Exception e) {
                JzbTools.logError(e);
                // 保存失败信息到批次表
                param.put("summary", "保存文件到本地失败");
                param.put("status", "4");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importMenuThread = new ImportMenuThread(filepath, param, result);
            Future future = pool.submit(importMenuThread);
            // 获取返回值结果
            result = (Response) future.get();

            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 读取菜单模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportMenuThread implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportMenuThread(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            String cid = JzbDataType.getString(param.get("cid"));
            Map<String, Object> parentid = new HashMap<>();
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>();
            Map<String, Object> exportMap = new HashMap<>(param);
            String summary;
            // 获取当前模板中有多少列
            int size = list.get(0).size();

            // 获取当前时间
            long addTime = System.currentTimeMillis();

            // 获取产品信息
            Map<Integer, String> productMap = list.get(1);
            Map<String, Object> pid = productService.getProductParam(productMap, size, cid);
            if (!JzbDataType.isEmpty(JzbDataType.getString(pid.get("pid")))) {
                // 定义初始开关
                boolean bl = true;

                // 定义存放菜单参数的LIST
                List<Map<String, Object>> listParam = new ArrayList<>();

                // 遍历结果行,菜单数据从第四行开始
                for (int i = 3; i < list.size(); i++) {
                    // 设置行信息
                    exportMap.put("idx", i);
                    Map<Integer, String> map = list.get(i);
                    // 遍历列获取每列的数据
                    for (int t = 0; t < size - 3; t++) {
                        String cname = map.get(t);
                        if (!JzbDataType.isEmpty(cname)) {
                            Map<String, Object> menuParam = new HashMap<>();
                            // 获取模板中的备注
                            summary = map.get(size - 1);
                            menuParam.put("summary", summary);

                            // 获取模板中的排序
                            int idx = JzbDataType.getInteger(map.get(size - 2));
                            menuParam.put("idx", idx);

                            // 获取模板中的链接地址
                            String menupath = map.get(size - 3);
                            menuParam.put("menupath", menupath);

                            // 获取随机菜单ID
                            String mid = JzbRandom.getRandomCharCap(15);
                            menuParam.put("mid", mid);
                            menuParam.put("cname", cname);

                            // 加入产品ID
                            menuParam.put("pid", JzbDataType.getString(pid.get("pid")));
                            //menuParam.put("cid", cid);

                            menuParam.put("addtime", addTime);
                            menuParam.put("updtime", addTime);

                            // 获取父级菜单ID
                            menuParam.put("parentid", JzbDataType.getString(parentid.get(JzbDataType.getString(t - 1))));
                            parentid.put(JzbDataType.getString(t), mid);
                            listParam.add(menuParam);
                            break;
                        } else {
                            // 代表已经获取到最后一个菜单名还没有值
                            if (t == size - 4) {
                                summary = "菜单名称不能为空!";
                                exportMap.put("summary", summary);
                                exportMap.put("status", "2");
                                userInfoList.add(exportMap);
                                bl = false;
                                break;
                            }
                        }
                    }
                }
                if (bl) {
                    int count = productService.addProduct(pid);
                    if (count == 1) {
                        count = productService.addProductMenu(listParam);
                        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                        result = count >= 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
                    } else {
                        summary = "创建产品失败!";
                        exportMap.put("summary", summary);
                        exportMap.put("status", "2");
                        userInfoList.add(exportMap);
                    }
                }
            } else {
                summary = "产品名称不能为空!";
                exportMap.put("summary", summary);
                exportMap.put("status", "2");
                userInfoList.add(exportMap);
            }
            exportMap.put("status", "1");
            userInfoList.add(exportMap);
            int export = 0;
            //保存用户导入信息表
            if (userInfoList.size() > 0) {
                export = deptMapper.insertExportUserInfoList(userInfoList);
            }
            //导入完成后修改状态
            if (export >= 1) {
                exportMap.put("status", "8");
            } else if (export == 0) {
                exportMap.put("status", "4");
            }
            deptService.updateExportBatch(exportMap);
            return result;
        }
    }


    /**
     * 写入数据到页面表格模板并导出给用户
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createPageExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createPageExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportProductPage.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 根据产品ID获取产品下所有的菜单
            List<Map<String, Object>> list = productService.getMenuDate(param);
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                // 获取产品ID
                String pid = JzbDataType.getString(param.get("pid"));
                sheet.createRow(i + 1).createCell(0).setCellValue(pid);
                // 获取产品名称
                String name = JzbDataType.getString(param.get("name"));
                sheet.getRow(i + 1).createCell(1).setCellValue(name);
                // 获取菜单ID
                String mid = JzbDataType.getString(page.get("mid"));
                sheet.getRow(i + 1).createCell(2).setCellValue(mid);
                // 获取菜单名称
                String cname = JzbDataType.getString(page.get("cname"));
                sheet.getRow(i + 1).createCell(3).setCellValue(cname);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportProductPage.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * 读取页面模板中的数据并存入数据库
     *
     * @param file
     * @author kuangbin
     */
    @RequestMapping(value = "/importPageExcel", method = RequestMethod.POST)
    @CrossOrigin
    public Response importPageExcel(@RequestBody MultipartFile file,
                                    @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("userinfo", userInfo);
            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);
            param.put("batchid", batchId);

            // 获取上传文件名称
            String fileName = file.getOriginalFilename();

            // 获取后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filepath = config.getImportPath() + "/" + batchId + suffix;
            param.put("address", filepath);
            param.put("status", "2");
            param.put("cname", fileName);
            try {
                // 保存文件到本地
                File intoFile = new File(filepath);
                intoFile.getParentFile().mkdirs();
                String address = intoFile.getCanonicalPath();
                file.transferTo(new File(address));
            } catch (Exception e) {
                JzbTools.logError(e);
                // 保存失败信息到批次表
                param.put("status", "4");
                param.put("summary", "保存文件到本地失败");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importPageThread = new ImportPageThread(filepath, param, result);
            Future future = pool.submit(importPageThread);
            // 获取返回值结果
            result = (Response) future.get();

            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 读取页面模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportPageThread implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportPageThread(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);
            List<Map<String, Object>> pageList = new ArrayList<>();
            // 定义初始开关
            boolean bl = true;

            // 获取当前时间
            long addTime = System.currentTimeMillis();
            // 遍历模板中获取的数据
            for (int i = 1; i < list.size(); i++) {
                Map<Integer, String> map = list.get(i);
                Map<String, Object> pageParam = new HashMap<>();
                // 获取页面模板中的产品ID
                String pid = JzbDataType.getString(map.get(0));
                pageParam.put("pid", pid);

                // 获取页面模板中的菜单ID
                String mid = JzbDataType.getString(map.get(2));
                pageParam.put("mid", mid);

                // 获取页面模板中的页面编码
                String pageCode = JzbDataType.getString(map.get(4));
                pageParam.put("pagecode", pageCode);

                // 获取页面模板中的页面名
                String cname = JzbDataType.getString(map.get(5));
                pageParam.put("cname", cname);
                if (JzbDataType.isEmpty(pid) || JzbDataType.isEmpty(mid) || JzbDataType.isEmpty(cname)) {
                    bl = false;
                    break;
                }
                // 获取页面模板中的页面链接地址
                String pagePath = JzbDataType.getString(map.get(6));
                pageParam.put("pagepath", pagePath);

                // 获取页面模板中的页面备注
                String summary = JzbDataType.getString(map.get(7));
                pageParam.put("summary", summary);
                // 加入当前时间戳
                pageParam.put("addtime", addTime);
                pageParam.put("updtime", addTime);
                // 获取产品页面ID
                String pageId = JzbRandom.getRandomCharCap(17);
                pageParam.put("pageid", pageId);
                pageParam.put("status", "1");
                pageList.add(pageParam);
            }
            if (bl) {
                int count = productService.addProductPage(pageList);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("必填项不能为空");
            }
            return result;
        }
    }

    /**
     * 写入数据到控件表格模板并导出给用户
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createControlExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createControlExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportPageControl.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 根据产品ID获取产品下所有的页面
            List<Map<String, Object>> list = productService.getPageDate(param);
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                // 获取菜单ID
                String mid = JzbDataType.getString(page.get("pageid"));
                if (i == 0) {
                    sheet.getRow(i + 1).createCell(0).setCellValue(mid);
                } else {
                    sheet.createRow(i + 1).createCell(0).setCellValue(mid);
                }
                // 获取菜单名称
                String cname = JzbDataType.getString(page.get("cname"));
                sheet.getRow(i + 1).createCell(1).setCellValue(cname);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportPageControl.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * 读取控件模板中的数据并存入数据库
     *
     * @param file
     * @author kuangbin
     */
    @RequestMapping(value = "/importControlExcel", method = RequestMethod.POST)
    @CrossOrigin
    public Response importControlExcel(@RequestBody MultipartFile file,
                                       @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> param = new HashMap<>();
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            param.put("userinfo", userInfo);
            // 获取上传文件名称
            String fileName = file.getOriginalFilename();
            long time = System.currentTimeMillis();
            String filepath = "D:\\v3\\static\\Import\\" + time + fileName;
            // 保存文件到本地
            File intoFile = new File(filepath);
            file.transferTo(intoFile);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importControlThread = new ImportControlThread(filepath, param, result);
            Future future = pool.submit(importControlThread);
            // 获取返回值结果
            result = (Response) future.get();

            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 读取控件模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportControlThread implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportControlThread(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 定义初始开关
            boolean bl = true;
            List<Map<String, Object>> pageList = new ArrayList<>();
            // 获取当前时间
            long addTime = System.currentTimeMillis();
            // 遍历模板中获取的数据
            for (int i = 1; i < list.size(); i++) {
                Map<Integer, String> map = list.get(i);
                Map<String, Object> pageParam = new HashMap<>();
                // 获取控件模板中的页面ID
                String pageId = JzbDataType.getString(map.get(0));
                pageParam.put("pageid", pageId);

                // 获取控件模板中的控件名称
                String cname = JzbDataType.getString(map.get(2));
                pageParam.put("cname", cname);
                if (JzbDataType.isEmpty(pageId) || JzbDataType.isEmpty(cname)) {
                    bl = false;
                    break;
                }
                // 获取控件模板中的控件编码
                String controlCode = JzbDataType.getString(map.get(3));
                pageParam.put("controlcode", controlCode);

                // 获取控件模板中的控件类型
                int controlType = JzbDataType.getInteger(map.get(4));
                pageParam.put("controltype", controlType);

                // 获取控件模板中的控件链接地址
                String powerApi = JzbDataType.getString(map.get(5));
                pageParam.put("powerapi", powerApi);

                // 获取控件模板中的控件备注
                String summary = JzbDataType.getString(map.get(6));
                pageParam.put("summary", summary);
                // 加入当前时间戳
                pageParam.put("addtime", addTime);
                pageParam.put("updtime", addTime);
                // 获取产品控件ID
                String controlId = JzbRandom.getRandomCharCap(19);
                pageParam.put("controlid", controlId);
                pageParam.put("status", "1");
                // 将当前控件参数map加入list集合中
                pageList.add(pageParam);
            }
            if (bl) {
                int count = productService.addPageControl(pageList);
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("必填项不能为空");
            }
            return result;
        }
    }

    /**
     * CRM-单位用户-所有用户-单位列表
     * 导出新增单位模板
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyTemplate", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyTemplate(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportCompanyTemplate.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportCompanyTemplate.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * CRM-单位用户-所有用户-单位列表
     * 导入新增单位模板,读取模板中的数据
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/importCompanyTemplate", method = RequestMethod.POST)
    @CrossOrigin
    public Response importCompanyTemplate(@RequestBody MultipartFile file,
                                          @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            param.put("userinfo", userInfo);
            // 获取上传文件名称
            long time = System.currentTimeMillis();
            String fileName = file.getOriginalFilename();
            String filepath = "D:\\v3\\static\\Import\\" + time + fileName;
            //生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);
            param.put("batchid", batchId);
            param.put("address", filepath);
            param.put("cname", fileName);
            param.put("status", "2");
            try {
                //保存文件到本地
                File intoFile = new File(filepath);
                intoFile.getParentFile().mkdirs();
                String address = intoFile.getCanonicalPath();
                file.transferTo(new File(address));
            } catch (Exception e) {
                e.printStackTrace();
                JzbTools.logError(e);
                // 保存失败信息到批次表
                param.put("status", "4");
                param.put("summary", "保存文件到本地失败");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importCompanyThread = new ImportCompanyThread(filepath, param, result);
            Future future = pool.submit(importCompanyThread);
            // 获取返回值结果
            result = (Response) future.get();
            result.setResponseEntity(param);
            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 读取新建企业模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportCompanyThread implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportCompanyThread(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>();
            Map<String, Object> exportMap = new HashMap<>(param);
            // 遍历结果行,菜单数据从第2行开始
            for (int i = 1; i < list.size(); i++) {
                // 设置行信息
                exportMap.put("idx", i);
                Map<Integer, String> map = list.get(i);
                // 获取模板中的用户姓名
                String name = JzbDataType.getString(map.get(0));

                // 获取模板中的用户手机号
                String phone = JzbDataType.getString(map.get(1));

                // 获取模板中的单位名称
                String cname = JzbDataType.getString(map.get(2));
                String summary = "";
                exportMap.put("cname", name);
                if (JzbDataType.isEmpty(name)) {
                    exportMap.put("status", "2");
                    summary = "用户姓名不能为空!";
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                if (JzbDataType.isEmpty(phone)) {
                    exportMap.put("status", "2");
                    summary += "用户手机号不能为空!";
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                } else {
                    if (!toPhone(phone)) {
                        exportMap.put("status", "2");
                        summary += "手机号不合规范";
                        exportMap.put("summary", summary);
                        userInfoList.add(exportMap);
                        continue;
                    }
                }
                if (JzbDataType.isEmpty(cname)) {
                    exportMap.put("status", "2");
                    summary += "单位名称不能为空!";
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                param.put("companyname", cname);
                // 获取模板中的单位地区
                String regionName = JzbDataType.getString(map.get(3));
                param.put("regionName", regionName);
                // 调用获取地区ID的接口
                Response regionID = regionBaseApi.getRegionID(param);
                Object obj = regionID.getResponseEntity();
                // 定义地区ID
                String region = "";
                if (JzbDataType.isMap(obj)) {
                    Map<Object, Object> regionMap = (Map<Object, Object>) obj;
                    region = JzbDataType.getString(regionMap.get("creaid"));
                }
                // 获取模板中的单位地址
                String address = JzbDataType.getString(map.get(4));

                // 获取模板中的备注
                String systemname = JzbDataType.getString(map.get(5));
                param.put("name", name);
                param.put("authid", "8");
                param.put("phone", phone);
                param.put("cname", cname);
                param.put("region", region);
                param.put("address", address);
                param.put("systemname", systemname);
                // 调用接口
                result = productService.addRegistrationCompany(param);
                if (JzbDataType.isString(result.getResponseEntity())){
                    exportMap.put("status", "2");
                    exportMap.put("summary", result.getResponseEntity());
                    userInfoList.add(exportMap);
                    continue;
                }
                exportMap.put("status", "1");
                userInfoList.add(exportMap);
            }
            int export = 0;
            //保存用户导入信息表
            if (userInfoList.size() > 0) {
                export = deptMapper.insertExportUserInfoList(userInfoList);
            }
            //导入完成后修改状态
            if (export >= 1) {
                exportMap.put("status", "8");
            } else if (export == 0) {
                exportMap.put("status", "4");
            }
            deptService.updateExportBatch(exportMap);
            return result;
        }
    }

    /**
     * 校验手机号
     *
     * @param obj
     * @return
     */
    private boolean toPhone(String obj) {
        boolean result = true;
        try {
            String eg = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
            result = Pattern.matches(eg, obj);
        } catch (Exception e) {
            JzbTools.logError(e);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主
     * 导出新增业主模板
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyCommon(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportCompanyCommon.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportCompanyCommon.xlsx");
            OutputStream os = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            // 将excel写入到输出流中
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            JzbTools.logError(e);
        }
    }

    /**
     * CRM-销售业主-公海-业主
     * 导入新增业主模板
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/importCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response importCompanyCommon(@RequestBody MultipartFile file,
                                          @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            param.put("userinfo", userInfo);
            long time = System.currentTimeMillis();
            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);

            // 获取上传文件名称
            String fileName = file.getOriginalFilename();

            // 设置保存文件名路径和文件名
            String filepath = "D:\\v3\\static\\Import\\" + time + fileName;
            param.put("batchid", batchId);
            param.put("address", filepath);
            param.put("status", "2");
            param.put("cname", fileName);
            try {
                // 保存文件到本地
                File intoFile = new File(filepath);
                intoFile.getParentFile().mkdirs();
                String address = intoFile.getCanonicalPath();
                file.transferTo(new File(address));
            } catch (Exception e) {
                JzbTools.logError(e);
                // 保存失败信息到批次表
                param.put("summary", "保存文件到本地失败");
                param.put("status", "4");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importCompanyCommon = new ImportCompanyCommon(filepath, param, result);
            Future future = pool.submit(importCompanyCommon);
            // 获取返回值结果
            result = (Response) future.get();
            result.setResponseEntity(param);
            // 关闭线程池
            pool.shutdown();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /***
     * 读取新建企业模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportCompanyCommon implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportCompanyCommon(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            Map<String, Object> exportMap = null;
            // 保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>();
            // 遍历结果行,菜单数据从第2行开始
            for (int i = 1; i < list.size(); i++) {
                exportMap = new HashMap<>(param);
                // 设置行信息
                exportMap.put("idx", i);
                Map<Integer, String> map = list.get(i);
                // 获取模板中的用户姓名
                String name = JzbDataType.getString(map.get(0));
                String summary = "";
                exportMap.put("cname", name);
                if (JzbDataType.isEmpty(name)) {
                    summary = "用户姓名不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的用户手机号
                String phone = JzbDataType.getString(map.get(1));
                if (JzbDataType.isEmpty(phone)) {
                    summary += "用户手机号不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                } else {
                    if (!toPhone(phone)) {
                        exportMap.put("status", "2");
                        summary += "手机号不合规范";
                        exportMap.put("summary", summary);
                        userInfoList.add(exportMap);
                        continue;
                    }
                }
                // 获取模板中的单位名称
                String cname = JzbDataType.getString(map.get(2));
                param.put("companyname", cname);
                if (JzbDataType.isEmpty(cname)) {
                    summary += "单位名称不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的单位地区
                String regionName = JzbDataType.getString(map.get(3));
                param.put("regionName", regionName);
                // 调用获取地区ID的接口
                Response regionID = regionBaseApi.getRegionID(param);
                Object obj = regionID.getResponseEntity();
                // 定义地区ID
                String region = "";
                if (JzbDataType.isMap(obj)) {
                    Map<Object, Object> regionMap = (Map<Object, Object>) obj;
                    region = JzbDataType.getString(regionMap.get("creaid"));
                }
                // 获取模板中的单位地址
                String address = JzbDataType.getString(map.get(4));

                // 获取模板中的备注
                String summary1 = JzbDataType.getString(map.get(5));
                param.put("authid", "0");
                param.put("name", name);
                param.put("cname", cname);
                param.put("phone", phone);
                param.put("region", region);
                param.put("address", address);
                param.put("summary", summary1);
                // 调用接口
                result = productService.addRegistrationCompany(param);
                if (JzbDataType.isString(result.getResponseEntity())){
                    exportMap.put("status", "2");
                    exportMap.put("summary", result.getResponseEntity());
                    userInfoList.add(exportMap);
                    continue;
                }
                exportMap.put("status", "1");
                userInfoList.add(exportMap);
            }
            int export = 0;
            //保存用户导入信息表
            if (userInfoList.size() > 0) {
                export = deptMapper.insertExportUserInfoList(userInfoList);
            }
            //导入完成后修改状态
            if (export >= 1) {
                exportMap.put("status", "8");
            } else if (export == 0) {
                exportMap.put("status", "4");
            }
            deptService.updateExportBatch(exportMap);
            return result;
        }
    }
} // End class ProductController
