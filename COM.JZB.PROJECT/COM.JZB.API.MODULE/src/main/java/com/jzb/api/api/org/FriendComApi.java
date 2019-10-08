package com.jzb.api.api.org;

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
 * @Date: 2019/9/29 17:01
 */
@FeignClient(name = "jzb-org")
@RequestMapping(value = "/org/friend")
public interface FriendComApi {

    /**
     * 通过负责人或者单位名称查伙伴单位列表数据(组织微服务)
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/24 16:07
     */
    @RequestMapping(value = "/searchCompanyFriend", method = RequestMethod.POST)
    @CrossOrigin
    public Response searchCompanyFriend(@RequestBody Map<String, Object> param);
}
