package com.jzb.open.api.org;

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
 * @Date: 2019/11/13 9:42
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/platform")
public interface PlatformCompanyApi {
    /**
     * 根据企业名称或企业cid集合获取cid合集
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchCidByCidCname", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCidByCidCname(@RequestBody Map<String, Object> param);
}
