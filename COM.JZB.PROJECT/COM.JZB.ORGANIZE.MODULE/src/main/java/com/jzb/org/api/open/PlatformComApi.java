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
@RequestMapping(value = "/open/com")
public interface PlatformComApi {

    /**
     * 获取所有开放平台的企业id
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/searchPlatformId", method = RequestMethod.POST)
    @CrossOrigin
    public Response getPlatformId(@RequestBody Map<String, Object> param);

    /**
     * 根据cid和集获取管理员信息
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     */
    @RequestMapping(value = "/getComAndMan", method = RequestMethod.POST)
    @CrossOrigin
    public Response getComAndMan(@RequestBody Map<String, Object> param);
}
