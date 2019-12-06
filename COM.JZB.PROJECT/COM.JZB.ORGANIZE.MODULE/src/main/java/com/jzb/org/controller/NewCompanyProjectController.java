package com.jzb.org.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.org.service.NewCompanyProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/4 14:18
 */
@RestController
@RequestMapping(value = "/company/project")
public class NewCompanyProjectController {

    @Autowired
    private NewCompanyProjectService newCompanyProjectService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(NewCompanyProjectController.class);
    /**
     * @Author sapientia
     * @Date  14:48
     * @Description  查询项目详情
     **/
    @RequestMapping(value = "/queryCompanyByid" , method = RequestMethod.POST)
    @CrossOrigin
    public Response queryCompanyByid(@RequestBody Map<String, Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/operate/reimburseSystem/queryCompanyByid";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pagesize", "pageno", "cid", "projectid"})) {
                response = Response.getResponseError();
            } else {
                JzbPageConvert.setPageRows(param);
                // 获取出差详情
                param.put("uid", userInfo.get("uid"));
                //获取单位信息
                List<Map<String, Object>> list = newCompanyProjectService.queryCommonCompanyListBycid(param);
                for (int i = 0 ,a = list.size(); i < a; i++) {
                    Map<String, Object> proMap = new HashMap<>();
                    proMap.put("cid", list.get(i).get("cid").toString());
                    //获取单位
                    List<Map<String, Object>> prolist = newCompanyProjectService.queryCompanyByid(proMap);
                    //获取单位下的项目
                    for (int j = 0,b = prolist.size(); j < b; j++) {
                        Map<String, Object> infoMap = new HashMap<>();
                        infoMap.put("projectid", prolist.get(j).get("projectid").toString());
                        List<Map<String, Object>> infolist = newCompanyProjectService.queryCompanyByProjectid(infoMap);
                        list.get(j).put("infoList", infolist);
                    }
                    list.get(i).put("prolist", prolist);
                }
                response = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                response.setPageInfo(pageInfo);
            }
        }catch (Exception ex) {
                flag = false;
                JzbTools.logError(ex);
                response = Response.getResponseError();
                logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "queryCompanyByid Method", ex.toString()));
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
     * @Author sapientia
     * @Date  15:01
     * @Description 修改项目信息
     **/
    @PostMapping("/updateCompanyProject")
    @Transactional
    public Response updateCompanyProject(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCompanyProject(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date  18:11
     * @Description 修改项目详情信息
     **/
    @PostMapping("/updateCompanyProjectInfo")
    @Transactional
    public Response updateCompanyProjectInfo(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCompanyProjectInfo(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

    /**
     * @Author sapientia
     * @Date  18:12
     * @Description 修改单位信息
     **/
    @PostMapping("/updateCommonCompanyList")
    @Transactional
    public Response updateCommonCompanyList(@RequestBody Map<String, Object> map){
        Response result;
        try {
            map.put("updtime",System.currentTimeMillis());
            result = newCompanyProjectService.updateCommonCompanyList(map) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
        }catch (Exception e){
            e.printStackTrace();
            result =  Response.getResponseError();
        }
        return result;
    }

}
