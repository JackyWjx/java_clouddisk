package com.jzb.operate.api.org;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author chenzhengduan
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/companyCommon")
@Repository
public interface OrgCompanyNameApi {


    /**
     * 主页获取单位名称
     *
     * @author kuangbin
     * para cid
     */
    @RequestMapping(value = "/getCompanyNameById", method = RequestMethod.POST)
    @CrossOrigin
    public Response getCompanyNameById(@RequestBody Map<String, Object> param);
}
