package com.jzb.auth.controller;

import com.jzb.auth.service.CompanyListService;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/***
 *
 *@Author
 *@Data 2019/7/26   11:06
 *@Describe 企业控制层
 *
 */
@RestController
@RequestMapping("/auth/list")
public class CompanyController {
    @Autowired
    private CompanyListService service;

    /**
     * 根据用户姓名获取id合集
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchUidByUidCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchUidByUidCname(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<String, Object> userInfo = (Map<String, Object>) param.get("userinfo");
            param.put("start", 0);
            param.put("pagesize", 100);
            String deList = service.searchUidByUidCname(param);
            result = Response.getResponseSuccess(userInfo);
            result.setResponseEntity(deList);
        } catch (Exception e) {
            JzbTools.logError(e);
            result = Response.getResponseError();
        }
        return result;
    }


}
