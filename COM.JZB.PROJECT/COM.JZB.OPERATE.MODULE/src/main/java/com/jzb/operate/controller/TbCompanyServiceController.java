package com.jzb.operate.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;

import com.jzb.operate.service.TbCompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("operate/CompanyService")
public class TbCompanyServiceController {

    @Autowired
    private TbCompanyService tbCompanyService;


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
            result = tbCompanyService.upComanyService(param) ?  Response.getResponseSuccess((Map)param.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
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
    @RequestMapping(value = "/saveComanyService", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveComanyService(@RequestBody Map<String, Object> param){
        Response result;
        try {
            result = tbCompanyService.saveComanyService(param) ?  Response.getResponseSuccess((Map)param.get("userinfo")) : Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }



    /**
     * 项目跟进
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveProject", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveProject(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //如果参数为为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"projected"})) {
                result = Response.getResponseError();
            } else {
                int count = tbCompanyService.saveProject(param);
                if (count > 0) {
                    //存入用户信息
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"projected"})) {
                result = Response.getResponseError();
            } else {
                List<Map<String, Object>> list = tbCompanyService.getProject(param);
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
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

}
