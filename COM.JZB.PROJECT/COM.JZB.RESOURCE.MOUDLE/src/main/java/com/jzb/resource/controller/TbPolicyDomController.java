package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbPolicyDomService;
import com.jzb.resource.util.PageConvert;
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
     * 查询政策列表（模糊查询）
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPolicyDomList", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyDomList(@RequestBody Map<String, Object> params) {
        Response result;
        try {
            // 判断如果参数为空则返回404
            if(JzbCheckParam.haveEmpty(params,new String[]{"typeid","page","rows"})){
                result=Response.getResponseError();
            }else {
                // 设置分页参数
                PageConvert.setPageRows(params);
                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryPolicyDomList(params);
                // 遍历转换时间
                for (int i=0,l=list.size();i<l;i++){
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")),JzbDateStr.yyyy_MM_dd));
//                    list.get(i).put("context",JzbDataType.getString(list.get(i).get("context")).replace("\\\\"," "));
                }
                //定义pageinfo
                PageInfo pi=new PageInfo();
                pi.setList(list);
                pi.setTotal(tbPolicyDomService.queryDocumentsCount(params));
                // 定义返回response
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);
            }

        }catch (Exception e){
            // 打印异常信息
            e.printStackTrace();
            result=Response.getResponseError();
        }
        return result;
    }

    /**
     * 查询政策文档详情
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPolicyDomDesc",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyDomDesc(@RequestBody Map<String, Object> params){
        Response result;
        try {
            // 判断如果指定参数为空则返回404
            if(JzbCheckParam.haveEmpty(params,new String[]{"domid"})){

                result=Response.getResponseError();

            }else {

                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryPolicyDomDesc(params);
                for (int i=0,l=list.size();i<l;i++){
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")),JzbDateStr.yyyy_MM_dd));
//                    list.get(i).put("context",JzbDataType.getString(list.get(i).get("context")).replace("\\\\"," "));
                }
                // 定义pageinfo
                PageInfo pi=new PageInfo();
                pi.setList(list);

                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);

            }
        }catch (Exception e){

            // 打印异常信息
            e.printStackTrace();
            result=Response.getResponseError();

        }
        return result;
    }



    /**
     * 查询政策文档详情
     * @param params
     * @return
     */
    @RequestMapping(value = "/getPolicyHotDom",method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getPolicyHotDom(@RequestBody Map<String, Object> params){
        Response result;
        try {
            // 判断如果指定参数为空则返回404
            if(JzbCheckParam.haveEmpty(params,new String[]{"count"})){

                result=Response.getResponseError();

            }else {

                // 查询结果集
                List<Map<String, Object>> list = tbPolicyDomService.queryHotDom(params);

                // 热榜条件总条数
                int count=list.size();

                // 定义pageinfo
                PageInfo pi=new PageInfo();
                pi.setList(list);
                pi.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pi);

            }
        }catch (Exception e){

            // 打印异常信息
            e.printStackTrace();
            result=Response.getResponseError();

        }
        return result;
    }
}
