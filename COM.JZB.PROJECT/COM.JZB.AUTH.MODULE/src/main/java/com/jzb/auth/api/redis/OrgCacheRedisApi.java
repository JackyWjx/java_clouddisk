package com.jzb.auth.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/14 10:41
 */
@FeignClient(name = "jzb-redis")
@RequestMapping(value = "/redis/org")
@Repository
public interface OrgCacheRedisApi {
    
    /**
     * 从Redis服务器中获取一个部门的Map缓存数据。
     *
     * @param param
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/9 10:42
     */
    @RequestMapping(value = "/getDeptMap", method = RequestMethod.POST)
    Response getDeptMap(@RequestBody Map<String, Object> param);

    /**
     * 获取产品或产品包的权限树
     *
     * @param param 参数为pid，产品或产品包的id
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/7 15:39
     */
    @RequestMapping(value = "/getProductTree", method = RequestMethod.POST)
    Response getProductTree(@RequestBody Map<String, Object> param);
}
