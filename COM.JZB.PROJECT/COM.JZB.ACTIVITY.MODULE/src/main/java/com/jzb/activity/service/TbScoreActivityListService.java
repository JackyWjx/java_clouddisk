package com.jzb.activity.service;

import com.jzb.activity.dao.TbScoreActivityListMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@Service
public class TbScoreActivityListService {
    @Autowired
    TbScoreActivityListMapper scoreMapper;

    /**
     * 查询活动列表
     * @param paramap
     * @return
     */
    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {

    return scoreMapper.getActivity(paramap);
    }

    /**
     * 查询活动总数
     * @param paramap
     * @return
     */
    public int getCount(Map<String, Object> paramap) {
        return scoreMapper.getCount(paramap);
    }
}
