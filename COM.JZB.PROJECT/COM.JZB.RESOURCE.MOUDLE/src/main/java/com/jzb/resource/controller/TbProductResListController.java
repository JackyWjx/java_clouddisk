package com.jzb.resource.controller;


import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductPriceService;
import com.jzb.resource.service.TbProductResListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/ProductResList")
public class TbProductResListController {

    //分页参数的设置
    @Autowired
    private AdvertService advertService;


    @Autowired
    private TbProductResListService tbProductResListService;

    @Autowired
    private TbProductPriceService tbProductPriceService;

    /**
     * 根据产品线的id查询产品表
     * 根据产品id查询出产品参数
     *
     * @param param
     * @return
     */

    @RequestMapping(value = "/getProductResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductResList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //前端传过来的总条数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = tbProductPriceService.getTbProductPriceCount(param);
            }
            //把分页参数在设置好
            param = advertService.setPageSize(param);
            //返回所有合同配置中的产品资源
            List<Map<String, Object>> productResList = tbProductResListService.getProductResList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(productResList);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : productResList.size());
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 根据产品的名称获取产品的参数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getProductResListCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response getProductResListCname(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //前端传过来的总条数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取单位总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询单位总数
                count = tbProductPriceService.getTbProductPriceCount(param);
            }
            //把分页参数在设置好
            param = advertService.setPageSize(param);
            List<Map<String, Object>> ProductResListCname = tbProductResListService.getProductResListCname(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(ProductResListCname);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : ProductResListCname.size());
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 合同配置中产品参数中新建中查询出产品名称
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/queryProductListCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response queryProductListCname(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> ProductResListCname = tbProductResListService.queryProductListCname(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(ProductResListCname);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception ex) {
            JzbTools.logError(ex);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加资源产品表的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductResList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid"})) {
                //添加资源产品表中的数据
                result = Response.getResponseError();
            } else {
                int count = tbProductResListService.saveTbProductResList(param);
                if (count > 0) {
                    Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                    result = Response.getResponseSuccess(userInfo);
                } else {
                    result = Response.getResponseError();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改资源产品表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductResList", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductResList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            int count = tbProductResListService.updateTbProductResList(param);
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            //打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 添加新建产品参数
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //获取Map参数中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                paramList.get(i).put("paraid", JzbRandom.getRandomCharCap(13));
            }
            //添加一条产品参数
            int count = tbProductResListService.saveTbProductParameteItem(paramList);
            //判断返回的参数来确定是否添加成功
            if (count > 0) {
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                //如果添加失败返回错误信息
                result = Response.getResponseError();
            }

        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改合同配置中的产品参数列表
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //获取Map参数中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            for (Map<String, Object> stringObjectMap : paramList) {
                long time = System.currentTimeMillis();
                stringObjectMap.put("updtime", time);
            }
            int count = tbProductResListService.updateTbProductParameteItem(paramList);
            //如果返回值大于0,代表修改成功 ， 否则就是修改失败
            if (count > 0) {
                //获取用户信息返回结果
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                result = Response.getResponseSuccess(userInfo);
            } else {
                result = Response.getResponseError();
            }
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 点击修改的时候进行查询返回给页面
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductParameteItem", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductParameteItem(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> itemList = tbProductResListService.getTbProductParameteItem(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(itemList);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }
}
