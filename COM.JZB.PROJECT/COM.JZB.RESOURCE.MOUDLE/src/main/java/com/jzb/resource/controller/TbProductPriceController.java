package com.jzb.resource.controller;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbTools;
import com.jzb.resource.service.AdvertService;
import com.jzb.resource.service.TbProductPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
    //用于调用分页参数的
    @Autowired
    private AdvertService advertService;

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
            //前端传过来的分页总条数
            int count = JzbDataType.getInteger(param.get("count"));
            // 获取产品报价总数
            count = count < 0 ? 0 : count;
            if (count == 0) {
                // 查询产品报价的总数
                count = tbProductPriceService.getTbProductPriceCount(param);
            }
            //把分页参数在设置好
            param = advertService.setPageSize(param);
            //返回所有合同配置中的产品功能
            List<Map<String, Object>> ProductPriceList = tbProductPriceService.getProductPrice(param);
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
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid"})) {
                result = Response.getResponseError();
            } else {
                //如果返回值大于0则表示添加成功否则添加失败
                result = tbProductPriceService.saveProductPrice(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
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
            //如果产品id为空  则返回错误信息
            if (JzbCheckParam.haveEmpty(param, new String[]{"pid"})) {
                result = Response.getResponseError();
            } else {
                //如果返回值大于0则表示修改成功否则添加失败
                result = tbProductPriceService.updateProductPrice(param) > 0 ? Response.getResponseSuccess() : Response.getResponseError();
            }
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