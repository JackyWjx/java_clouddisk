package com.jzb.resource.service;

import com.jzb.resource.dao.TbStandardTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbStandardTypeService {

    @Autowired
    private TbStandardTypeMapper tbStandardTypeMapper;

    /**
     * 标准分类中的新建
     *
     * @return
     */
    public int saveStandardType(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        return tbStandardTypeMapper.saveStandardType(param);
    }

    /**
     * 标准分类下菜单分类修改
     *
     * @param param
     * @return
     */
    public int updateStandardType(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbStandardTypeMapper.updateStandardType(param);
    }


    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    public int updateStatus(Map<String, Object> param) {
        //修改状态，1.可用 2.禁用
        param.put("status", "2");
        return tbStandardTypeMapper.updateStatus(param);
    }

    /**
     * 查询运营管理中的菜单类别
     *
     * @param
     * @param param
     * @return
     */
    public List<Map<String, Object>> getStandardType(Map<String, Object> param) {
        return tbStandardTypeMapper.getStandardType(param);

    }
}
