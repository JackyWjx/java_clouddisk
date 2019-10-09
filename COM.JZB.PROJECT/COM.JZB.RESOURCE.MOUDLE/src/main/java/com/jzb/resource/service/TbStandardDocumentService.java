package com.jzb.resource.service;

import com.jzb.base.util.JzbRandom;
import com.jzb.resource.dao.TbStandardDocumentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 运营管理中的内容列表
 */
@Service
public class TbStandardDocumentService {

    @Autowired
    private TbStandardDocumentMapper tbStandardDocumentMapper;

    /**
     * 查询运营管理下的内容列表
     * @param param
     * @return
     */
    public List<Map<String, Object>> getTbStandardDocument(Map<String, Object> param) {
        return tbStandardDocumentMapper.getTbStandardDocument(param);
    }

    /**
     * 运营管理中标准中内容列表的新建
     * @param param
     * @return
     */
    public int saveTbStandardDom(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("createtime", time);
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("domid", JzbRandom.getRandomCharLow(11));
        return tbStandardDocumentMapper.saveTbStandardDom(param);
    }

    /**
     * 运营管理中标准中内容列表的修改
     * @param param
     * @return
     */
    public int updateTbStandardDom(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbStandardDocumentMapper.updateTbStandardDom(param);
    }

    /**
     * 设置删除状态
     * @param param
     * @return
     */
    public int updateDelete(Map<String, Object> param) {
        //状态 4.表示删除
        param.put("status", "4");
        return tbStandardDocumentMapper.updateDelete(param);
    }

    /**
     * 根据名称进行模糊查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCname(Map<String, Object> param) {
        return tbStandardDocumentMapper.getCname(param);
    }
}