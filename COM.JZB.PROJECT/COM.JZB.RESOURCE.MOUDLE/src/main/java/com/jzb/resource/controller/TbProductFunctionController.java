package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.TbProductFunctionService;
import com.jzb.resource.util.PageConvert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 合同配置中的产品功能
 */

@RestController
@RequestMapping(value = "/TbProductFunction")
public class TbProductFunctionController {
    @Autowired
    private TbProductFunctionService tbProductFunctionService;

    /**
     * 查询产品功能表对应的资源产品
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductFunction(@RequestBody(required = false) Map<String, Object> param) {
        Response result;
        try {
            //判断分页参数是否为空
            if (JzbCheckParam.haveEmpty(param, new String[]{"rows", "page"})) {
                result = Response.getResponseError();
            } else {
                // 设置分页参数
                PageConvert.setPageRows(param);

                List<Map<String, Object>> list = tbProductFunctionService.getTbProductFunction(param);

                PageInfo pageInfo = new PageInfo();
                pageInfo.setList(list);
                //总数
                int count = tbProductFunctionService.getCount(param);
                pageInfo.setTotal(count);
                // 定义返回结果
                result = Response.getResponseSuccess();
                result.setPageInfo(pageInfo);
            }
        } catch (Exception ex) {
            //错误信息
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductFunction",method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductFunction(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            //循环设置创建时间和修改时间
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                paramList.get(i).put("funid", JzbRandom.getRandomCharCap(15));
            }
              int count = tbProductFunctionService.saveTbProductFunction(paramList);
              //如果返回值大于0，表示添加成功,否则就是添加失败
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
        } catch (Exception e) {
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改产品功能
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductFunction",method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductFunction(@RequestBody Map<String,Object> param) {
        Response result;
        try {
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            //循环设置创建时间和修改时间
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("updtime", time);
            }
            int count = tbProductFunctionService.updateTbProductFunction(paramList);
            //如果返回值大于0，表示添加成功,否则就是添加失败
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 点击修改时查询产品功能表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductFunction", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductFunction(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> list = tbProductFunctionService.getProductFunction(param);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(list);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");

            //设置返回响应结果
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }

        return result;
    }
}
