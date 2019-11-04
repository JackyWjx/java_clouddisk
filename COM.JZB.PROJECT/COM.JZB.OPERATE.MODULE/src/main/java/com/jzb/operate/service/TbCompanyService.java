package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.operate.api.base.RegionBaseApi;
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
     * 查询地区信息
     */
    @Autowired
    private RegionBaseApi regionBaseApi;
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
        // 设置分页参数
        param = setPageSize(param);

        // 获取所有的服务记录
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryCompanyServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            // 根据服务的项目ID获取项目信息
            Response response = tbCompanyProjectApi.getServiceProjectList(projectList);

            // 获取查询到的返回值
            List<Map<String, Object>> list = response.getPageInfo().getList();
            for (int i = 0; i < projectList.size(); i++) {
                Map<String, Object> projectMap = projectList.get(i);
                projectMap.put("uid", JzbDataType.getString(param.get("uid")));
                // 查询人员对每个项目服务的次数
                int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                projectMap.put("service", count);
                for (int k = 0; k < list.size(); k++) {
                    Map<String, Object> map = list.get(k);
                    // 如果项目ID相同则将结果全部加入返回值中
                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))) {
                        projectMap.putAll(map);
                        Response region = regionBaseApi.getRegionInfo(projectMap);
                        projectMap.put("region", region.getResponseEntity());
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
        // 设置分页参数
        param = setPageSize(param);

        // 获取所有的服务记录
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryCompanyServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            // 加入查询条件项目ID
            param.put("list", projectList);

            // 根据服务的项目ID获取模糊搜索项目信息
            Response response = tbCompanyProjectApi.searchCompanyServiceList(param);
            List<Map<String, Object>> list = response.getPageInfo().getList();
            for (int i = projectList.size() - 1; i >= 0; i--) {
                // 设置初始开关
                boolean bl = false;
                Map<String, Object> projectMap = projectList.get(i);
                projectMap.put("uid", JzbDataType.getString(param.get("uid")));
                // 查询人员对每个项目服务的次数
                int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                projectMap.put("service", count);
                for (int k = 0; k < list.size(); k++) {
                    Map<String, Object> map = list.get(k);
                    // 如果项目ID相同则将结果全部加入返回值中
                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))) {
                        projectMap.putAll(map);
                        // 获取模糊查询中的总数,加入结果中
                        projectMap.put("count", response.getPageInfo().getTotal());

                        // 如果有相等的则将开关设置为true
                        bl = true;
                        break;
                    }
                }
                // 如果开关为false则说明此次循环没有结果,将本次循环的值删除
                if (!bl){
                    projectList.remove(i);
                }
            }
        }
        return projectList;
    }

    /**
     * 修改跟进记录
     * @return
     */
    public boolean upComanyService(Map<String, Object> param){
        param.put("updtime",System.currentTimeMillis());
        if(param.containsKey("ouid")){
            param.put("upduid",param.get("ouid"));
        }
        param.put("times", JzbDataType.getInteger(param.get("times")));
        return tbCompanyServiceMapper.upComanyService(param) > 0 ? true : false;
    }

    /**
     * 添加跟进记录
     * @return
     */
    public boolean saveComanyService(Map<String, Object> param) {
        param.put("addtime", System.currentTimeMillis());
        param.put("updtime", System.currentTimeMillis());
        if (param.containsKey("ouid")) {
            param.put("upduid", param.get("ouid"));
            param.put("adduid", param.get("ouid"));
        }
        param.put("planid", JzbRandom.getRandomChar(11));
        param.put("times", JzbDataType.getInteger(param.get("times")));
        return tbCompanyServiceMapper.saveComanyService(param) > 0 ? true : false;
    }

    /**
     * CRM-销售业主-我服务的业主-服务记录1
     * 显示我服务的所有记录的总数
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int getServiceListCount(Map<String, Object> param) {
        param.put("status", "1");
        return tbCompanyServiceMapper.queryServiceListCount(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getServiceList(Map<String, Object> param) {
        // 设置分页参数
        param = setPageSize(param);
        param.put("status", "1");
        // 获取所有的服务记录
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            // 根据服务的项目ID获取项目信息
            Response response = tbCompanyProjectApi.getServiceProjectList(projectList);

            // 获取查询到的返回值
            List<Map<String, Object>> list = response.getPageInfo().getList();
            for (int i = 0; i < projectList.size(); i++) {
                Map<String, Object> projectMap = projectList.get(i);
                for (int k = 0; k < list.size(); k++) {
                    Map<String, Object> map = list.get(k);
                    // 如果项目ID相同则将结果全部加入返回值中
                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))
                    && JzbDataType.getString(projectMap.get("cid")).equals(JzbDataType.getString(map.get("cid")))) {
                        projectMap.putAll(map);
                        break;
                    }
                }
            }
        }
        return projectList;
    }
}
