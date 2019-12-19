package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.operate.dao.TbVersionLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 日志业务
 * @Author Han Bin
 */
@Service
public class TbVersionLogService {

    @Autowired
    TbVersionLogMapper mapper;

    /**
    *  查询
    */
    public List<Map<String , Object>> queryVersionLog(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  mapper.queryVersionLog(map);
    }

    /**
     *  查询总数
     */
    public int queryVersionLogCount(Map<String , Object> map){
        return  mapper.queryVersionLogCount(map);
    }

    /**
     *  模糊查询
     */
    public List<Map<String , Object>> searchVersionLog(Map<String , Object> map){
        int page  = JzbDataType.getInteger(map.get("page"))  == 0  ? 0 : JzbDataType.getInteger(map.get("page"))- 1;
        map.put("page",page * JzbDataType.getInteger(map.get("rows")));
        map.put("rows",JzbDataType.getInteger(map.get("rows")));
        return  mapper.searchVersionLog(map);
    }

    /**
     *  模糊查询总数
     */
    public int searchVersionLogCount(Map<String , Object> map){
        return  mapper.searchVersionLogCount(map);
    }

    /**
     *  添加
     */
    public int  saveVersionLog(Map<String , Object> map){
        map.put("status",1);
        return  mapper.insertVersionLog(map);
    }

    /**
     *  修改
     */
    public int  upVersionLog(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  mapper.updateVersionLog(map);
    }

    /**
     *  禁用
     */
    public int  removeVersionLog(Map<String , Object> map){
        map.put("id",JzbDataType.getInteger(map.get("id")));
        return  mapper.deleteVersionLog(map);
    }
}
