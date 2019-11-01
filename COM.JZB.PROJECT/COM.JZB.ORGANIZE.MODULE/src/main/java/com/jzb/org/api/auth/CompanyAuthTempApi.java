package com.jzb.org.api.auth;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@FeignClient(name = "jzb-resource")
@RequestMapping(value = "/resource/tempType")
@Repository
public interface CompanyAuthTempApi {

    /**
     * 添加企业角色表信息（角色表）
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getCompanyIsAuth", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyIsAuth(@RequestBody Map<String, Object> param);
}
