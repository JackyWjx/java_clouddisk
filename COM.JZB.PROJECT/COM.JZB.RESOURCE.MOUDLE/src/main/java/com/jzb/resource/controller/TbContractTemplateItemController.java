package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/resource/contractTemplateItem")
public class TbContractTemplateItemController {
    @Autowired
    private TbContractTemplateItemService tbContractTemplateItemService;

    /**
     * 查询合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"tempid"})) {
                result = Response.getResponseError();
            } else {

                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByTempid(param);

                // 定义返回结果
                Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getBaseContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getBaseContractTemplateItem(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
                param.put("tempid","000000000");
                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByTempid(param);

                // 定义返回结果
                Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }



    /**
     * 引用模板
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplateItemByid", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplateItemByid(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断指定参数为空则返回error
            if (JzbCheckParam.haveEmpty(param, new String[]{"itemid"})) {
                result = Response.getResponseError();
            } else {

                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateItemService.quertContractTemplateItemByItemid(param);

                // 定义返回结果
                Map<String, Object> userinfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userinfo);
                PageInfo pi = new PageInfo();
                pi.setList(list);
                pi.setTotal(list.size());
                result.setPageInfo(pi);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }



    /**
     * 添加合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
            int count = tbContractTemplateItemService.addContractTemplateItem(list);
            if (count > 0) {
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改合同模板项
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractTemplateItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractTemplateItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {

            List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");

            result = tbContractTemplateItemService.updateContractTemplateItem(list) > 0 ? Response.getResponseSuccess((Map<String, Object>) param.get("userinfo")) : Response.getResponseError();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }



}
