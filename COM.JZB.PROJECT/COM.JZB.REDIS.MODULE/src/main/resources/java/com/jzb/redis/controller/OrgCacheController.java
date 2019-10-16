package com.jzb.redis.controller;

import com.alibaba.fastjson.JSON;
import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.JzbReturnCode;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbTools;
import com.jzb.redis.config.OrgCacheKeys;

import com.jzb.redis.service.OrganizeRedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 组织相关数据缓存到Redis
 *
 * @author Chad
 * @date 2019年7月20日
 */
@RestController
@RequestMapping(value = "/redis/org")
public class OrgCacheController {
    /**
     * 组织Redis服务对象
     */
    @Autowired
    private OrganizeRedisService redisService;

    /**
     * 从Redis中读取一个地区ID的地区信息
     * 请求参数，region
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/queryRegionById", method = RequestMethod.POST)
    public Map<String, Object> queryRegionById(@RequestBody Map<String, Object> param) {
        Map<String, Object> result = new HashMap<>();
        try {
            String name;
            Object region = param.get("region");
            if (region != null) {
                Map<Object, Object> map = redisService.getRedisService().getMap(region.toString());
                name = JzbDataType.getString(map.get("region"));
                if (!StringUtils.isEmpty(name)) {
                    result.put("region", name);
                }
            } else {
                result.put("region", "没想到把,我没有数据");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result.put("region", "没想到把,我出错了,你气不气");
        }
        return result;
    } // End getNameById

    /**
     * 从Redis服务器中获取一个部门的Map缓存数据。
     */
    @RequestMapping(value = "/getDeptMap", method = RequestMethod.POST)
    public Response getDeptMap(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<Object, Object> value = redisService.getRedisService().getMap(JzbDataType.getString(param.get("key")));
            result = new Response(JzbReturnCode.HTTP_200, "OK");
            result.setResponseEntity(value);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = new Response(JzbReturnCode.HTTP_404, "FAILED");
        }

        return result;
    } // End getDeptMap


    /**
     * 从Redis中读取一个地区ID的地区信息
     * 请求参数，region
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getOrgList", method = RequestMethod.POST)
    public Response getOrgList(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            Map<Object, Object> records = redisService.getRedisService().getMap(OrgCacheKeys.JZB_ORG_CID_INFO);
            List<Map<String, Object>> orgList = new ArrayList<>(records.size());
            for (Map.Entry<Object, Object> entry : records.entrySet()) {
                Map<String, Object> record = new HashMap<>();
                record.put("cid", entry.getKey().toString());
                record.put("name", entry.getValue());
                orgList.add(record);
            }
            result = Response.getResponseSuccess();
            result.setResponseEntity(orgList);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getOrgList

    /**
     * 从Redis中读取企业信息
     * 请求参数，cid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/getIdCompanyData", method = RequestMethod.POST)
    public Response getIdCompanyData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String cid = JzbDataType.getString(param.get("cid"));
            Map<Object, Object> record = redisService.getRedisService().getMap(OrgCacheKeys.JZB_ORG_CID_INFO + cid);
            if (record.size() == 0 || record == null) {
                result = Response.getResponseError();
            } else {
                result = Response.getResponseSuccess();
                result.setResponseEntity(record);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getIdCompanyData

    /**
     * 存储企业信息至缓存中
     * 请求参数，param
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/cacheIdCompanyData", method = RequestMethod.POST)
    public Response cacheIdCompanyData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            // 获取成立日期
            long birth = JzbDataType.getInteger(param.get("birthday"));
            // 判断是否有成立日期
            if (birth != 0) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                // 获取营业时间
                long limit = JzbDataType.getInteger(param.get("limitday"));
                if (limit != 0) {
                    date.setTime(limit);
                    // 将营业时间改为yyyy-MM-dd格式
                    String limitday = simpleDateFormat.format(date);
                    param.put("limitday", limitday);
                }
                date.setTime(birth);
                // 将成立日期改为yyyy-MM-dd格式
                String birthday = simpleDateFormat.format(date);
                param.put("birthday", birthday);
            }
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                }
                entry.setValue(entry.getValue().toString());
            }
            String cid = JzbDataType.getString(param.get("cid"));
            redisService.getRedisService().setMap(OrgCacheKeys.JZB_ORG_CID_INFO + cid, param);
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getIdCompanyData

    /**
     * 企业信息修改后,修改缓存中过时企业数据
     * 请求参数，param  cid
     *
     * @param param 请求参数
     */
    @RequestMapping(value = "/updateIdCompanyData", method = RequestMethod.POST)
    public Response updateIdCompanyData(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String cid = JzbDataType.getString(param.get("cid"));
            for (Map.Entry<String, Object> entry : param.entrySet()) {
                if (entry.getValue() == null) {
                    entry.setValue("");
                }
                redisService.getRedisService().setMapValue(
                        OrgCacheKeys.JZB_ORG_CID_INFO + cid, entry.getKey(), entry.getValue().toString());
            }
            result = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    } // End getIdCompanyData


    /**
     * 缓存产品或产品包的权限树
     *
     * @param param pid为产品或产品包id，list是权限树
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/7 15:00
     */
    @RequestMapping(value = "/cacheProductTree", method = RequestMethod.POST)
    public Response cacheProductTree(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            String pid = JzbDataType.getString(param.get("pid"));
            if (!JzbTools.isEmpty(pid)) {
                Map<String, Object> cacheInfo = new HashMap<>(1);
                cacheInfo.put(pid, JSON.toJSONString(param.get("list")));
                // 缓存产品或产品包的权限树
                redisService.getRedisService().setMap(OrgCacheKeys.JZB_ORG_PRODUCT_TREE + pid, cacheInfo);
                response = Response.getResponseSuccess();
            } else {
                response = Response.getResponseError();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }//cacheProductTree

    /**
     * 获取产品或产品包的权限树
     *
     * @param param 参数为pid，产品或产品包的id
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/7 15:39
     */
    @RequestMapping(value = "/getProductTree", method = RequestMethod.POST)
    public Response getProductTree(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String pid = JzbDataType.getString(param.get("pid"));
            Map<Object, Object> record = redisService.getRedisService().getMap(OrgCacheKeys.JZB_ORG_PRODUCT_TREE + pid);
            if (record.size() == 0 || record == null) {
                result = Response.getResponseError();
            } else {
                result = Response.getResponseSuccess();
                result.setResponseEntity(record);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }//getProductTree

    /**
     * 删除缓存产品或产品包的权限树
     *
     * @param param 参数为pid产品或产品包的id
     * @return com.jzb.base.message.Response
     * @Author: DingSC
     * @DateTime: 2019/9/7 15:08
     */
    @RequestMapping(value = "/removeProductTree", method = RequestMethod.POST)
    public Response removeProductTree(@RequestBody Map<String, Object> param) {
        Response response;
        try {
            String pid = JzbDataType.getString(param.get("pid"));
            if (!JzbTools.isEmpty(pid)) {
                // 删除缓存产品或产品包的权限树
                redisService.getRedisService().comRemoveKey(OrgCacheKeys.JZB_ORG_PRODUCT_TREE + pid);
            }
            response = Response.getResponseSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            response = Response.getResponseError();
        }
        return response;
    }//removeProductTree

    /**
     * 判断是否存在企业信息
     * 请求参数，param
     *
     * @param param 请求参数
     * @author kuangbin
     */
    @RequestMapping(value = "/comHasKey", method = RequestMethod.POST)
    public Response comHasKey(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String cid = JzbDataType.getString(param.get("cid"));
            // 判断是否存在KEY
            boolean bl = redisService.getRedisService().comHasKey(OrgCacheKeys.JZB_ORG_CID_INFO + cid);
            result = Response.getResponseSuccess();
            result.setResponseEntity(bl);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }

    /**
     * 判断在缓存中是否存在产品菜单树对应的key值
     * @param param 参数为pid，产品或产品包的id
     *
     * @author kuangbin
     */
    @RequestMapping(value = "/comHasMenuTree", method = RequestMethod.POST)
    public Response comHasMenuTree(@RequestBody Map<String, Object> param) {
        Response result;
        try {
            String pid = JzbDataType.getString(param.get("pid"));
            // 判断是否存在KEY
            boolean bl = redisService.getRedisService().comHasKey(OrgCacheKeys.JZB_ORG_PRODUCT_TREE + pid);
            result = Response.getResponseSuccess();
            result.setResponseEntity(bl);
        } catch (Exception ex) {
            ex.printStackTrace();
            result = Response.getResponseError();
        }
        return result;
    }
} // End class OrgCacheController
