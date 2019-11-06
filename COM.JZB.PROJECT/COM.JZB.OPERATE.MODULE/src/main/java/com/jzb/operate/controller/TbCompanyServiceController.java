package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTimeConvert;
import com.jzb.base.util.JzbTools;

import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.service.TbCompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("operate/CompanyService")
public class TbCompanyServiceController {

    @Autowired
    private TbCompanyService tbCompanyService;


    @Autowired
    private UserRedisServiceApi userRedisServiceApi;


    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyMethodController.class);

    /**
     * 项目跟进
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectid"})) {
                result = Response.getResponseError();
            } else {
                int count = tbCompanyService.saveProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    PageInfo pageInfo = new PageInfo();
                    //返回成功的响应消息
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    //否则就输出错误信息
                    result = Response.getResponseError();
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
     * 项目跟进的查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid", "pagesize", "pageno", "count"})) {
                result = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                List<Map<String, Object>> list = tbCompanyService.getProject(param);
                for (int i = 0; i < list.size(); i++) {
                    param.put("uid",list.get(i).get("uid"));
                    Response region = userRedisServiceApi.getCacheUserInfo(param);
                    list.get(i).put("userInfo",region.getResponseEntity());
                    list.get(i).put("handletime", JzbTimeConvert.ToStringy_M_d_H_m_s(list.get(i).get("handletime")));
                    list.get(i).put("nexttime", JzbTimeConvert.ToStringy_M_d_H_m_s(list.get(i).get("nexttime")));
                    list.get(i).put("addtime", JzbTimeConvert.ToStringy_M_d_H_m_s(list.get(i).get("addtime")));
                    list.get(i).put("updtime", JzbTimeConvert.ToStringy_M_d_H_m_s(list.get(i).get("updtime")));
                }
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(JzbDataType.getInteger(param.get("count")) > 0 ? list.size() : 0);
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);

            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getCompanyServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 获取前台的总数
            int count = JzbDataType.getInteger(param.get("count"));
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有我服务的业主总数
                count = tbCompanyService.getCompanyServiceCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyService.getCompanyServiceList(param);
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
     * CRM-销售业主-我服务的业主-2
     * 根据模糊搜索条件获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/searchCompanyServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyService.searchCompanyServiceList(param);
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(companyList.size() == 0 ? 0: JzbDataType.getInteger(companyList.get(0).get("count")));
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改 服务记录
     * @auth  han bin
     * @param param
     * @return
     */
    @RequestMapping(value = "/upComanyService", method = RequestMethod.POST)
    @CrossOrigin
    public Response upComanyService(@RequestBody Map<String, Object> param){
        Response result;
        try {
            Map userinfo  =  (Map)param.get("userinfo");
            param.put("ouid",userinfo.get("uid"));
            result = tbCompanyService.upComanyService(param) ?  Response.getResponseSuccess(userinfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加 服务记录
     * @auth  han bin
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveComanyService", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveComanyService(@RequestBody Map<String, Object> param){
        Response result;
        try {
            Map userinfo  =  (Map)param.get("userinfo");
            param.put("ouid",userinfo.get("uid"));
            result = tbCompanyService.saveComanyService(param) ?  Response.getResponseSuccess(userinfo) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-服务记录1
     * 显示我服务的所有记录
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            // 获取前台的总数
            int count = JzbDataType.getInteger(param.get("count"));
            param.put("uid", JzbDataType.getString(userInfo.get("uid")));
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询所有我服务的业主总数
                count = tbCompanyService.getServiceListCount(param);
            }
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyService.getServiceList(param);
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
     * 所有业主-销售统计分析-服务跟踪记录
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryServiceList",method = RequestMethod.POST)
    @CrossOrigin
    public Response queryServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/operate/CompanyService/queryServiceList";
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"uid"})) {
                result = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = tbCompanyService.queryServiceList(param);

                PageInfo pageInfo = new PageInfo();
                //获取用户信息
                userInfo = (Map<String, Object>) param.get("userinfo");
                pageInfo.setList(list);
                //响应成功信息
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryServiceList Method", ex.toString()));
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
