package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.operate.dao.TbUserScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 用户积分/月报  业务
 * @Author Han Bin
 */
@Service
public class TbUserScoreService {

    @Autowired
    TbUserScoreMapper mapper;

    /**
     *   查询积分
     */
    public List<Map<String , Object>> qureyUserScore(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.qureyUserScore(map);
    }

    /**
     *   查询积分月报
     */
    public List<Map<String , Object>> qureyUserMonthScore(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.qureyUserMonthScore(map);
    }

    /**
     *   查询积分总数
     */
    public int qureyUserScoreCount(Map<String, Object> map){
        return mapper.qureyUserScoreCount(map);
    }

    /**
     *   查询积分月报总数
     */
    public int qureyUserMonthScoreCount(Map<String, Object> map){
        return mapper.qureyUserMonthScoreCount(map);
    }

    /**
     *   模糊查询积分
     */
    public List<Map<String , Object>> seachUserScore(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.seachUserScore(map);
    }

    /**
     *   模糊查询积分月报
     */
    public List<Map<String , Object>> seachUserMonthScore(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.seachUserMonthScore(map);
    }

    /**
     *   模糊查询积分总数
     */
    public int seachUserScoreCount(Map<String, Object> map){
        return mapper.seachUserScoreCount(map);
    }

    /**
     *   模糊查询积分变更总数
     */
    public int seachUserMonthScoreCount(Map<String, Object> map){
        return mapper.seachUserMonthScoreCount(map);
    }

    /**
     *   添加积分
     */
    public int saveUserScore(Map<String, Object> map){
        map.put("updtime",System.currentTimeMillis());
        map.put("status",'1');
        return mapper.insertUserScore(map);
    }

    /**
     *   添加积分月报
     */
    public int saveUserMonthScoreRule(Map<String, Object> map){
        map.put("otime",System.currentTimeMillis());
        return mapper.insertUserMonthScoreRule(map);
    }

    /**
     *   修改积分
     */
    public int upUserScore(Map<String, Object> map){
        map.put("updtime",System.currentTimeMillis());
        return mapper.upUserScore(map);
    }

    /**
     *   修改积分月报
     */
    public int upUserMonthScoreRule(Map<String, Object> map){
        map.put("otime",System.currentTimeMillis());
        return mapper.upUserMonthScoreRule(map);
    }

    /**
     *   禁用积分
     */
    public int removeUserScore(Map<String, Object> map){
        map.put("updtime",System.currentTimeMillis());
        return mapper.removeUserScore(map);
    }

}
