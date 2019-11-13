package com.jzb.open.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @author dell
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/org")
public interface OrgRedisServiceApi {
    /**
     * 获取企业地区信息
     */
    @RequestMapping(value = "/queryRegionById", method = RequestMethod.POST)
    Response queryRegion(@RequestBody Map<String, Object> param);

    /**
     * 获取缓存中的企业信息
     *
     * @param param 参数为cid，企业ID
     * @return
     */
    @RequestMapping(value = "/getIdCompanyData", method = RequestMethod.POST)
    Response getIdCompanyData(@RequestBody Map<String, Object> param);


} // End interface OrgRedisServiceApi
