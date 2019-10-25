package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.OrgRedisServiceApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.CompanyUserService;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.OrgToken;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
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
 * 业务控制层
 *
 * @author kuangbin
 * @date 2019年7月20日
 */
@RestController
@RequestMapping("org/company")
public class CompanyUserController {
    @Autowired
    private CompanyUserService companyUserService;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private OrgToken orgToken;
    /**
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private DeptService deptService;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * CRM-单位用户-所有单位-单位列表
     * 点击所有单位显示所有单位列表信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanyListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-单位用户-所有单位-单位列表
     * 点击调入公海是加入公海单位表
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addCompanyCommon", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyCommon(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 返回所有的企业列表
            int count = companyUserService.addCompanyCommon(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-单位用户-所有单位-单位列表
     * 单位创建成功发送短信提醒
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/sendRemind", method = RequestMethod.POST)
    @CrossOrigin
    public Response sendRemind(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            param.put("username", JzbDataType.getString(param.get("name")));
            param.put("companyname", JzbDataType.getString(param.get("cname")));
            param.put("relphone", JzbDataType.getString(param.get("phone")));
            param.put("groupid", "1013");
            param.put("msgtag", "sendRemind1013");
            param.put("senduid", "sendRemind1013");
            result = companyService.sendRemind(param);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主1
     * 点击公海显示所有的单位信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyCommonList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyCommonList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCommonListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyCommonList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主4
     * 根据单位ID显示对应的供应商或全部供应商
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanySupplierList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanySupplierList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanySupplierCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanySupplierList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目6
     * 点击业主下的项目获取项目列表
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanyProjectCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyProjectList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count > 0 ? count : companyList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中新建项目
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = companyUserService.addCompanyProject(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目8
     * 点击业主下的项目中的修改项目按钮
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/modifyCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyCompanyProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = companyUserService.modifyCompanyProject(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 根据用户ID查询企业中是否存在用户
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getDeptCount", method = RequestMethod.POST)
    @CrossOrigin
    public Response getDeptCount(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 返回所有的企业列表
            int count = companyUserService.getDeptCount(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            result.setResponseEntity(count);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员11
     * 将用户加入单位资源池中
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/addCompanyDept", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyDept(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 返回所有的企业列表
            int count = companyUserService.addCompanyDept(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-项目1
     * 点击项目中的导入Excel表格上传模板
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyProject(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "D:/v3/static/excel/ImportCompanyProject.xlsx";
            FileInputStream in = new FileInputStream(srcFilePath);
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportCompanyProject.xlsx");
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
    @RequestMapping(value = "/importCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response importCompanyProject(@RequestBody MultipartFile file,
                                         @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            param.put("userinfo", userInfo);
            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);

            // 获取上传文件名称
            long time = System.currentTimeMillis();
            String fileName = file.getOriginalFilename();
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
                param.put("status", "4");
                param.put("summary", "保存文件到本地失败");
            }
            // 添加批次信息到用户导入批次表
            deptService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable importCompanyProject = new ImportCompanyProject(filepath, param, result);
            Future future = pool.submit(importCompanyProject);
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
    public class ImportCompanyProject implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportCompanyProject(String filepath, Map<String, Object> param, Response result) {
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
                // 获取模板中的项目名称
                String name = JzbDataType.getString(map.get(0));

                // 定义批次中的备注
                String summary = "";
                exportMap.put("cname", name);
                // 在参数中加入项目名称
                param.put("projectname", name);
                if (JzbDataType.isEmpty(name)) {
                    summary = "项目名称不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的招标人姓名
                String cname = JzbDataType.getString(map.get(1));
                param.put("tendername", cname);
                if (JzbDataType.isEmpty(cname)) {
                    summary += "招标人姓名不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的招标人联系方式
                String phone = JzbDataType.getString(map.get(2));
                param.put("tenderphone", phone);
                if (JzbDataType.isEmpty(phone)) {
                    summary += "招标人手机号不能为空!";
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
                // 获取模板中的招标日期
                String time = JzbDataType.getString(map.get(3));
                long tenderTime = 0;
                try {
                    tenderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time).getTime();
                }catch (Exception e){
                    JzbTools.logError(e);
                    summary += "招标日期格式不正确!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                }
                param.put("tendertime", tenderTime);
                if (JzbDataType.isEmpty(tenderTime)) {
                    summary += "招标日期不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的单位地区
                String regionName = JzbDataType.getString(map.get(4));
                param.put("regionName", regionName);
                if (JzbDataType.isEmpty(regionName)) {
                    summary += "项目所属地区不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 调用获取地区ID的接口
                Response regionID = regionBaseApi.getRegionID(param);
                Object obj = regionID.getResponseEntity();
                // 定义地区ID
                String region = "";
                if (JzbDataType.isMap(obj)) {
                    Map<Object, Object> regionMap = (Map<Object, Object>) obj;
                    region = JzbDataType.getString(regionMap.get("creaid"));
                }
                param.put("region", region);
                // 获取模板中的单位地址
                String address = JzbDataType.getString(map.get(5));
                param.put("address", address);
                // 调用接口
                int count = companyUserService.addCompanyProject(param);
                if (count == 0) {
                    exportMap.put("status", "2");
                    exportMap.put("summary", "创建项目失败!");
                    userInfoList.add(exportMap);
                    continue;
                } else {
                    exportMap.put("status", "1");
                    userInfoList.add(exportMap);
                }
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

    private boolean toPhone(String obj) {
        boolean result = true;
        try {
            String eg = "^[1][3,4,5,6,7,8,9][0-9]{9}$";
            result = Pattern.matches(eg, obj);
        } catch (Exception e) {
            JzbTools.logError(e);
        }
        return result;
    }
}
