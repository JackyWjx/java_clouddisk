package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.OrgToken;
import com.jzb.org.service.ProductService;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
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
            String srcFilePath = "D:/v3/static/excel/ImportProductMenu.xlsx";
            FileInputStream in = new FileInputStream(srcFilePath);
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
                    Map<Integer, String> map = list.get(i);
                    // 遍历列获取每列的数据
                    for (int t = 0; t < size - 3; t++) {
                        String cname = map.get(t);
                        if (!JzbDataType.isEmpty(cname)) {
                            Map<String, Object> menuParam = new HashMap<>();
                            // 获取模板中的备注
                            String summary = map.get(size - 1);
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
                                result = Response.getResponseError();
                                result.setResponseEntity("菜单名称不能为空");
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
                        result = Response.getResponseError();
                        result.setResponseEntity("产品创建失败");
                    }
                }
            } else {
                result = Response.getResponseError();
                result.setResponseEntity("产品名称不能为空");
            }
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
            String srcFilePath = "D:/v3/static/excel/ImportProductPage.xlsx";
            FileInputStream in = new FileInputStream(srcFilePath);
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
                String pagePath = JzbDataType.getString(map.get(5));
                pageParam.put("pagepath", pagePath);

                // 获取页面模板中的页面备注
                String summary = JzbDataType.getString(map.get(6));
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
            String srcFilePath = "D:/v3/static/excel/ImportPageControl.xlsx";
            FileInputStream in = new FileInputStream(srcFilePath);
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
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
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
            String srcFilePath = "D:/v3/static/excel/ImportCompanyTemplate.xlsx";
            FileInputStream in = new FileInputStream(srcFilePath);
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
} // End class ProductController
