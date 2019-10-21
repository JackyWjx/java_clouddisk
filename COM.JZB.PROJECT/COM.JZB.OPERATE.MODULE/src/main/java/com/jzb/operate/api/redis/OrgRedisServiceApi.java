package com.jzb.operate.api.redis;

import com.jzb.base.message.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

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
     * @param param 参数为cid，企业ID
     */
    @RequestMapping(value = "/getIdCompanyData", method = RequestMethod.POST)
    Response getIdCompanyData(@RequestBody Map<String, Object> param);

    /**
     * 存储企业信息至缓存中
     * @param record 参数为cid，企业ID
     */
    @RequestMapping(value = "/cacheIdCompanyData", method = RequestMethod.POST)
    Response cacheIdCompanyData(@RequestBody Map<String, Object> record);

    /**
     * 企业信息修改后,删除缓存中过时企业数据
     * @param param 参数为cid，企业ID
     */
    @RequestMapping(value = "/updateIdCompanyData", method = RequestMethod.POST)
    Response updateIdCompanyData(@RequestBody Map<String, Object> param);

    /**
     * 缓存产品或产品包的权限树
     *
     * @param param pid为产品或产品包id，list是权限树
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/7 15:00
     */
    @RequestMapping(value = "/cacheProductTree", method = RequestMethod.POST)
    Response cacheProductTree(@RequestBody Map<String, Object> param);

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

    /**
     * 判断在缓存中是否存在对应的key值
     * @param param 参数为cid，企业ID
     */
    @RequestMapping(value = "/comHasKey", method = RequestMethod.POST)
    Response comHasKey(@RequestBody Map<String, Object> param);

    /**
     * 判断在缓存中是否存在产品菜单树对应的key值
     * @param param 参数为pid，产品或产品包的id
     */
    @RequestMapping(value = "/comHasMenuTree", method = RequestMethod.POST)
    Response comHasMenuTree(@RequestBody Map<String, Object> param);

    /**
     * 删除缓存中存在产品菜单树
     * @param param 参数为pid，产品或产品包的id
     */
    @RequestMapping(value = "/removeProductTree", method = RequestMethod.POST)
    Response removeProductTree(@RequestBody Map<String, Object> param);
} // End interface OrgRedisServiceApi
