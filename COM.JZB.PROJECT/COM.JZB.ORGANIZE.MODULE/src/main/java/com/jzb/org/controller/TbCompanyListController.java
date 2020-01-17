package com.jzb.org.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.TbCompanyListService;
import com.jzb.org.util.SetPageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/org/CompanyList")
public class TbCompanyListController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyPropertyController.class);

    @Autowired
    private TbCompanyListService tbCompanyListService;

    /**
     * 所有业主-销售统计分析
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getManagerByCid", method = RequestMethod.POST)
    @CrossOrigin
    public Response getManagerByCid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                String cid = tbCompanyListService.queryManagerByCid(param);
                result=Response.getResponseSuccess();
                result.setResponseEntity(cid);
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }




    /**
     * 所有业主-销售统计分析
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置分页参数
                SetPageSize setPageSize = new SetPageSize();
                param = setPageSize.setPagenoSize(param);
                //获取前端传过来的分页参数
                //int count = JzbDataType.getInteger(param.get("count"));
                //获取总数
                /*count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCompanyListService.getCount(param);
                }*/
                //查询数据
                List<Map<String, Object>> list = tbCompanyListService.getCompanyList(param);

                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

                PageInfo pageInfo = new PageInfo();


                pageInfo.setList(list);
                //设置分页总数
                //pageInfo.setTotal(count > 0 ? count : list.size());
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
                for (int i = 0; i < list.size(); i++) {
                    pageInfo.setTotal(JzbDataType.getInteger(list.get(list.size() - 1).get("count")));
                }
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 所有业主-今日添加业主
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyListCount", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyListCount(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CompanyList/getCompanyListCount";
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
            //获取今天添加业主的数量
            int count = tbCompanyListService.getCompanyListCount(param);

            PageInfo pageInfo = new PageInfo();

            pageInfo.setTotal(count);
            // 获取用户信息返回
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            result.setPageInfo(pageInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getCompanyListCount Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**
     * 销售统计分析的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryCompanyList", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryCompanyList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CompanyList/queryCompanyList";
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
            List<Map<String,Object>> map = tbCompanyListService.queryCompanyList(param);
            PageInfo pageInfo = new PageInfo();
            //获取用户信息
            userInfo = (Map<String, Object>) param.get("userinfo");
            //响应成功信息

            result = Response.getResponseSuccess();
            result.setPageInfo(pageInfo);
            result.setResponseEntity(map);
        } catch (Exception ex) {
            //获取异常信息不捕捉进行日志信息的打印
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompanyList Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

}
