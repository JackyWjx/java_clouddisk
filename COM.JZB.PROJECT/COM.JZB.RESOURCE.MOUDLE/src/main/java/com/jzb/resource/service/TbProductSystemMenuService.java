package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbProductSystemMenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProductSystemMenuService {

    @Autowired
    private TbProductSystemMenuMapper tbProductSystemMenuMapper;


    // 查询
    public List<Map<String, Object>> queryMenuList(Map<String, Object> param){
        return tbProductSystemMenuMapper.queryMenuList(param);
    }

    // 添加
    public int addMenuList(Map<String, Object> param){
        long time=System.currentTimeMillis();
        param.put("menuid","jzbcrm00001"+ JzbRandom.getRandomCharCap(4));
        param.put("addtime",time);
        param.put("updtime",time);
        param.put("adduid",param.get("uid"));
        param.put("upduid",param.get("uid"));

        return tbProductSystemMenuMapper.addMenuList(param);
    }

    // 修改
    public int updateMenuList(Map<String, Object> param){
        return tbProductSystemMenuMapper.updateMenuList(param);
    }
}
