package com.jzb.org.service;

import com.jzb.org.dao.TbCommonProjectInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbCommonProjectInfoService {

    @Autowired
    private TbCommonProjectInfoMapper tbCommonProjectInfoMapper;
    /**
     * 项目情报的新增
     * @param param
     * @return
     */
    public int saveCommonProjectInfo(Map<String, Object> param) {
        //添加新建时间
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        return tbCommonProjectInfoMapper.saveCommonProjectInfo(param);
    }

    /**
     * 项目情报的修改
     * @param param
     * @return
     */
    public int updateCommonProjectInfo(Map<String, Object> param) {
        return tbCommonProjectInfoMapper.updateCommonProjectInfo(param);
    }

    /**
     * 项目情报的查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCommonProjectInfo(Map<String, Object> param) {
        return tbCommonProjectInfoMapper.getCommonProjectInfo(param);
    }
}
