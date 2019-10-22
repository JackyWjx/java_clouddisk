package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbPageConvert;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbContractTemplateItemService;
import com.jzb.resource.service.TbContractTemplateService;
import com.jzb.resource.util.CheckParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 合同模板
 */
@RestController
@RequestMapping(value = "/resource/contractTemplate")
public class TbContractTemplateController {
    @Autowired
    private TbContractTemplateService tbContractTemplateService;

    @Autowired
    private TbContractTemplateItemService tbContractTemplateItemService;

    /**
     * 查询合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/getContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response getContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断指定参数为空则返回error
            if(JzbCheckParam.haveEmpty(param,new String[]{"pageno","pagesize"})){
                result=Response.getResponseError();
            }else {
                // 设置好分页参数
                JzbPageConvert.setPageRows(param);

                // 获取结果集
                List<Map<String, Object>> list = tbContractTemplateService.queryContractTemplate(param);

                // 定义返回结果
                Map<String, Object> userInfo=(Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                PageInfo pageInfo=new PageInfo();
                pageInfo.setList(list);
                pageInfo.setTotal(tbContractTemplateService.getContractTemplateCount(param));
                result.setPageInfo(pageInfo);
            }

        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/addContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response addContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if(JzbCheckParam.haveEmpty(param,new String[]{"cname"})){
                result=Response.getResponseError();
            }else {
                String tempid="t000"+ JzbRandom.getRandomCharCap(5);
                param.put("tempid", tempid);
                int count=tbContractTemplateService.addContractTemplate(param);
                if(count>0){
                    result=Response.getResponseSuccess((Map<String, Object>) param.get("userinfo"));
                    // 获取模板项
                    List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                    // 放入模板id
                    for (int i=0,l=list.size();i<l;i++){
                        list.get(i).put("tempid",tempid);
                    }
                    tbContractTemplateItemService.addContractTemplateItem(list);
                }else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改合同模板
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateContractTemplate", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response updateContractTemplate(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = tbContractTemplateService.updateContractTemplate(param);
                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                //修改获取参数中的数据
                List<Map<String, Object>> list = (List<Map<String, Object>>) param.get("list");
                for (int i = 0; i < list.size(); i++) {
                       Map<String,Object> map = list.get(i);
                    if (list.get(i).get("itemid") == null || list.get(i).get("itemid") == "") {
                      int counts =  tbContractTemplateItemService.addContractTemplateItems(map);
                    } else {
                        tbContractTemplateItemService.updateContractTemplateItem(map);
                    }
                }


            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }
}
