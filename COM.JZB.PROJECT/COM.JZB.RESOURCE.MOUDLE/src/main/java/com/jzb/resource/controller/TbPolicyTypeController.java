package com.jzb.resource.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbPolicyTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 获取政策类型
 */
@RestController
@RequestMapping(value = "/policyType")
public class TbPolicyTypeController {


    @Autowired
    private TbPolicyTypeService tbPolicyTypeService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbPolicyTypeController.class);

    /**
     * 查询政策类型（父子级）
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPolicyType",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyType(@RequestBody(required = false) Map<String ,Object> param){
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyType/getPolicyType";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }

            // 获取返回jsonArray
            JSONArray objects = tbPolicyTypeService.queryPolicyType(param);

            // 定义返回结果
            result = Response.getResponseSuccess();
            result.setResponseEntity(objects);

        }catch (Exception e){
            flag = false;
            // 打印异常信息
            JzbTools.logError(e);
            result=Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyType Method", e.toString()));
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
     * LBQ
     * 查询运营管理下政策中的菜单类别
     *
     * @param
     * @return
     */

    @RequestMapping(value = "/getPolicyTypes",method = RequestMethod.POST)
    @CrossOrigin
    public Response getPolicyTypes(@RequestBody Map<String,Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyType/getPolicyTypes";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //查询运营管理中的菜单类别
            List<Map<String, Object>> list = tbPolicyTypeService.getPolicyTypes(param);

            // 遍历转格式
            for (int i = 0, l = list.size(); i < l; i++) {
                list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
            }

            // 定义返回pageinfo
            PageInfo pageInfo = new PageInfo<>();
            pageInfo.setList(list);

            // 定义返回结果
            result = Response.getResponseSuccess(userInfo);

            // 设置返回pageinfo
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyTypes Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;

    }


    /** LBQ
     * 运营管理下政策模块中的菜单类别新增
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/savePolicyType", method = RequestMethod.POST)
    @CrossOrigin
    public Response savePolicyType(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyType/savePolicyType";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname"})) {
                result = Response.getResponseError();
            } else {
                //定义返回的结果
                param.put("adduid",userInfo.get("cname"));
                //添加一条记录
                int count = tbPolicyTypeService.savePolicyType(param);
                //如果返回值大于0，添加成功
                if (count > 0) {

                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.添加失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "savePolicyType Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }



    /**LBQ
     * 运营管理下政策菜单列表的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePolicyType", method = RequestMethod.POST)
    @CrossOrigin
    public Response updatePolicyType(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyType/updatePolicyType";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "cname"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录

                int count = tbPolicyTypeService.updatePolicyType(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updatePolicyType Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }

    /**LBQ
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateStatus(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyType/updateStatus";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果有空值，返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbPolicyTypeService.updateStatus(param);
                //如果返回值大于0，修改成功
                if (count > 0) {
                    //定义返回的结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateStatus Method", ex.toString()));
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
