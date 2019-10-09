package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbPolicyDomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbPolicyDomService {
    @Autowired
    private TbPolicyDomMapper tbPolicyDomMapper;

    /**
     * 查询政策文档列表(模糊查询)
     */
    public List<Map<String, Object>> queryPolicyDomList(Map<String, Object> param) {
        return tbPolicyDomMapper.queryPolicyDomList(param);
    }

    /**
     * 标准文档总数
     */
    public int queryDocumentsCount(Map<String, Object> param) {
        return tbPolicyDomMapper.queryDocumentsCount(param);
    }

    /**
     * 查询政策文档详情
     */
    public List<Map<String, Object>> queryPolicyDomDesc(Map<String, Object> param) {
        return tbPolicyDomMapper.queryPolicyDomDesc(param);
    }

    /**
     * 查询热门文档
     */
    public List<Map<String, Object>> queryHotDom(Map<String, Object> param) {
        return tbPolicyDomMapper.queryHotDom(param);
    }

    /**
     * LBQ
     * 根据菜单类别进行查询  如果没有菜单类别则进行所有的查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbPolicyDocument(Map<String, Object> param) {
        return tbPolicyDomMapper.getTbPolicyDocument(param);
    }


    /**
     * LBQ
     * 运营管理中政策中内容列表的新建
     *
     * @param param
     * @return
     */
    public int saveTbPolicyDom(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("createtime", time);
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("domid", JzbRandom.getRandomCharLow(11));
        return tbPolicyDomMapper.saveTbPolicyDom(param);
    }

    /**
     * 运营管理政策中内容列表的修改
     *
     * @param param
     * @return
     */
    public int updatePolicyDom(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbPolicyDomMapper.updatePolicyDom(param);
    }

    /**
     * 设置删除状态
     *
     * @param param
     * @return
     */
    public int updateDelete(Map<String, Object> param) {
           //设置删除状态 4.表示删除
        param.put("status", "4");
        return tbPolicyDomMapper.updateDelete(param);
    }
}
