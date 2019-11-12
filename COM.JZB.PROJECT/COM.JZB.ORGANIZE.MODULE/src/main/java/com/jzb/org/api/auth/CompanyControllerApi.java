package com.jzb.org.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/11 16:55
 */

@FeignClient(name = "jzb-auth")
@RequestMapping(value = "/auth/list")
public interface CompanyControllerApi {
    /**
     * 根据用户姓名获取id合集
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchUidByUidCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchUidByUidCname(@RequestBody Map<String, Object> param);
}
