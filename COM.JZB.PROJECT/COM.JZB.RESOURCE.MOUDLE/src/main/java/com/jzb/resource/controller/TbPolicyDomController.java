package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbPolicyDomService;
import com.jzb.resource.util.PageConvert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 政策文档操作
 */
@RestController
@RequestMapping(value = "/policyDom")
public class TbPolicyDomController {

    @Autowired
    private TbPolicyDomService tbPolicyDomService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbPolicyDomController.class);

    /**
     * 查询政策列表（模糊查询）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPolicyDomList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyDomList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/getPolicyDomList";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid", "pageno", "pagesize"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);
                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryPolicyDomList(param);
                // 遍历转换时间
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
//                    list.get(i).put("context",JzbDataType.getString(list.get(i).get("context")).replace("\\\\"," "));
                }
                //定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(tbPolicyDomService.queryDocumentsCount(param));
                // 定义返回response
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }

        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyDomList Method", e.toString()));
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
     * 查询政策文档详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPolicyDomDesc", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyDomDesc(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/getPolicyDomDesc";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {

                result = Response.getResponseError();

            } else {

                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryPolicyDomDesc(param);
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
//                    list.get(i).put("context",JzbDataType.getString(list.get(i).get("context")).replace("\\\\"," "));
                }
                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyDomDesc Method", e.toString()));
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
     * 查询政策文档详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getPolicyHotDom", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyHotDom(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/getPolicyHotDom";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 判断如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"count"})) {

                result = Response.getResponseError();

            } else {

                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryHotDom(param);

                // 热榜条件总条数
                int count = list.size();

                // 定义pageinfo
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);

            }
        } catch (Exception e) {
            flag = false;
            // 打印异常信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getPolicyHotDom Method", e.toString()));
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
     * 根据菜单类别进行查询 模糊查询 如果没有菜单类别则进行所有的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbPolicyDocument", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbStandardDocument(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/getTbPolicyDocument";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //判断分页参数是否为空
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbPolicyDomService.getTbPolicyDocument(param);

                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);

                //总数
                int count = tbPolicyDomService.getCount(param);
                pageInfo.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            flag = false;
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTbStandardDocument Method", ex.toString()));
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
     * 运营管理中政策中内容列表的新建
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbPolicyDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbPolicyDom(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/saveTbPolicyDom";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //判断如果指定参数有为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname"})) {
                result = Response.getResponseError();
            } else {
                param.put("adduid", userInfo.get("cname"));
                //添加一条模板记录
                int count = tbPolicyDomService.saveTbPolicyDom(param);

                //如果返回值大于0表示添加成功
                if (count > 0) {
                    //定义返回的结果

                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.添加失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTbStandardDocument Method", e.toString()));
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
     * 运营管理政策中内容列表的修改
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePolicyDom", method = RequestMethod.POST)
    @CrossOrigin
    public Response updatePolicyDom(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/updatePolicyDom";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //判断如果指定参数有为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname"})) {
                result = Response.getResponseError();
            } else {
                //修改一条模板记录
                int count = tbPolicyDomService.updatePolicyDom(param);

                //如果返回值大于0表示修改成功
                if (count > 0) {
                    //定义返回的结果
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //如果返回值小于等于0.修改失败
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            flag = false;
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getTbStandardDocument Method", e.toString()));
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
     * 设置删除状态
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateDelete(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/updateStatus";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                //修改一条记录
                int count = tbPolicyDomService.updateDelete(param);
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
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateDelete Method", ex.toString()));
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
     * 政策的SEO优化
     *
     * @param param
     * @return
     */

    @RequestMapping(value = "/updateSeo", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateSeo(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/updateSeo";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                int count = tbPolicyDomService.updateSeo(param);

                //获取用户信息
                //响应成功或者失败的
                result = count > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
        } catch (Exception e) {
            flag = false;
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateDelete Method", e.toString()));
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
     * 政策SEO优化的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getSeo", method = RequestMethod.POST)
    @CrossOrigin
    public Response getSeo(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/policyDom/getSeo";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            //如果指定参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"domid"})) {
                result = Response.getResponseError();
            } else {
                Map<String, Object> map = tbPolicyDomService.getSeo(param);

                //获取用户信息
                result = Response.getResponseSuccess();
                result.setResponseEntity(map);
            }
        } catch (Exception e) {
            flag = false;
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "updateDelete Method", e.toString()));
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
