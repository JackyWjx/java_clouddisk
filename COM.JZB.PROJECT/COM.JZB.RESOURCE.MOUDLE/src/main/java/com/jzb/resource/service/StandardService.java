package com.jzb.resource.service;

import com.jzb.resource.dao.StandardMapper;
import com.jzb.resource.vo.JSONPageSize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author duanfeiyu
 * @Version v1.0
 * @Since 1.0
 * @Date: 2019/8/16 18:20
 */
@Service
public class StandardService {

    @Autowired
    private StandardMapper standardMapper;

    /**
     * 查询父子极
     *
     * @return
     */
    public List<Map<String, Object>> queryFatherOne() {

        return standardMapper.queryFatherOne();
    }

    /**
     * 标准文档总数
     *
     * @return 返回总数
     */
    public int queryDocumentsCount(Map<String, Object> params) {
        return standardMapper.queryDocumentsCount(params);
    }

    /**
     * 标准文档查询
     *
     * @param params
     * @return
     */
    public List<Map<String, Object>> queryDocumentsList(Map<String, Object> params) {
        return standardMapper.queryDocumentsList(params);
    }

    /**
     * 查询文档详情
     * @author chenzhengduan
     * @param params
     * @return
     */
    public List<Map<String, Object>> getStandardDocumentDesc(Map<String, Object> params) {
        return standardMapper.queryDocumentDesc(params);
    }

    /**
     * 文档热门榜
     */
    public List<Map<String, Object>> queryHotDom(Map<String, Object> param){
        return standardMapper.queryHotDom(param);
    }
}
