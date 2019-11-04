package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.dao.TbHandItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Description: 跟进详情
 * @Author Han Bin
 */
@Service
public class TbHandItemService {

    @Autowired
    TbHandItemMapper mapper;

    /**
     * 获取 服务记录
     *
     * @param map
     * @return
     */
    public List<Map<String , Object>> queryTbCompanyService(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.queryTbCompanyService(map);
    }

    /**
     * 获取 服务记录
     *
     * @param map
     * @return
     */
    public int queryTbCompanyServiceCount(Map<String , Object> map){
        return mapper.queryTbCompanyServiceCount(map);
    }

    /**
     * 根据项目id 获取项目跟进详情
     *
     * @param proid
     * @return
     */
    public List<Map<String , Object>> queryTbCompanyServiceNotDis(Map<String , Object> map){
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return mapper.queryTbCompanyServiceNotDis(map);
    }

    /**
     * 根据项目id 获取项目跟进详情总数
     *
     * @param
     * @return
     */
    public int  queryTbCompanyServiceNotDisCount(Map<String , Object> map){
        return mapper.queryTbCompanyServiceNotDisCount(map);
    }

    /**
     * 根据项目id 获取服务次数
     *
     * @param
     * @return
     */
    public int  queryCount(Map<String , Object> map){
        return mapper.queryCount(map);
    }

}
