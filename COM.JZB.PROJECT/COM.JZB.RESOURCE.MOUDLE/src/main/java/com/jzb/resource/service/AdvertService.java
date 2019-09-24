package com.jzb.resource.service;


import com.jzb.base.util.JzbTools;
import com.jzb.resource.dao.AdvertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 广告接口实现类
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 15:20
 */
@Service
public class AdvertService {

    @Autowired
    private AdvertMapper advertMapper;

    /**
     * 查询广告信息
     * @return  List<Map<String, Object>> 返回数组
     */
    public List<Map<String, Object>> queryAdvertisingList() {
        List<Map<String,Object>>  list = null;
        try {
              list = advertMapper.queryAdvertisingList();
        }catch (Exception e){
            JzbTools.logError(e);
            System.out.println("查询失败");
        }
        return  list;
    }
}
