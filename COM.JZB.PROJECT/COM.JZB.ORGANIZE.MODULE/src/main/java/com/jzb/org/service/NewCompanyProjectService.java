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

    public List<Map<String, Object>> queryCommonCompanyListBycid(Map<String, Object> param) {
        return newCompanyProjectMapper.queryCommonCompanyListBycid(param);
    }

    public List<Map<String, Object>> queryCompanyByid(Map<String, Object> param) {
        return newCompanyProjectMapper.queryCompanyByid(param);
    }

    public List<Map<String, Object>> queryCompanyByProjectid(Map<String, Object> param) {
        return newCompanyProjectMapper.queryCompanyByProjectid(param);
    }

    public int  updateCompanyProjectInfo(Map<String, Object> param) {
        return  newCompanyProjectMapper.updateCompanyProjectInfo(param);
    }
    public int  updateCompanyProject(Map<String, Object> param) {
        return  newCompanyProjectMapper.updateCompanyProject(param);
    }

    public int  updateCommonCompanyList(Map<String, Object> param) {
        return  newCompanyProjectMapper.updateCommonCompanyList(param);
    }

    public int countProjectInfo(Map<String, Object> param) {
        return newCompanyProjectMapper.countProjectInfo(param);
    }
}
