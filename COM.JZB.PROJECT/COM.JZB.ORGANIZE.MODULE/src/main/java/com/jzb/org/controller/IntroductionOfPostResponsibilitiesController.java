package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.Response;
import com.jzb.base.office.JzbExcelOperater;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.config.OrgConfigProperties;
import com.jzb.org.service.OrgToken;
import com.jzb.org.service.TbExportBatchJobDutyService;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author 王吉祥
 * @DateTime 2020.1.7
 * 岗位职责导入控制类
 *
 */
@RestController
@RequestMapping("/org/jobDutyExcel")
public class IntroductionOfPostResponsibilitiesController {
    @Autowired
    private TbExportBatchJobDutyService tbExportBatchJobDutyService;
    @Autowired
    private OrgToken orgToken;
    @Autowired
    private OrgConfigProperties config;
    @Autowired
    private TbPlantaskJobDutyController tbPlantaskJobPositionController;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(IntroductionOfPostResponsibilitiesController.class);

    /**
     * 计划管理-岗位职责-获取批次信息
     */
    @RequestMapping(value = "/getBatchMsg", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getBatchMsg(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/jobDutyExcel/getBatchMsg";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"batchid"})) {
                response = Response.getResponseError();
            } else {
                String batchid = JzbDataType.getString(param.get("batchid"));
                Map<String, Object> list = tbExportBatchJobDutyService.queryExportBatch(batchid);
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(list);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getFileByConId Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }


    /**
     * 计划管理-岗位职责-获取导入信息
     */
    @RequestMapping(value = "/getImportMsg", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getImportMsg(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/jobDutyExcel/getImportMsg";
        boolean flag = true;
        try {
            // 如果获取参数userinfo不为空的话
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 验证指定参数
            if (JzbCheckParam.haveEmpty(param, new String[]{"batchid"})) {
                response = Response.getResponseError();
            } else {
                String batchid = JzbDataType.getString(param.get("batchid"));
                List<Map<String, Object>> list = tbExportBatchJobDutyService.queryExportList(batchid);
                response = Response.getResponseSuccess(userInfo);
                response.setResponseEntity(list);
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getFileByConId Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }



    /**
     * 计划管理-岗位职责-导出表格
     * 点击用户中的导入Excel表格上传模板
     *
     * @param
     * @author wangjixiang
     */
    @RequestMapping(value = "/createJobDuty", method = RequestMethod.POST)
    @CrossOrigin
    public void createJobDuty(HttpServletResponse response, @RequestBody Map<String, Object> param) {
        try {
            // 模板路径
            String srcFilePath = "static/excel/importJobDuty.xlsx";
            // 资源路径
            ClassPathResource resource = new ClassPathResource(srcFilePath);
            // 创建输入流
            InputStream in = resource.getInputStream();
            // 读取excel模板
            XSSFWorkbook wb = new XSSFWorkbook(in);
            // 读取了模板内所有sheet内容
            XSSFSheet sheet = wb.getSheetAt(0);

            // 响应到客户端
            response.addHeader("Content-Disposition", "attachment;filename=importJobDuty.xlsx");
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
     * CRM-销售业主-公海-用户
     * 点击项目中的导入Excel表格获取模板数据
     *
     * @param
     * @author chenhui
     */
    @RequestMapping(value = "/ImportJobDuty", method = RequestMethod.POST)
    @CrossOrigin
    public Response ImportCommonUser(@RequestBody MultipartFile file,
                                     @RequestHeader(value = "token") String token) {
        Response result = new Response();
        try {
            // 获取用户信息token
            Map<String, Object> userInfo = orgToken.getUserInfoByToken(token);
            Map<String, Object> param = new HashMap<>();
            param.put("adduid", JzbDataType.getString(userInfo.get("uid")));
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
//            param.put("cname", fileName);
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
            // 添加批次信息到岗位职责导入批次表
            tbExportBatchJobDutyService.addExportBatch(param);
            // 创建一个线程池
            ExecutorService pool = Executors.newFixedThreadPool(1);

            // 创建一个有返回值的任务
            Callable ImportCommonUser = new ImportJobDuty(filepath, param, result);
            Future future = pool.submit(ImportCommonUser);
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
     * 读取新建用户模板中的数据时启动线程
     * @author kuangbin
     */
    public class ImportJobDuty implements Callable {
        // 保存文件的路径
        private String filepath;

        // 前台传来的参数
        private Map<String, Object> param;

        // 结果对象
        Response result;

        public ImportJobDuty(String filepath, Map<String, Object> param, Response result) {
            this.filepath = filepath;
            this.param = param;
            this.result = result;
        }

        @Override
        public Response call() {
            List<Map<String, Object>> errorList=new ArrayList<>();

            // 读取模板中的数据
            List<Map<Integer, String>> list = JzbExcelOperater.readSheet(filepath);

            // 保存到用户导入信息表
            List<Map<String, Object>> userInfoList = new ArrayList<>();
            Map<String, Object> exportMap = null;

            List<Map<String,Object>> allMap = new ArrayList<>();

            // 遍历结果行,菜单数据从第2行开始
            for (int i = 1; i < list.size(); i++) {
                exportMap = new HashMap<>(param);
                // 设置行信息
                exportMap.put("idx", i);
                Map<Integer, String> map = list.get(i);

                // 获取模板中的部门名称
                String deptName = JzbDataType.getString(map.get(0).trim());
                // 定义批次中的备注
                String summary = "";
                exportMap.put("deptName", deptName);
                if (JzbDataType.isEmpty(deptName)) {
                    summary = "部门名称不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }else {
                    //根据部门名称获取部门id
                    List<String> deptId = tbExportBatchJobDutyService.selectDeptIdByDeptName(deptName);
                    if(deptId.size()>0){
                        // 在参数中加入用户名称
                        param.put("cddid", deptId.get(0));
                    }else {
                        summary = "请输入的您所在的部门！！!";
                        exportMap.put("status", "2");
                        exportMap.put("summary", summary);
                        userInfoList.add(exportMap);
                        errorList.add(exportMap);
                        continue;
                    }

                }

                // 获取模板中的角色名称
                String roleName = JzbDataType.getString(map.get(1).trim());

                exportMap.put("roleName", roleName);
                if (JzbDataType.isEmpty(roleName)) {
                    summary += "角色名称不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }else {
                    param.put("crcontent", roleName);
                }

                // 获取模板中的职责名称
                String dutyName = JzbDataType.getString(map.get(2).trim());
                exportMap.put("dutyName", dutyName);
                if (JzbDataType.isEmpty(dutyName)) {
                    summary += "职责不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }else {
                    param.put("dutycontent", dutyName);
                }
                // 获取模板中的输出名称
                String outputName = JzbDataType.getString(map.get(3).trim());
                exportMap.put("outputName", outputName);
                if (JzbDataType.isEmpty(outputName)) {
                    summary += "输出不能为空!";
                    exportMap.put("status", "2");
                    exportMap.put("summary", summary);
                    userInfoList.add(exportMap);
                    errorList.add(exportMap);
                    continue;
                }else {
                    param.put("outputcontent", outputName);
                }

                param.put("workcontent","");
                param.put("workstandardcontent","");
                param.put("kpicontent","");
                allMap.add(param);

                Map<String,Object> xxx = new HashMap<>();
                xxx.put("list",allMap);
                Response response = tbPlantaskJobPositionController.addJobResponsibilities(xxx);
                if(response.getServerResult().getResultCode()==200){
                    exportMap.put("status", "1");
                    userInfoList.add(exportMap);
                }else {
                    exportMap.put("status", "2");
                    exportMap.put("summary", "创建项目失败!");
                    userInfoList.add(exportMap);
                    continue;

                }
            }

            int export = 0;
            //保存用户导入信息表
            if (userInfoList.size() > 0) {
                export = tbExportBatchJobDutyService.insertExportDutyInfoList(userInfoList);
            }
            //导入完成后修改状态
            if (export >= 1) {
                exportMap.put("status", "8");
            } else if (export == 0) {
                exportMap.put("status", "4");
            }
            tbExportBatchJobDutyService.updateExportBatch(exportMap);
            result.setResponseEntity(errorList);
            return result;
        }
    }





}
