package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.date.JzbDateUtil;
import com.jzb.operate.dao.TbScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 日志规则/日志 业务
 * @Author Han Bin
 */
@Service
public class TbScoreService {

    @Autowired
    TbScoreMapper mapper;


    /**
     *   查询积分规则
     */
    public List<Map<String , Object>> qureyScoreRule(Map<String, Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return mapper.qureyScoreRule(map);
    }

    /**
     *   查询积分日志
     */
    public List<Map<String , Object>> qureyScoreList(Map<String, Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return mapper.qureyScoreList(map);
    }

    /**
     *   查询积分规则总数
     */
    public int qureyScoreRuleCount(Map<String, Object> map){
        return mapper.qureyScoreRuleCount(map);
    }

    /**
     *   查询积分日志总数
     */
    public int qureyScoreListCount(Map<String, Object> map){
        return mapper.qureyScoreListCount(map);
    }

    /**
     *   模糊查询积分规则
     */
    public List<Map<String , Object>> seachScoreRule(Map<String, Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return mapper.seachScoreRule(map);
    }

    /**
     *   模糊查询积分日志
     */
    public List<Map<String , Object>> seachScoreList(Map<String, Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return mapper.seachScoreList(map);
    }

    /**
     *   模糊查询积分规则总数
     */
    public int seachScoreRuleCount(Map<String, Object> map){
        return mapper.seachScoreRuleCount(map);
    }

    /**
     *   模糊查询积分日志总数
     */
    public int seachScoreListCount(Map<String, Object> map){
        return mapper.seachScoreListCount(map);
    }

    /**
     *   添加积分日志
     */
    public int saveScortList(Map<String, Object> map){
        map.put("opttime", System.currentTimeMillis());
        return mapper.insertScortList(map);
    }

    /**
     *   添加积分规则
     */
    public int saveScortRule(Map<String, Object> map){
        map.put("status",'1');
        map.put("addtime",System.currentTimeMillis());
        map.put("updtime",System.currentTimeMillis());
        return mapper.insertScortRule(map);
    }

    /**
     *   修改积分规则
     */
    public int upScortRule(Map<String, Object> map){
        map.put("updtime",System.currentTimeMillis());
        return mapper.upScortRule(map);
    }

    /**
     *   修改积分日志
     */
    public int upScortList(Map<String, Object> map){
        map.put("opttime", System.currentTimeMillis());
        return mapper.upScortList(map);
    }

    /**
     *   禁用积分规则
     */
    public int removeScortRule(Map<String, Object> map){
        map.put("updtime",System.currentTimeMillis());
        return mapper.removeScortRule(map);
    }

}
