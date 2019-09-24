package com.jzb.activity.controller;

import com.jzb.activity.service.ProductService;
import com.jzb.activity.vo.JsonPageInfo;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 产品控制层,与前端对接
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/13 9:43
 */
@RequestMapping("/product")
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 查询产品的信息
     * @param param 用 kv 存储
     * @return
     */
    @RequestMapping(value = "/findProductLsit", method = RequestMethod.POST)
    @CrossOrigin
    public Response findProductLsit(@RequestBody Map<String,Object> param){
        Response response;
        try {
            // 获取指定页记录
            List<Map<String, Object>> records = productService.getProductList(param);
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            response = Response.getResponseSuccess(userInfo);
            PageInfo page = new PageInfo();
            page.setList(records);

            // 当count为0时，获取总记录个数
            Object count = param.get("count");
            if (count != null && count.toString().equals("0")) {
                int size = productService.getProductTotal(param);
                page.setTotal(size > 0 ? size : records.size());
            }

            // 设置返回页数据
            response.setPageInfo(page);
        }catch (Exception e){
            response = Response.getResponseError();
            e.printStackTrace();
        }
        return  response;
    } // End findProductLsit
}