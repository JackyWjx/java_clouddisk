package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 合同配置中的产品报价
 */
@RestController
@RequestMapping("/resource/productPrice")
public class TbProductPriceController {

    @Autowired
    private TbProductPriceService tbProductPriceService;
    //用于调用分页参数
    @Autowired
    private AdvertService advertService;

    /**
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(TbContractTemplateController.class);

    /**
     * 添加产品报价的服务类型
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/addPriceService", method = RequestMethod.POST)
    @CrossOrigin
    public Response addPriceService(@RequestBody Map<String, Object> param) {
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/resource/productPrice/addPriceService";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid", "plid"})) {
                response = Response.getResponseError();
            } else {
                //添加数据
                param.put("ouid", userInfo.get("uid"));
                int count = tbProductPriceService.addPriceService(param);
                //响应成功结果
                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            flag = false;
            //打印错误信息
            JzbTools.logError(e);
            response = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "addPriceService Method", e.toString()));
        }

        return response;
    }

    /**
     * 修改
     * @param param
     * @return
     */
    @RequestMapping(value = "/updatePriceService", method = RequestMethod.POST)
    @CrossOrigin
    public Response updatePriceService(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid", "service"})) {
                response = Response.getResponseError();
            } else {
                //获取用户信息
                Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
                //执行修改
                int count = tbProductPriceService.updatePriceService(param);
                //响应结果信息
                response = count > 0 ? Response.getResponseSuccess(userInfo) : Response.getResponseError();
            }
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            response = Response.getResponseError();
        }
        return response;
    }
    /**
     * 删除合同模板
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/deleteProductPrice", method = RequestMethod.POST)
    @ResponseBody
    @CrossOrigin
    @Transactional
    public Response deleteProductPrice(@RequestBody Map<String, Object> param) {
        Response result;
        Map<String, Object> userInfo = null;
        String api = "/resource/productPrice/deleteProductPrice";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            if(JzbCheckParam.haveEmpty(param,new String[]{"servicetype"})){
                result=Response.getResponseError();
            }else {
                String[] strings = JzbDataType.getString(param.get("servicetype")).split(",");
                List<String> list = new ArrayList<>();
                for (int i = 0; i < strings.length; i++) {
                    list.add(strings[i]);
                }
                int count = list.size() > 0 ? tbProductPriceService.updatePriceStatus(list) : 0;
                result = count>0?Response.getResponseSuccess(userInfo):Response.getResponseError();
            }
        } catch (Exception ex) {
            flag = false;
            JzbTools.logError(ex);
            result = Response.getResponseError();
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "deleteProductPrice Method", ex.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return result;
    }


    /**
     * 根据产品线的id查资源
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/ProductPrice", method = RequestMethod.POST)
    @CrossOrigin
    @Transactional
    public Response getProductPrice(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 前端传过来的分页总条数
            int count = JzbDataType.getInteger(param.get("count"));

            // 获取产品报价总数
            count = count < 0 ? 0 : count;

            // 把分页参数在设置好
            param = advertService.setPageSize(param);
            //返回所有合同配置中的产品功能
            List<Map<String, Object>> ProductPriceList = tbProductPriceService.getProductPrice(param);
            if (count == 0) {
                // 查询产品报价的总数
                count = tbProductPriceService.getCount(param);
            }
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            result = Response.getResponseSuccess(userInfo);
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(ProductPriceList);
            //设置分页总数
            pageInfo.setTotal(count > 0 ? count : ProductPriceList.size());
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 添加产品价格的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/saveProductPrice", method = RequestMethod.POST)
    @CrossOrigin
    public Response saveProductPrice(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            //获取map中的list
            List<Map<String, Object>> paramList = (List) param.get("list");
            for (int i = 0; i < paramList.size(); i++) {
                long time = System.currentTimeMillis();
                paramList.get(i).put("addtime", time);
                paramList.get(i).put("updtime", time);
                paramList.get(i).put("itemid", JzbRandom.getRandomCharCap(13));
            }
            //如果返回值大于0则表示添加成功否则添加失败
            result = tbProductPriceService.saveProductPrice(paramList) > 0 ? Response.getResponseSuccess() : Response.getResponseError();

        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 修改产品价格表中的数据
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/updateProductPrice", method = RequestMethod.POST)
    @CrossOrigin
    public Response updateProductPrice(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> paramList = (List) param.get("list");
            int count;
            if (paramList == null || paramList.size() <= 0) {
                long time = System.currentTimeMillis();
                param.put("updtime", time);
                param.put("status", "2");
                count = tbProductPriceService.updateProductPrices(param);
            } else {
                for (int i = 0; i < paramList.size(); i++) {
                    long time = System.currentTimeMillis();
                    paramList.get(i).put("updtime", time);
                    if (paramList.get(i).get("itemid") == null) {
                        time = System.currentTimeMillis();
                        paramList.get(i).put("addtime", time);
                        paramList.get(i).put("updtime", time);
                        paramList.get(i).put("itemid", JzbRandom.getRandomChar(13));
                        tbProductPriceService.addProductPrice(paramList.get(i));
                        //如果返回值大于0则表示修改成功否则添加失败
                    }
                }
            }
            result = tbProductPriceService.updateProductPrice(paramList) >= 0 ? Response.getResponseSuccess() : Response.getResponseError();

        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


    /**
     * 点击修改查询产品价格返回给前端
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getTbProductPrice", method = RequestMethod.POST)
    @CrossOrigin
    public Response getTbProductPrice(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            List<Map<String, Object>> priceList = tbProductPriceService.getTbProductPrice(param);
            //获取用户信息
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            PageInfo pageInfo = new PageInfo();
            pageInfo.setList(priceList);
            result = Response.getResponseSuccess(userInfo);
            result.setPageInfo(pageInfo);
        } catch (Exception e) {
            //打印错误信息
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }
}