package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbScoreManualService;
import com.jzb.operate.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@RestController
@RequestMapping("/operate/score")
public class TbScoreManualTypeController {
    @Autowired
    TbScoreManualService scoreManual;

    @RequestMapping("/getScoreManualList")
    public Response getScoreManual(@RequestBody Map<String ,Object> paramap){

        Response response;
        try{
            //获取单位总数
            int count = JzbDataType.getInteger(paramap.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                //查询活动总数
                count = scoreManual.getCount(paramap);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramap);
            // 查询指导手册
            List<Map<String, Object>> list  = scoreManual.getActivity(paramap);
            response = Response.getResponseSuccess();
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count > 0 ? count : list.size());

            response.setPageInfo(pageInfo);
        }catch (Exception e){
            JzbTools.logError(e);
            response =  Response.getResponseError();
        }
        return response;



    }

    /**
     * 查询积分规则
     * @param paramp
     * @return
     */
    @RequestMapping("/getScoreRuleList")
    public Response getScoreRuleList(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            int count = JzbDataType.getInteger(paramp.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                // 查询活动总数
                count = scoreManual.getScoreRuleCount(paramp);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(paramp);
            // 查询积分规则
            List<Map<String,Object>> list = scoreManual.getScoreRule(paramp);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            pageInfo.setTotal(count > 0 ? count : list.size());
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 新建积分规则
     * @param paramp
     * @return
     */
    @RequestMapping("/addScoreRule")
    public Response addScoreRuleList(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            // 获取用户信息
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 加入新建积分规则内容
            int count = scoreManual.insertScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    @RequestMapping("/delScoreRule")
    public Response delScoreRule(@RequestBody Map<String ,Object> paramp){
        Response response;

        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            // 删除积分规则
            int count = scoreManual.delScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    @RequestMapping("/updScoreRule")
    public Response modifyScoreRule(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("ouid",userinfo.get("uid"));
            int count = scoreManual.updScoreRule(paramp);
            response = count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }


    @RequestMapping("/queryMyTask")
    public Response queryMyTask(@RequestBody Map<String,Object> paramp){
        Response response;
        // 查询用户信息
        try {
            Map<String ,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            // 查询积分值
            List<Map<String,Object>> list = scoreManual.querySocre(paramp);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            response = Response.getResponseSuccess(userinfo);
            response.setPageInfo(pageInfo);

        }catch (Exception e){
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 领取积分
     * @param paramp
     * @return
     */
    @RequestMapping("/modifyScoreStatus")
    public Response modifyScoreStatus(@RequestBody Map<String,Object> paramp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) paramp.get("userinfo");
            paramp.put("uid",userinfo.get("uid"));
            int count = scoreManual.modifyStatus(paramp);
            response =  count > 0 ? Response.getResponseSuccess(userinfo):Response.getResponseError();
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 查询消费明细
     * @param parmp
     * @return
     */
    @RequestMapping("/getConsumeList")
    public Response getConsumeList(@RequestBody Map<String ,Object> parmp){
        Response response;
        try {
            Map<String,Object> userinfo = (Map<String, Object>) parmp.get("userinfo");
            parmp.put("uid",userinfo.get("uid"));

            // 查询消费明细总记录数
            int count = JzbDataType.getInteger(parmp.get("count"));
            count = count < 0 ? 0:count;
            if (count == 0){
                count = scoreManual.getConsumeCount(parmp);
            }
            PageConvert pageConvert = new PageConvert();
            pageConvert.setPageRows(parmp);

            // 查询消费明细
            List<Map<String,Object>> counsumeList = scoreManual.getConsumeList(parmp);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(count);
            pageInfo.setList(counsumeList);
            response = Response.getResponseSuccess(userinfo);
            response.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;

    }
}
