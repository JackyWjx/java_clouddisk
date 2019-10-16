package com.jzb.resource.controller;

import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.resource.service.TbCostItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 获取特殊条目
 *
 * @author chenzhengduan
 */
@RestController
@RequestMapping(value = "/costItem")
public class TbCostItemController {

    @Autowired
    private TbCostItemService tbCostItemService;

    /**
     * @param param
     * @return
     * @deprecated 1.根据模板类型查询条目
     */
    @RequestMapping(value = "/getCostItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response getCostItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断参数为空返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"typeid"})) {
                result = Response.getResponseError();
            } else {

                PageInfo pi = new PageInfo();

                // 查询总数
                int count = tbCostItemService.getCostItemCount(param);

                // 查询结果
                List<Map<String, Object>> list = tbCostItemService.queryCostItem(param);

                // 把list放入pageinfo
                pi.setList(list);
                pi.setTotal(count);

                // 定义返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
                result.setPageInfo(pi);
            }

        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * @param param brotherid
     * @return
     * @deprecated 添加条目
     */
    @RequestMapping(value = "addCostItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response addCostItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "typeid", "ouid"})) {
                result = Response.getResponseSuccess();
            } else {
                // 添加返回值大于0 则添加成功
                int count = tbCostItemService.saveCostItem(param);
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
     * @param param itemid
     * @return
     * @deprecated 修改条目
     */
    @RequestMapping(value = "updateCostItem", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response updateCostItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 验证指定值为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"cname", "itemid"})) {
                result = Response.getResponseError();
            } else {

                // 添加返回值大于0 则添加成功
                int count = tbCostItemService.updateCostItem(param);
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
     * @param param(itemid)
     * @return
     * @deprecated 设置删除状态
     */
    @RequestMapping(value = "/setDelete", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    public Response setDelete(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 判断如果参数为空则返回404
            if (JzbCheckParam.haveEmpty(param, new String[]{"itemid"})) {
                result = Response.getResponseError();
            } else {

                // 添加返回值大于0 则添加成功
                int count = tbCostItemService.updateCostItemStatus(param);
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
}