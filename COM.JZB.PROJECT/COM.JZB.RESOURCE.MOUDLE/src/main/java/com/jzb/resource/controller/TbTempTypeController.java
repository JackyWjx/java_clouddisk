package com.jzb.resource.controller;

import com.alibaba.fastjson.JSONArray;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateStr;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.resource.service.TbTempTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author chenzhengduan
 * 模板类型
 */
@RestController
@RequestMapping(value = "/resource/tempType")
public class TbTempTypeController {

    @Autowired
    private TbTempTypeService tbTempTypeService;

    /**
     * @return
     * @deprecated 查询模板大类，小类
     */
    @RequestMapping(value = "/getTempType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getTempType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 调用方法查询模板类别
            JSONArray list = tbTempTypeService.queryTempType();

            // 定义返回结果
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(list);
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param
     * @return
     * @deprecated 根据父级id查询模板类型
     */
    @RequestMapping(value = "/getTempTypeById", method = RequestMethod.POST)
    @ResponseBody
    public Response getTempTypeById(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"parentid"})) {
                //判断如果需要的参数为空则返回404
                result = Response.getResponseError();
            } else {
                //调用方法查询模板类别
                List<Map<String, Object>> list = tbTempTypeService.queryTempTypeById(param);

                // 遍历转格式
                for (int i = 0, l = list.size(); i < l; i++) {
                    list.get(i).put("addtime", JzbDateUtil.toDateString(JzbDataType.getLong(list.get(i).get("addtime")), JzbDateStr.yyyy_MM_dd));
                }

                // 定义返回pageinfo
                PageInfo pi = new PageInfo<>();
                pi.setList(list);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);

                // 设置返回pageinfo
                result.setPageInfo(pi);
            }
            return result;
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;

    }

    /**
     * @param param (cname,common,typecode,typedesc,parentid,cid,standdata,ouid)
     * @return
     * @date 2019-8-9 15.04
     * @deprecated 添加系统模板
     */
    @RequestMapping(value = "/saveTempType", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response saveTempType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果指定参数有为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "common", "parentid", "ouid", "standdata"})) {
                result = Response.getResponseError();
            } else {
                //添加一条模板记录
                int count = tbTempTypeService.saveTempType(param);

                // 返回count大于0 返回success 否则error
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param(cname,common,typecode,typedesc,typeid)
     * @return
     * @deprecated 修改系统模板
     */
    @RequestMapping(value = "/updateTempType", method = RequestMethod.POST)
    @ResponseBody
    public Response updateTempType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "common", "typeid"})) {
                result = Response.getResponseError();
            } else {
                //修改模板
                int count = tbTempTypeService.updateTempType(param);

                // 返回count大于0 返回success 否则error
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param(typeid)
     * @return
     * @deprecated 设置删除状态
     */
    @RequestMapping(value = "/setDelete", method = RequestMethod.POST)
    @ResponseBody
    public Response updateDataType(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {
                //设置删除模板
                int count = tbTempTypeService.setDelete(param);

                // 返回count大于0 返回success 否则error
                if (count > 0) {
                    // 定义返回结果
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 获取该单位是否设置了模板
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyIsAuth", method = RequestMethod.POST)
    @ResponseBody
    public Response queryCompanyIsAuth(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cid"})) {
                result = Response.getResponseError();
            } else {
                //设置删除模板
                int count = tbTempTypeService.queryCompanyIsAuth(param);
                result = Response.getResponseSuccess();
                result.setResponseEntity(count);
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }
}