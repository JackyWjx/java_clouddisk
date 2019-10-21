package com.jzb.operate.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.service.TbHandleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author kuangBin
 * 项目跟进
 */
@RestController
@RequestMapping(value = "/operate/handle")
public class TbHandleController {

    @Autowired
    private TbHandleService tbHandleService;

    /**
     * CRM-销售业主-公海-业主下的人员13
     * 点击业主/项目/跟进信息获取项目下的跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/getHandlecItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response getHandlecItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
        Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
        int count = JzbDataType.getInteger(param.get("count"));
        // 获取单位总数
        count = count < 0 ? 0 : count;
        if (count == 0) {
            // 查询单位总数
            count = tbHandleService.getHandlecItemCount(param);
        }
        // 返回所有的企业列表
        List<Map<String, Object>> companyList = tbHandleService.getHandlecItem(param);
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
     * CRM-销售业主-公海-业主下的人员14
     * 点击业主/项目/跟进信息中点击添加跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/addHandlecItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response addHandlecItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("adduid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = tbHandleService.addHandlecItem(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * CRM-销售业主-公海-业主下的人员15
     * 点击业主/项目/跟进信息中点击修改跟进信息
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    @RequestMapping(value = "/modifyHandlecItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response modifyHandlecItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("ouid", JzbDataType.getString(userInfo.get("uid")));
            // 返回所有的企业列表
            int count = tbHandleService.modifyHandlecItem(param);
            result = count == 1 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
