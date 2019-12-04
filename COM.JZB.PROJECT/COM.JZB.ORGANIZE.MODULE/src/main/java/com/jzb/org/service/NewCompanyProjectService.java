package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.org.dao.NewCompanyProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author sapientia
 * @Date 2019/12/4 14:19
 */
@Service
public class NewCompanyProjectService {

    @Autowired
    private NewCompanyProjectMapper newCompanyProjectMapper;

    public List<Map<String, Object>> queryCommonCompanyListBycid(Map<String, Object> map) {
        int page = JzbDataType.getInteger(map.get("pageno")) == 0 ? 0 : JzbDataType.getInteger(map.get("pageno")) - 1;
        map.put("pageno", page * JzbDataType.getInteger(map.get("pagesize")));
        map.put("pagesize", JzbDataType.getInteger(map.get("pagesize")));
        return newCompanyProjectMapper.queryCommonCompanyListBycid(map);
    }

    public List<Map<String, Object>> queryCompanyByid(Map<String, Object> map) {
        return newCompanyProjectMapper.queryCompanyByid(map);
    }

    public List<Map<String, Object>> queryCompanyByProjectid(Map<String, Object> map) {
        return newCompanyProjectMapper.queryCompanyByProjectid(map);
    }

    public int  updateCompanyProjectInfo(Map<String, Object> map) {
        return  newCompanyProjectMapper.updateCompanyProjectInfo(map);
    }
    public int  updateCompanyProject(Map<String, Object> map) {
        return  newCompanyProjectMapper.updateCompanyProject(map);
    }

    public int  updateCommonCompanyList(Map<String, Object> map) {
        return  newCompanyProjectMapper.updateCommonCompanyList(map);
    }
}
