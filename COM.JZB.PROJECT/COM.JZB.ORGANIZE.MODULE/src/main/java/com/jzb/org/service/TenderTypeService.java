package com.jzb.org.service;

import com.jzb.org.dao.TenderTypeMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TenderTypeService {

    @Resource
    private TenderTypeMapper tenderTypeMapper;

    public List<Map<String, Object>> queryTenderType(Map<String, Object> param) {
        return tenderTypeMapper.queryTenderType(param);
    }

    public void addTenderType(Map<String, Object> param) {
        tenderTypeMapper.addTenderType(param);
    }

    public Integer selectMaxNum() {
        return  tenderTypeMapper.selectMaxNum();
    }

    public Integer delTenderType(String typeId) {
        return tenderTypeMapper.delTenderType(typeId);
    }

    public Integer putTenderType(Map<String, Object> param) {
        return tenderTypeMapper.putTenderType(param);
    }
}
