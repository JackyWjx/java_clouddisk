package com.jzb.operate.service;

import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.dao.TbHandleItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;

@Service
public class TbHandleItemService {

    @Autowired
    private TbHandleItemMapper tbHandleItemMapper;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;
    /**
     * 跟进人详情查询
     * @param param
     * @return
     */
    public Map<String, Object> getHandleItem(Map<String, Object> param) {
        Map<String, Object> handleItem;
        //根据服务跟踪记录去查询数据
        if (param.get("service") != null) {
             handleItem = tbHandleItemMapper.getService(param);
            if (handleItem.get("projectid") == null) {
                handleItem.remove("projectid");
                handleItem.remove("handletime");
                handleItem.remove("summary");
                handleItem.remove("uid");
                handleItem.remove("invest");
                handleItem.remove("context");
                handleItem.remove("cname");
                handleItem.remove("needres");
                handleItem.remove(  "attach");

            }
            //根据用户的uid从缓存中获取用户的姓名
            if (handleItem != null) {
                Map<String,Object> map = (Map<String, Object>) userRedisServiceApi.getCacheUserInfo(handleItem).getResponseEntity();
                if (map != null) {
                    Set<String> strings = map.keySet();
                    for (String string : strings) {
                        Object uids = map.get("cname");
                        handleItem.put("cname",uids);
                    }
                }
            }
             //根据项目跟踪记录去查询数据
        } else if (param.get("item") != null) {
            handleItem =  tbHandleItemMapper.getItem(param);
            if (handleItem.get("projectid") == null) {
                handleItem.remove("projectid");
                handleItem.remove("handletime");
                handleItem.remove("summary");
                handleItem.remove("uid");
                handleItem.remove("invest");
                handleItem.remove("context");
                handleItem.remove("cname");
                handleItem.remove("needres");
                handleItem.remove(  "attach");
                //根据用户的uid从缓存中获取用户的姓名
                if (handleItem != null) {
                    Map<String,Object> map = (Map<String, Object>) userRedisServiceApi.getCacheUserInfo(handleItem).getResponseEntity();
                    if (map != null) {
                        Set<String> strings = map.keySet();
                        for (String string : strings) {
                            Object uids = map.get("cname");
                            handleItem.put("cname",uids);
                        }
                    }
                }
            }
        } else {
            //查询全部的
            handleItem = tbHandleItemMapper.getHandleItem(param);
            //根据用户的uid从缓存中获取用户的姓名
            if (handleItem != null) {
                Map<String, Object> map = (Map<String, Object>) userRedisServiceApi.getCacheUserInfo(handleItem).getResponseEntity();
                if (map != null) {
                    Set<String> strings = map.keySet();
                    for (String string : strings) {
                        Object uids = map.get("cname");
                        handleItem.put("cname", uids);
                    }
                }
            }
        }

        return handleItem;
    }
}
