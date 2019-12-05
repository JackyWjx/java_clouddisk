package com.jzb.operate.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.operate.service.TbTravelApprovalService;
import com.jzb.operate.service.TbTravelPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author ：Champ-Ping
 * @date ：Created in 2019-12-04 20:09
 * @description：出差/报销申请
 * @modified By：
 * @version: 1.0$
 */
@RestController
@RequestMapping(value = "/operate/travelApproval")
public class TbTravelApprovalController {

    @Autowired
    TbTravelApprovalService travelApprovalService;
    @Autowired
    TbTravelPlanService travelPlanService;


    /**
     * 添加出差报销申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/addTravelApproval")
    public Response addTravelApproval(@RequestBody Map<String, Object> param) {

        Response response;

        try {
            List<Map<String,Object>> approvalList = (List<Map<String, Object>>) param.get("list");
            for (Map<String,Object> approval : approvalList){

                approval.put("travelid",param.get("travelid"));
                approval.put("apid", JzbRandom.getRandomChar(19));
                approval.put("addtime",System.currentTimeMillis());
                //默认状态和默认版本号
                approval.put("trstatus",1);
                approval.put("version",JzbRandom.getRandom(8));
                travelApprovalService.save(approval);
            }

            travelPlanService.updateTravelRecord(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 修改出差报销申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/updateTravelApproval")
    public Response updateTravelApproval(@RequestBody Map<String, Object> param) {

        Response response;

        try {
            List<Map<String,Object>> approvalList = (List<Map<String, Object>>) param.get("list");
            for (Map<String,Object> approval : approvalList){

                approval.put("travelid",param.get("travelid"));
                travelApprovalService.update(approval);
            }

            travelPlanService.updateTravelRecord(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }

    /**
     * 同意出差申请
     * param中添加
     * "isOk" : "0" 表示退回
     * "isOk" : "1" 表示同意
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/setTravelApproval")
    public Response setTravelApproval(@RequestBody Map<String, Object> param) {

        Response response;

        try {
            param.put("trtime",System.currentTimeMillis());//审批时间
            Integer isOk = (Integer) param.get("isOk");

            if(isOk == 1){// 同意
                //判断是否是最后一级审批人
                String lastApid = travelApprovalService.getMaxIdxApid((String) param.get("travelid"));
                if ( param.get("apid").equals(lastApid)){
                    param.put("trstatus", 3);
                }else {
                    param.put("trstatus", 2);
                }

            }else {// 退回
                param.put("trstatus", 4);
                //更新版本好
                param.put("version",JzbRandom.getRandom(8));

            }

            travelApprovalService.update(param);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }



    /**
     * 查询显示出差申请
     * @param param
     * @return
     */
    @CrossOrigin
    @Transactional
    @PostMapping("/selectTravelApproval")
    public Response setTravelApprovalOk(@RequestBody Map<String, Object> param) {

        Response response;

        try {

            PageInfo pageInfo = new PageInfo();
            List<Map<String , Object>>  approvalList = travelApprovalService.list(param);
            long count = travelApprovalService.count(param);
            pageInfo.setList(approvalList);
            pageInfo.setTotal(count);
            response = Response.getResponseSuccess();
            response.setPageInfo(pageInfo);
            response = Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));

        }catch (Exception e) {
            response = Response.getResponseError();
        }
        return response;
    }
}
