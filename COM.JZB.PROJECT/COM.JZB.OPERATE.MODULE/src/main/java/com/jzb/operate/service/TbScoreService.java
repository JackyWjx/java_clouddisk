package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.dao.TbScoreMapper;
import com.jzb.operate.dao.TbUserScoreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    @Autowired
    TbUserScoreMapper scoreMapper;

    /**
     *  消耗积分
     */
    public Map<String ,Object> consumeUserIntegration(Map<String , Object> map){
        Map<String ,Object> paraMap  =  new HashMap<>();
        try{

            paraMap.clear();
            paraMap.put("message","success");
        }catch (Exception e){
            JzbTools.logError(e);
            paraMap.clear();
            paraMap.put("message","para is error");
        }
        return paraMap;
    }

    /**
     *  添加积分
     */
    public  boolean saveUserIntegration(Map<String , Object> map){
        boolean result;
        try{
            Map<String ,Object> paraMap  =  new HashMap<>();
            // 查询触发那个积分规则
            paraMap.put("optid",map.get("optid"));
            paraMap.put("page",1);
            paraMap.put("rows",10);
            List<Map<String , Object>> list  = mapper.qureyScoreRule(paraMap);
            // 如果未找到积分规则 则是错误的一次添加积分操作
            if(list.size() > 0){
                paraMap.put("uid",map.get("uid"));
                paraMap.put("pid",map.get("pid"));
                if(map.containsKey("cid")){
                    paraMap.put("cid",map.get("cid"));
                }
                paraMap.put("score",list.get(0).get("score"));
                paraMap.put("opttime",System.currentTimeMillis());
                // 积分日志
                mapper.insertScortList(paraMap);
                // 判断是否存在用户积分信息
                List<Map<String , Object>> ruleList = scoreMapper.qureyUserScore(paraMap);
                if(ruleList.size() > 0){
                    paraMap.clear();
                    // 添加积分
                    paraMap.put("sumscore", JzbDataType.getInteger(map.get("money")) + JzbDataType.getInteger(ruleList.get(0).get("sumscore")));
                    paraMap.put("consume", JzbDataType.getInteger(map.get("money")) + JzbDataType.getInteger(ruleList.get(0).get("consume")));
                    paraMap.put("recharge", JzbDataType.getInteger(map.get("money")) + JzbDataType.getInteger(ruleList.get(0).get("recharge")));
                    paraMap.put("score", JzbDataType.getInteger(list.get(0).get("score")) + JzbDataType.getInteger(ruleList.get(0).get("score")));
                    paraMap.put("id",ruleList.get(0).get("id"));
                    paraMap.put("uptime",System.currentTimeMillis());
                    scoreMapper.upUserScore(paraMap);
                }else{
                    // 创建用户积分表
                    paraMap.put("sumscore",map.get("money"));
                    paraMap.put("consume",map.get("money"));
                    paraMap.put("recharge",map.get("money"));
                    paraMap.put("freezes",0);
                    paraMap.put("updtime",System.currentTimeMillis());
                    paraMap.put("status","1");
                    scoreMapper.insertUserScore(paraMap);
                }
                result = true;
            }else{
                result = false;
            }
        }catch (Exception e){
            JzbTools.logError(e);
            result = false;
        }
        return result;
    }


    /**
     *   查询积分规则
     */
    public List<Map<String , Object>> qureyScoreRule(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.qureyScoreRule(map);
    }

    /**
     *   查询积分日志
     */
    public List<Map<String , Object>> qureyScoreList(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
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
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.seachScoreRule(map);
    }

    /**
     *   模糊查询积分日志
     */
    public List<Map<String , Object>> seachScoreList(Map<String, Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
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
