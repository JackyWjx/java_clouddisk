package com.jzb.org.api.open;

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
 * @Date: 2019/11/8 10:54
 */
@FeignClient(name = "jzb-open")
@RequestMapping(value = "/open/page")
public interface OpenPageApi {

    /**
     * 查询开放平台应用列表
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchOrgApplication", method = RequestMethod.POST)
    @CrossOrigin
     Response searchOrgApplication(@RequestBody(required = false) Map<String, Object> param);
}
