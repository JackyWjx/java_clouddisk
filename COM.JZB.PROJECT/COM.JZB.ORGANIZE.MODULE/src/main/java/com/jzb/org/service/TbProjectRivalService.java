package com.jzb.org.service;

import com.jzb.org.dao.TbProjectRivalMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbProjectRivalService {


    @Autowired
    private TbProjectRivalMapper tbProjectRivalMapper;

    /**
     * 项目竞争对手的添加
     *
     * @param param
     * @return
     */
    public int saveProjectRiva(Map<String, Object> param) {
        //添加新建时间
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        return tbProjectRivalMapper.saveProjectRiva(param);
    }

    /**
     * 项目竞争对手的修改
     * @param param
     * @return
     */
    public int updateProjectRiva(Map<String, Object> param) {
        //添加修改时间
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return tbProjectRivalMapper.updateProjectRiva(param);
    }

    /**
     * 项目竞争对手的查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProjectRiva(Map<String, Object> param) {
        return tbProjectRivalMapper.getProjectRiva(param);
    }

}
