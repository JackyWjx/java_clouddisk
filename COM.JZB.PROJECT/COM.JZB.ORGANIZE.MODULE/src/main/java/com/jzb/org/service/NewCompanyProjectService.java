package com.jzb.org.service;

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

    public List<Map<String, Object>> queryCompanyNameBycid(Map<String, Object> param) {
        return  newCompanyProjectMapper.queryCompanyNameBycid(param);
    }

    public List<Map<String, Object>> queryPronameByid(Map<String, Object> param) {
        return newCompanyProjectMapper.queryPronameByid(param);
    }

    /**
     *  @author: gongWei
     *  @Date:  2019/12/20 20:02
     *  @Description: 根据cid获取一条公司信息和公司下的项目
     *  @Param:
     *  @Return:
     *  @Exception:
     */

    public Map<String,Object> getCompanyInfoByCid(Map<String, Object> param) {
//        Map<String,Object> companyInfo = newCompanyProjectMapper.queryCompanyInfoByCid(param);
//        List<Map<String,Object>> projectInfoList = newCompanyProjectMapper.queryProjectInfoByCid(param);
//        companyInfo.put("list",projectInfoList);
        return newCompanyProjectMapper.queryCompanyInfoByCid(param);
    }


    /**
     *  @author: gongWei
     *  @Date:  2019/12/21 9:51
     *  @Description: 根据项目id 获取项目基本信息 和 项目情报详情
     *  @Param:
     *  @Return:
     *  @Exception:
     */

    public Map<String, Object> getProjectInfoByProid(Map<String, Object> param) {
        Map<String,Object> project = newCompanyProjectMapper.queryProjectByProid(param);
        Map<String,Object> projectInfoList = newCompanyProjectMapper.queryProjectInfoByProid(param);
        project.put("infoList",projectInfoList);
        return project;
    }

    /**
     *  @author: gongWei
     *  @Date:  2019/12/31 11:31
     *  @description: 更新项目详情时 判断
     * @return
     */
    public boolean checkHaveInfoByProjectId(Map<String, Object> param) {
        int count = newCompanyProjectMapper.getProjectInfoCountByProid(param);
        return count > 0 ? true : false;
    }
}
