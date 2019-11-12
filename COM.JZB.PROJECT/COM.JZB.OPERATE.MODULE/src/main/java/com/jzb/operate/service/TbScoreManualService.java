package com.jzb.operate.service;

import com.jzb.operate.dao.TbScoreManualMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/
@Service
public class TbScoreManualService {
    @Autowired
    TbScoreManualMapper scoreManual;

    //查询积分指导手册总数
    public Integer getCount(Map<String, Object> paramap) {
        return scoreManual.getCount(paramap);
    }

    public List<Map<String, Object>> getActivity(Map<String, Object> paramap) {
        return scoreManual.queryScoreManualList(paramap);
    }
}
