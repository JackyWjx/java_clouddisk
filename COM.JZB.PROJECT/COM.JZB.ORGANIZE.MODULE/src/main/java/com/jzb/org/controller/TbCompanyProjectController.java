package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.TbCityRedisApi;
import com.jzb.org.service.TbCompanyProjectService;
import com.jzb.org.util.SetPageSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "org/CompanyProject")
public class TbCompanyProjectController {

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbCompanyCommonController.class);

    @Autowired
    private TbCompanyProjectService tbCompanyProjectService;

    @Autowired
    private RegionBaseApi RegionBaseApi;


    /**
     *模糊查询 单位 项目名称
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompany", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompany(@RequestBody Map<String, Object> param){
        Response result;
        try {
            Map<String , Object> resultMap = new HashMap<>();
            List<Map<String , Object>> resultList = new ArrayList<>();
            List<Map<String , Object>> paraList = new ArrayList<>();
            List<Map<String , Object>> list  = (List<Map<String, Object>>) param.get("list");
            if(param.containsKey("cname")){
                for(int i = 0  ; i< list.size() ; i++){
                    Map<String , Object> comMap =  new HashMap<>();
                    comMap.put("cid",list.get(i).get("cid"));
                    comMap.put("cname",param.get("cname"));
                    Map<String , Object> com = tbCompanyProjectService.getCompany(comMap);
                    if(!JzbTools.isEmpty(com)){
                        paraList.add(list.get(i));
                    }
                }
            }else if(param.containsKey("projectname")){
                for(int i = 0  ; i< list.size() ; i++){
                    Map<String , Object> proMap =  new HashMap<>();
                    proMap.put("projectid",list.get(i).get("projectid"));
                    proMap.put("projectname",param.get("projectname"));
                    Map<String , Object> com = tbCompanyProjectService.getProject(proMap);
                    if(!JzbTools.isEmpty(com)){
                        paraList.add(list.get(i));
                    }
                }
            }
            // 索引
            int count = 1;
            // 页数
            int pageno  = JzbDataType.getInteger(param.get("pageno")) == 1 ? 0 : JzbDataType.getInteger(param.get("pageno"));
            // 页码
            int pagesize  = JzbDataType.getInteger(param.get("pagesize"));
            // 起始
            int begsize = pageno * pagesize;
            for(int  i = begsize ; i < paraList.size() ; i++){
                resultList.add(paraList.get(i));
                count ++;
                if(count >= pagesize ){
                    break;
                }
            }
            resultMap.put("list",resultList);
            resultMap.put("count",paraList.size());
            result = Response.getResponseSuccess();
            result.setResponseEntity(resultMap);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     *销售业主-公海-项目-数据查询 LBQ
     * @param param
     * @return
     */
    @RequestMapping(value = "/getComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComProject(@RequestBody Map<String, Object> param) {
        Response result;
        //判断请求参数如果为空则返回404
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pageno","pagesize"})) {
                result = Response.getResponseError();
            } else {
                //设置好分页参数
                SetPageSize setPageSize = new SetPageSize();
                param = setPageSize.setPagenoSize(param);

                //判断前端传过来的分页总数
                int count = JzbDataType.getInteger(param.get("count"));
                // 获取产品报价总数
                count = count < 0 ? 0 : count;
                if (count == 0) {
                    count = tbCompanyProjectService.getCount(param);
                }
                //查询项目模块下的数据
                List<Map<String, Object>> list = tbCompanyProjectService.getComProject(param);
                //获取用户信息
                for (int i = 0; i < list.size(); i++) {
                    Response cityList = RegionBaseApi.getRegionInfo(list.get(i));

                    list.get(i).put("region",cityList.getResponseEntity());
                }
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                //设置分页总数
                pageInfo.setTotal(count > 0 ? count : list.size());
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
     * 项目的添加
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveComProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projectname"})) {
                result = Response.getResponseError();
            } else {
                int count = tbCompanyProjectService.saveComProject(param);
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
     * 关联业主  根据项目id对项目表做修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateComProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> paramList = (List) param.get("list");

            int count = tbCompanyProjectService.updateComProject(paramList);
            if (count > 0) {
                //存入用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                //返回成功的响应消息
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 取消关联  根据项目id对项目表做修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/ComProject",method = RequestMethod.POST)
    @CrossOrigin
    public Response ComProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> paramList = (List) param.get("list");

            int count = tbCompanyProjectService.ComProject(paramList);
            if (count > 0) {
                //存入用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                //返回成功的响应消息
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getServiceProjectList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getServiceProjectList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = ( List<Map<String, Object>>)param.get("list");
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyProjectService.getServiceProjectList(list);
            result = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据服务的项目ID获取模糊搜索项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/searchCompanyServiceList", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyServiceList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = tbCompanyProjectService.getCompanyServiceCount(param);
            // 返回所有的企业列表
            List<Map<String, Object>> companyList = tbCompanyProjectService.searchServiceProjectList(param);
            result = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(companyList);
            pageInfo.setTotal(count);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 获取今日添加项目
     * @param param
     * @return
     */

    @RequestMapping(value = "/getComProjectCount",method = RequestMethod.POST)
    @CrossOrigin
    public Response getComProjectCount(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/org/CompanyProject/getComProjectCount";
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

            int count =  tbCompanyProjectService.getComProjectCount(param);

            PageInfo pageInfo = new PageInfo();

            pageInfo.setTotal(count);
            // 获取用户信息返回
            result = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
            result.setPageInfo(pageInfo);

        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "getComProjectCount Method", ex.toString()));
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
