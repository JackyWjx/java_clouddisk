package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.dao.DeptMapper;
import com.jzb.org.service.CompanyService;
import com.jzb.org.service.CompanyUserService;
import com.jzb.org.service.DeptService;
import com.jzb.org.service.OrgToken;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    private OrgConfigProperties config;

    @Autowired
    private OrgToken orgToken;

    @Autowired
    private AuthApi authApi;
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
            param.put("groupid", config.getAddCompany());
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
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyCommonList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(companyList.size() == 0 ? 0 : JzbDataType.getInteger(companyList.get(0).get("count")));
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
            param.put("projectid", JzbRandom.getRandomCharCap(19));
            // 返回所有的企业列表
            int count = companyUserService.addCompanyProject(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            Map<String , Object> map = new HashMap<>();
            map.put("projectid",param.get("projectid"));
            result.setResponseEntity(map);
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
    @RequestMapping(value = "/addCompanyProjectInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response addCompanyProjectInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = companyUserService.addCompanyProjectInfo(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中修改项目详情
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/upCompanyProjectInfo", method = RequestMethod.POST)
    @CrossOrigin
    public Response upCompanyProjectInfo(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = companyUserService.upCompanyProjectInfo(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中修改
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/upCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response upCompanyProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = companyUserService.upCompanyProject(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的项目7
     * 点击业主下的项目中删除
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/delCompanyProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response delCompanyProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            List<Map<String,Object>> list = (List<Map<String, Object>>) param.get("list");
            if(!JzbTools.isEmpty(list)){
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).put("status",'2');
                }
            }
            param.put("list",list);
            // 返回所有的企业列表
            int count = companyUserService.delCompanyProject(param);
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
     * CRM-销售业主-公海-业主下的人员9
     * 查询业主下所有的人员信息
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/getCompanyUserList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyUserList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = companyUserService.getCompanyUserListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = companyUserService.getCompanyUserList(param);
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
            String srcFilePath = "static/excel/ImportCompanyProject.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
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
     * CRM-销售业主-公海-项目1
     * 点击项目中的导入Excel表格获取模板数据
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
            // 获取文件名
            String fileName = file.getOriginalFilename();

            // 生成批次ID
            String batchId = JzbRandom.getRandomCharCap(11);

            //获取后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filepath = config.getImportPath() + "/" + batchId + suffix;
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
                } catch (Exception e) {
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
        }
        return result;
    }

    /**
     * CRM-销售业主-所有业主-业主下的人员1
     * 点击项目中的导入Excel表格上传模板
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyUser", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyUser(HttpServletResponse response) {
        try {
            String srcFilePath = "static/excel/ImportCompanyUser.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportCompanyUser.xlsx");
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
     * CRM-销售业主-公海-项目1
     * 点击项目中的导入Excel表格获取模板数据
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/importCompanyUser", method = RequestMethod.POST)
    @CrossOrigin
    public Response importCompanyUser(@RequestBody MultipartFile file,
                                      @RequestHeader(value = "token") String token,
                                      @RequestParam(value = "cid") String cid,
                                      @RequestParam(value = "companyname") String companyname) {
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
            String fileName = file.getOriginalFilename();

            // 获取后缀名
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            String filepath = config.getImportPath() + "/" + batchId + suffix;
            param.put("batchid", batchId);
            param.put("address", filepath);
            param.put("cid", cid);
            param.put("companyname", companyname);
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
            Callable importCompanyUser = new ImportCompanyUser(filepath, param, result);
            Future future = pool.submit(importCompanyUser);
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
    public class ImportCompanyUser implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportCompanyUser(String filepath, Map<String, Object> param, Response result) {
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
                // 获取模板中的用户名称
                String cname = JzbDataType.getString(map.get(0));

                // 定义批次中的备注
                String summary = "";
                exportMap.put("cname", cname);
                // 在参数中加入项目名称
                param.put("cname", cname);
                if (JzbDataType.isEmpty(cname)) {
                    summary = "用户姓名不能为空!";
                    exportMap.put("summary", summary);
                    exportMap.put("status", "2");
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的用户联系方式
                String relphone = JzbDataType.getString(map.get(1));
                param.put("relphone", relphone);
                if (JzbDataType.isEmpty(relphone)) {
                    summary += "用户手机号不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                } else {
                    if (!toPhone(relphone)) {
                        exportMap.put("status", "2");
                        summary += "用户手机号不合规范";
                        exportMap.put("summary", summary);
                        userInfoList.add(exportMap);
                        continue;
                    }
                }
                // 获取模板中的电子邮箱
                String relmail = JzbDataType.getString(map.get(2));
                param.put("relmail", relmail);
                if (JzbDataType.isEmpty(relmail)) {
                    summary += "电子邮箱不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    continue;
                }
                // 获取模板中的出生日期
                String time = JzbDataType.getString(map.get(3));
                long born = 0;
                try {
                    born = new SimpleDateFormat("yyyy-MM-dd").parse(time).getTime();
                } catch (Exception e) {
                    JzbTools.logError(e);
                    summary += "出生日期格式不正确!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                }
                param.put("born", born);
                // 获取模板中的性别
                String sex = JzbDataType.getString(map.get(4));
                if (!JzbDataType.isEmpty(sex)) {
                    if ("男".equals(sex)) {
                        sex = "1";
                    } else if ("女".equals(sex)) {
                        sex = "2";
                    } else {
                        sex = "";
                    }
                }
                param.put("sex", sex);
                // 获取模板中的毕业院校
                String college = JzbDataType.getString(map.get(5));
                param.put("college", college);
                // 获取模板中的学历
                String education = JzbDataType.getString(map.get(6));
                param.put("education", education);
                // 获取模板中的婚姻状况
                String marriage = JzbDataType.getString(map.get(7));
                if (!JzbDataType.isEmpty(marriage)) {
                    if ("已婚".equals(marriage)) {
                        marriage = "1";
                    } else if ("未婚".equals(marriage)) {
                        marriage = "2";
                    } else {
                        marriage = "";
                    }
                }
                param.put("marriage", marriage);
                // 获取模板中的籍贯
                String origin = JzbDataType.getString(map.get(8));
                param.put("origin", origin);
                // 获取模板中的爱好
                String likes = JzbDataType.getString(map.get(9));
                param.put("likes", likes);
                // 调用接口
                result = authApi.addCompanyEmployee(param);
                if (!JzbDataType.isEmpty(result.getResponseEntity())) {
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
     * CRM-销售业主-所有业主-销售统计分析1
     * 点击导出Excel表格,将查询出的数据导出
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createSellStatisticsExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createSellStatisticsExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ImportSellStatistics.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            Object object = param.get("list");
            List<Map<String, Object>> list = new ArrayList<>();
            if (JzbDataType.isCollection(object)) {
                list = (List<Map<String, Object>>) object;
            }
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                Map<String, Object> kkk = new HashMap<>();
                if (!JzbDataType.isEmpty(page.get("kkk"))) {
                    kkk = (Map<String, Object>) page.get("kkk");
                }
                // 获取跟进人
                String uname = JzbDataType.getString(page.get("trackuname"));
                sheet.createRow(i + 1).createCell(0).setCellValue(uname);
                // 获取产品名称
                String cname = JzbDataType.getString(page.get("cname"));
                sheet.getRow(i + 1).createCell(1).setCellValue(cname);
                // 获取项目名称
                String projectname = JzbDataType.getString(page.get("projectname"));
                sheet.getRow(i + 1).createCell(2).setCellValue(projectname);
                // 获取联系人
                String relperson = JzbDataType.getString(page.get("relperson"));
                sheet.getRow(i + 1).createCell(3).setCellValue(relperson);
                // 获取跟进方式
                int tracktype = JzbDataType.getInteger(page.get("tracktype"));
                String s = menthodTrackType(tracktype);
                sheet.getRow(i + 1).createCell(4).setCellValue(s);
                // 获取客户等级
                String dictvalue = JzbDataType.getString(page.get("level"));
                sheet.getRow(i + 1).createCell(5).setCellValue(dictvalue);
                // 获取跟进日期 todo
                long handletime = JzbDataType.getLong(page.get("tracktime"));
                String result2 = new SimpleDateFormat("yyyy-MM-dd ").format(handletime);
                sheet.getRow(i + 1).createCell(6).setCellValue(result2);
                // 获取跟进内容
                String context = JzbDataType.getString(page.get("trackcontent"));
                sheet.getRow(i + 1).createCell(7).setCellValue(context);
                // 获取附件
                String needres = JzbDataType.getString(kkk.get("image"));
                sheet.getRow(i + 1).createCell(8).setCellValue(needres);

            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ImportSellStatistics.xlsx");
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

    private String  menthodTrackType(int tracktype){
        String trackTpye = null;
        switch (tracktype){
            case 0:
                trackTpye = "其它";
                break;
            case 1:
                trackTpye = "发帖";
                break;
            case 2:
                trackTpye = "朋友圈";
                break;
            case 4:
                trackTpye = "qq";
                break;
            case 8:
                trackTpye = "微信";
                break;
            case 16:
                trackTpye = "电话";
                break;
        }
        return trackTpye;
    }
    /**
     * CRM-销售业主-所有业主-业主下的项目1
     * 点击导出Excel表格,将查询出的数据导出
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyProjectExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyProjectExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/CompanyProjectData.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            Object object = param.get("list");
            List<Map<String, Object>> list = new ArrayList<>();
            if (JzbDataType.isCollection(object)) {
                list = (List<Map<String, Object>>) object;
            }
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                Map<String, Object> region = new HashMap<>();
                if (!JzbDataType.isEmpty(page.get("region"))) {
                    region = (Map<String, Object>) page.get("region");
                }
                // 获取所属地
                String regionName = "";
                if (!JzbDataType.isEmpty(JzbDataType.getString(region.get("province")))) {
                    regionName = JzbDataType.getString(region.get("province"));
                }
                if (!JzbDataType.isEmpty(JzbDataType.getString(region.get("city")))) {
                    regionName += "/" + JzbDataType.getString(region.get("city"));
                }
                if (!JzbDataType.isEmpty(JzbDataType.getString(region.get("county")))) {
                    regionName += "/" + JzbDataType.getString(region.get("county"));
                }
                sheet.createRow(i + 1).createCell(0).setCellValue(regionName);
                // 获取项目名称
                String projectname = JzbDataType.getString(page.get("projectname"));
                sheet.getRow(i + 1).createCell(1).setCellValue(projectname);
                // 获取招标人
                String tendername = JzbDataType.getString(page.get("tendername"));
                sheet.getRow(i + 1).createCell(2).setCellValue(tendername);
                // 获取招标人电话
                String tenderphone = JzbDataType.getString(page.get("tenderphone"));
                sheet.getRow(i + 1).createCell(3).setCellValue(tenderphone);
                // 获取创建时间
                String addtime = JzbDataType.getString(page.get("addtime"));
                sheet.getRow(i + 1).createCell(4).setCellValue(addtime);
                // 获取备注
                String summary = JzbDataType.getString(page.get("summary"));
                sheet.getRow(i + 1).createCell(5).setCellValue(summary);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=CompanyProjectData.xlsx");
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
     * CRM-销售业主-所有业主-业主下的人员1
     * 点击导出Excel表格,将查询出的数据导出
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyUserExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyUserExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/CompanyUserData.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            Object object = param.get("list");
            List<Map<String, Object>> list = new ArrayList<>();
            if (JzbDataType.isCollection(object)) {
                list = (List<Map<String, Object>>) object;
            }
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                Map<String, Object> uid = new HashMap<>();
                if (!JzbDataType.isEmpty(page.get("uid"))) {
                    uid = (Map<String, Object>) page.get("uid");
                }
                // 获取姓名
                String cname = JzbDataType.getString(uid.get("cname"));
                sheet.createRow(i + 1).createCell(0).setCellValue(cname);
                // 获取用户名
                String regid = JzbDataType.getString(uid.get("regid"));
                sheet.getRow(i + 1).createCell(1).setCellValue(regid);
                // 获取电话
                String relphone = JzbDataType.getString(uid.get("relphone"));
                sheet.getRow(i + 1).createCell(2).setCellValue(relphone);
                // 获取电子邮箱
                String relmail = JzbDataType.getString(uid.get("relmail"));
                sheet.getRow(i + 1).createCell(3).setCellValue(relmail);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=CompanyUserData.xlsx");
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
     * CRM-销售业主-所有业主-业主下的合同1
     * 点击导出Excel表格,将查询出的数据导出
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createCompanyContractExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createCompanyContractExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/CompanyContractData.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            Object object = param.get("list");
            List<Map<String, Object>> list = new ArrayList<>();
            if (JzbDataType.isCollection(object)) {
                list = (List<Map<String, Object>>) object;
            }
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                // 获取合同编码
                String contid = JzbDataType.getString(page.get("contid"));
                sheet.createRow(i + 1).createCell(0).setCellValue(contid);
                // 获取单位名称
                String companyname = JzbDataType.getString(page.get("companyname"));
                sheet.getRow(i + 1).createCell(1).setCellValue(companyname);
                // 获取项目名称
                String projectname = JzbDataType.getString(page.get("projectname"));
                sheet.getRow(i + 1).createCell(2).setCellValue(projectname);
                // 获取合同金额
                String contamount = JzbDataType.getString(page.get("contamount"));
                sheet.getRow(i + 1).createCell(3).setCellValue(contamount);
                // 获取负责人
                String username = JzbDataType.getString(page.get("username"));
                sheet.getRow(i + 1).createCell(4).setCellValue(username);
                // 获取电话号码
                String userphone = JzbDataType.getString(page.get("userphone"));
                sheet.getRow(i + 1).createCell(5).setCellValue(userphone);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=CompanyContractData.xlsx");
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
     * CRM-销售业主-所有业主-服务统计分析1
     * 点击导出Excel表格,将查询出的数据导出
     *
     * @param
     * @author kuangbin
     */
    @RequestMapping(value = "/createServiceStatisticsExcel", method = RequestMethod.POST)
    @CrossOrigin
    public void createServiceStatisticsExcel(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            String srcFilePath = "static/excel/ServiceStatisticsData.xlsx";
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            InputStream in = resource.getInputStream();
            Object object = param.get("list");
            List<Map<String, Object>> list = new ArrayList<>();
            if (JzbDataType.isCollection(object)) {
                list = (List<Map<String, Object>>) object;
            }
            Workbook wb = new XSSFWorkbook(in);

            XSSFSheet sheet = (XSSFSheet) wb.getSheetAt(0);

            XSSFCellStyle cellStyle = (XSSFCellStyle) wb.createCellStyle();
            cellStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> page = list.get(i);
                // 获取单位名称
                String cname = JzbDataType.getString(page.get("cname"));
                sheet.createRow(i + 1).createCell(0).setCellValue(cname);
                // 获取项目名称
                String projectname = JzbDataType.getString(page.get("projectname"));
                sheet.getRow(i + 1).createCell(1).setCellValue(projectname);
                // 获取服务人员
                String username = JzbDataType.getString(page.get("username"));
                sheet.getRow(i + 1).createCell(2).setCellValue(username);
                // 获取创建时间
                String addtime = JzbDataType.getString(page.get("addtime"));
                sheet.getRow(i + 1).createCell(3).setCellValue(addtime);
                // 获取服务次数
                String service = JzbDataType.getString(page.get("service"));
                sheet.getRow(i + 1).createCell(4).setCellValue(service);
            }
            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=ServiceStatisticsData.xlsx");
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
}
