package com.jzb.activity.controller;

import com.jzb.activity.service.ProductService;
import com.jzb.base.log.JzbLoggerUtil;
import com.jzb.base.message.PageInfo;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 日志记录对象
     */
    private final static Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * 查询产品的信息
     * @param param 用 kv 存储
     * @return
     */
    @RequestMapping(value = "/findProductLsit", method = RequestMethod.POST)
    @CrossOrigin
    public Response findProductLsit(@RequestBody Map<String,Object> param){
        Response response;
        Map<String, Object> userInfo = null;
        String api = "/product/findProductLsit";
        boolean flag = true;
        try {
            if (param.get("userinfo") != null) {
                userInfo = (Map<String, Object>) param.get("userinfo");
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "INFO",
                        userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(), userInfo.get("msgTag").toString(), "User Login Message"));
            } else {
                logger.info(JzbLoggerUtil.getApiLogger(api, "1", "ERROR", "", "", "", "", "User Login Message"));
            }
            // 获取指定页记录
            List<Map<String, Object>> records = productService.getProductList(param);
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
            flag=false;
            response = Response.getResponseError();
            JzbTools.logError(e);
            logger.error(JzbLoggerUtil.getErrorLogger(userInfo == null ? "" : userInfo.get("msgTag").toString(), "findProductLsit Method", e.toString()));
        }
        if (userInfo != null) {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", flag ? "INFO" : "ERROR", userInfo.get("ip").toString(), userInfo.get("uid").toString(), userInfo.get("tkn").toString(),
                    userInfo.get("msgTag").toString(), "User Login Message"));
        } else {
            logger.info(JzbLoggerUtil.getApiLogger(api, "2", "ERROR", "", "", "", "", "User Login Message"));
        }
        return  response;
    } // End findProductLsit
}