package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbMethodTargetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbMethodTargetService {

    @Autowired
    private TbMethodTargetMapper tbMethodTargetMapper;

    /*
     * 1.查询方法论目标
     */
    public List<Map<String, Object>> queryMethodTarget(Map<String, Object> param) {
        return tbMethodTargetMapper.queryMethodTarget(param);
    }

    /*
     * 2.添加方法论目标
     * */
    public int addMethodTarget(List<Map<String, Object>> list) {
        Long time=System.currentTimeMillis();
        for (int i=0,l=list.size();i<l;i++){
            list.get(i).put("tarkey",list.get(i).get("dataid")+ JzbRandom.getRandomCharCap(2));
            list.get(i).put("addtime",time);
            list.get(i).put("updtime",time);
            list.get(i).put("idx",getMethodTargetIdx());
        }
        return tbMethodTargetMapper.addMethodTarget(list);
    }

    /*
     * 3.修改方法论目标状态（删除）
     * */
    public int updateMethodTargetStatus(Map<String, Object> param) {
        return tbMethodTargetMapper.updateMethodTargetStatus(param);
    }

    /*
     * 4.修改方法论目标
     * */
    public int updateMethodTarget(Map<String, Object> param) {
        Long time=System.currentTimeMillis();
        param.put("updtime",time);
        return tbMethodTargetMapper.updateMethodTarget(param);
    }

    /*
     * 5.获取排序
     * */
    public int getMethodTargetIdx() {
        return tbMethodTargetMapper.getMethodTargetIdx();
    }
}
