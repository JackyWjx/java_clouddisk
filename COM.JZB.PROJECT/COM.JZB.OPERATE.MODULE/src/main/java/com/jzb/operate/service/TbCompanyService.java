package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.operate.api.org.TbCompanyProjectApi;
import com.jzb.operate.dao.TbCompanyServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TbCompanyService {

    @Autowired
    private TbCompanyServiceMapper tbCompanyServiceMapper;

    @Autowired
    private TbCompanyProjectApi tbCompanyProjectApi;

    /**
     * 项目跟进
     *
     * @param param
     * @return
     */
    public int saveProject(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("planid", JzbRandom.getRandomChar(11));
        param.put("updtime", time);
        Date handletime = JzbDataType.getDateTime(param.get("handletime"));
        Date nexttime = JzbDataType.getDateTime(JzbDataType.getInteger(param.get("nexttime")) / 1000);
        param.put("handletime", handletime.getTime());
        param.put("nexttime", nexttime.getTime());
        return tbCompanyServiceMapper.saveProject(param);
    }

    /**
     * 项目跟进的查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getProject(Map<String, Object> param) {
        return tbCompanyServiceMapper.getProject(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int getCompanyServiceCount(Map<String, Object> param) {
        param.put("status", "1");
        return tbCompanyServiceMapper.queryCompanyServiceCount(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getCompanyServiceList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryCompanyServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            Response response = tbCompanyProjectApi.getServiceProjectList(projectList);
            List<Map<String, Object>> list = response.getPageInfo().getList();
            for (int i = 0; i < projectList.size(); i++) {
                Map<String, Object> projectMap = projectList.get(i);
                projectMap.put("uid", JzbDataType.getString(param.get("uid")));
                int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                projectMap.put("service", count);
                for (int k = 0; k < list.size(); k++) {
                    Map<String, Object> map = list.get(i);
                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))) {
                        projectMap.putAll(map);
                        break;
                    }
                }
            }
        }
        return projectList;
    }

    /**
     * 设置分页数
     */
    public Map<String, Object> setPageSize(Map<String, Object> param) {
        int pageno = JzbDataType.getInteger(param.get("pageno"));
        int pagesize = JzbDataType.getInteger(param.get("pagesize"));
        pageno = pageno <= 0 ? 1 : pageno;
        pagesize = pagesize <= 0 ? 15 : pagesize;
        param.put("pageno", (pageno - 1) * pagesize);
        param.put("pagesize", pagesize);
        return param;
    }

    /**
     * CRM-销售业主-我服务的业主-2
     * 根据模糊搜索条件获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> searchCompanyServiceList(Map<String, Object> param) {
        param.put("status", "1");
        param = setPageSize(param);
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryCompanyServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            param.put("list", projectList);
            Response response = tbCompanyProjectApi.searchCompanyServiceList(param);
            List<Map<String, Object>> list = response.getPageInfo().getList();
            for (int i = projectList.size()-1; i >=0; i--) {
                Map<String, Object> projectMap = projectList.get(i);
                projectMap.put("uid", JzbDataType.getString(param.get("uid")));
                int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                projectMap.put("service", count);
                for (int k = 0; k < list.size(); k++) {
                    Map<String, Object> map = list.get(i);
                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))) {
                        projectMap.putAll(map);
                        break;
                    }
                    projectList.remove(i);
                }
            }
        }
        return projectList;
    }
}
