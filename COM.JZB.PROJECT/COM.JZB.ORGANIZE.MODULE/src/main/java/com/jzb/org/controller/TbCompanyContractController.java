package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * <p>
 * 企业合同表
 */
@RestController
@RequestMapping(value = "/org/companyContract")
public class TbCompanyContractController {

    @Autowired
    private TbCompanyContractService tbCompanyContractService;

    @Autowired
    private TbHandleContractService tbHandleContractService;

    @Autowired
    private TbContractItemService tbContractItemService;

    @Autowired
    private TbContractProductService tbContractProductService;

    @Autowired
    private TbContractProductFunService tbContractProductFunService;

    @Autowired
    private TbContractServiceService tbContractServiceService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyCommonController.class);

    /**
     * 生成合同
     *
     * @param param
     * @return
     * @author chenzhengduan
     */
    @RequestMapping(value = "/addCompanyContract", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addCompanyContract(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyContract/addCompanyContract";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 验证指定参数为空则返回error
            if (JzbCheckParam.allNotEmpty(param, new String[]{"companyContractList", "contractProductList", "contractFunctionList", "contractServiceList"})) {

                // 获取合同id
                String contid = JzbRandom.getRandomCharLow(19);

                // 合同list
                List<Map<String, Object>> companyContractList = (List<Map<String, Object>>) param.get("companyContractList");

                // 获取当前系统时间
                long time = System.currentTimeMillis();

                // 合同记录
                Map<String, Object> map = companyContractList.get(0);
                map.put("contid", contid);
                map.put("addtime", time);
                map.put("updtime", time);

                int baseCount = tbCompanyContractService.addCompanyContract(map);

                // 如果合同添加成功后
                if (baseCount > 0) {

                    if (param.get("handleContractList") != null) {

                        // 合同动态属性
                        // begin ==========================================================>
                        List<Map<String, Object>> handleContractList = (List<Map<String, Object>>) param.get("handleContractList");

                        // 遍历存放合同id
                        for (int i = 0, l = handleContractList.size(); i < l; i++) {
                            handleContractList.get(i).put("contid", contid);
                            handleContractList.get(i).put("addtime", time);
                            handleContractList.get(i).put("updtime", time);
                        }
                        // 执行添加操作
                        tbHandleContractService.addHandleContract(handleContractList);
                        // end ==========================================================>

                    }

                    if (param.get("contractItemList") != null) {
                        // 企业合同条项
                        // begin ==========================================================>
                        List<Map<String, Object>> contractItemList = (List<Map<String, Object>>) param.get("contractItemList");
                        for (int i = 0, l = contractItemList.size(); i < l; i++) {
                            String itemid = JzbRandom.getRandomCharLow(11);
                            contractItemList.get(i).put("contid", contid);
                            contractItemList.get(i).put("itemid", itemid);
                            contractItemList.get(i).put("addtime", time);
                            contractItemList.get(i).put("updtime", time);
                        }
                        // 执行添加操作
                        tbContractItemService.addContractItem(contractItemList);
                        // end ==========================================================>

                    }

                    if (param.get("contractProductList") != null) {

                        // 企业合同产品
                        // begin ==========================================================>
                        List<Map<String, Object>> contractProductList = (List<Map<String, Object>>) param.get("contractProductList");
                        // 遍历存放合同id
                        for (int i = 0, l = contractProductList.size(); i < l; i++) {
                            contractProductList.get(i).put("contid", contid);
                            contractProductList.get(i).put("addtime", time);
                            contractProductList.get(i).put("updtime", time);
                        }
                        // 执行添加操作
                        tbContractProductService.addContractProduct(contractProductList);
                        // end ==========================================================>

                    }

                    if (param.get("contractFunctionList") != null) {
                        // 企业合同产品功能
                        // begin ==========================================================>
                        List<Map<String, Object>> contractFunctionList = (List<Map<String, Object>>) param.get("contractFunctionList");
                        // 遍历存放合同id
                        for (int i = 0, l = contractFunctionList.size(); i < l; i++) {
                            contractFunctionList.get(i).put("contid", contid);
                            contractFunctionList.get(i).put("addtime", time);
                            contractFunctionList.get(i).put("updtime", time);
                            contractFunctionList.get(i).put("funtype", JzbDataType.getInteger(contractFunctionList.get(i).get("funtype")));
                            contractFunctionList.get(i).put("funsubtype", JzbDataType.getInteger(contractFunctionList.get(i).get("funsubtype")));

                        }
                        // 执行添加操作
                        tbContractProductFunService.addContractProductFun(contractFunctionList);
                        // end ==========================================================>

                    }

                    if (param.get("contractServiceList") != null) {
                        // 企业合同服务
                        // begin ==========================================================>
                        List<Map<String, Object>> contractServiceList = (List<Map<String, Object>>) param.get("contractServiceList");
                        // 遍历存放合同id
                        for (int i = 0, l = contractServiceList.size(); i < l; i++) {
                            contractServiceList.get(i).put("contid", contid);
                            contractServiceList.get(i).put("addtime", time);
                            contractServiceList.get(i).put("updtime", time);
                        }
                        // 执行添加操作
                        tbContractServiceService.addContractService(contractServiceList);
                        // end ==========================================================>
                    }
                }

                // 添加成功则返回成功
                response = baseCount > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();

            } else {
                // 返回error
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addCompanyContract Method", "参数为空"));
            }
        } catch (Exception ex) {
            flag = false;
            // 打印异常信息
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addCompanyContract Method", ex.toString()));
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
     * 查询合同
     *
     * @param param
     * @return
     * @author chenzhengduan
     */
    @RequestMapping(value = "/getCompanyContract", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response queryCompantContract(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/org/companyContract/getCompanyContract";
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
            // 如果指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "count", "pageno", "pagesize"})) {
                response = Response.getResponseSuccess();
            } else {

                JzbPageConvert.setPageRows(param);
                // 查询合同
                List<Map<String, Object>> list = tbCompanyContractService.quertCompantContract(param);
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }
                response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list == null ? new ArrayList() : list);
                // 如果count>0 就返回list 大小
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
                response.setPageInfo(pageInfo);

            }
        } catch (Exception ex) {
            flag = false;
            // 打印错误信息
            JzbTools.logError(ex);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompantContract Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return response;
    }

}