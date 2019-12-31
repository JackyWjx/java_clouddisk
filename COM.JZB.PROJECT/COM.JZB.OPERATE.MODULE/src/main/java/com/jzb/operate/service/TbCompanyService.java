package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTimeConvert;
import com.jzb.base.util.JzbTools;
import com.jzb.operate.api.auth.AuthUserApi;
import com.jzb.operate.api.base.RegionBaseApi;
import com.jzb.operate.api.org.CockpitApi;
import com.jzb.operate.api.org.TbCompanyListApi;
import com.jzb.operate.api.org.TbCompanyProjectApi;
import com.jzb.operate.api.redis.UserRedisServiceApi;
import com.jzb.operate.dao.TbCompanyServiceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TbCompanyService {

    @Autowired
    private TbCompanyServiceMapper tbCompanyServiceMapper;

    @Autowired
    private TbCompanyProjectApi tbCompanyProjectApi;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private TbCompanyListApi tbCompanyListApi;

    @Autowired
    private TbCompanyProjectApi projectApi;

    @Autowired
    private AuthUserApi authUserApi;

    @Autowired
    private CockpitApi cockpitApi;

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

    // 获取我服务的项目数量
    public  int queryMyProjectListCount(Map<String,Object> param){
        return tbCompanyServiceMapper.queryMyProjectListCount(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 获取所有的我服务的业主
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getCompanyServiceList(Map<String, Object> param) {
        List<Map<String, Object>> myProjectList = null;
        if (!JzbTools.isEmpty(param.get("projectname"))){
            Response projectidByname = tbCompanyProjectApi.getProjectidByname(param);
            List<Map<String,Object>> nList = projectidByname.getPageInfo().getList();
            if (!JzbTools.isEmpty(nList)){
            for (int i = 0; i < nList.size(); i++) {
                param.put("projectid",nList.get(i).get("projectid"));
                // 查询我服务的项目信息
                myProjectList = tbCompanyServiceMapper.queryMyProjectList(param);
                for (int j = 0; j < myProjectList.size(); j++) {
                    Map<String,Object> pmap = myProjectList.get(j);
                    param.put("projectid",pmap.get("projectid"));
                    // 跟进项目id获取项目信息
                    Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                    Map<String,Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                    if (!JzbTools.isEmpty(omap)){
                        pmap.putAll(omap);
                    }
                    Response region = regionBaseApi.getRegionInfo(omap);
                    pmap.put("region",region.getResponseEntity());
                    int count = tbCompanyServiceMapper.queryServiceCount(pmap);
                    pmap.put("service",count);

                    pmap.put("uid",pmap.get("oneheader"));
                    Response serviceRegion = userRedisServiceApi.getCacheUserInfo(pmap);
                    Map<String,Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                    if (!JzbTools.isEmpty(serviceMap)){
                        pmap.put("saler", serviceMap.get("cname"));
                    }
                }
            }
            }
        }else {
            // 查询我服务的项目信息
            myProjectList = tbCompanyServiceMapper.queryMyProjectList(param);
            for (int i = 0; i < myProjectList.size(); i++) {
                Map<String, Object> pmap = myProjectList.get(i);
                param.put("projectid", pmap.get("projectid"));
                // 跟进项目id获取项目信息
                Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                if (!JzbTools.isEmpty(omap)) {
                    pmap.putAll(omap);
                }
                Response region = regionBaseApi.getRegionInfo(omap);
                pmap.put("region", region.getResponseEntity());
                int count = tbCompanyServiceMapper.queryServiceCount(pmap);
                pmap.put("service", count);

                pmap.put("uid", pmap.get("oneheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(pmap);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    pmap.put("saler", serviceMap.get("cname"));
                }

            }
        }
        return myProjectList;
    }

    /**
     * 统计分析-销售统计分析
     * 获取所有已分配售后的单位项目信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCompanyProjectTrackList(Map<String, Object> param) {
        List<Map<String, Object>> projectList = new ArrayList<>();
        //判断是否为根据项目名称查询
        if (!JzbTools.isEmpty(param.get("projectname"))){
            Response projectidByname = tbCompanyProjectApi.getProjectidByname(param);
            List<Map<String,Object>> nList = projectidByname.getPageInfo().getList();
            for (int i = 0; i < nList.size(); i++) {
                Map<String, Object> projectMap = new HashMap<>();
                param.put("projectid",nList.get(i).get("projectid"));
                param.remove("uid");
                // 查询我服务的项目信息
                List<Map<String, Object>> pList = tbCompanyServiceMapper.queryMyProjectList(param);
                for (int j = 0; j < pList.size(); j++) {
                    // 跟进项目id获取项目信息
                    Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                    Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                    if (!JzbTools.isEmpty(omap)) {
                        projectMap.putAll(omap);
                    }
                    Response region = regionBaseApi.getRegionInfo(omap);
                    projectMap.put("region", region.getResponseEntity());
                    // 获取服务记录数
                    int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                    projectMap.put("service", count);

                    projectMap.put("uid", projectMap.get("oneheader"));
                    Response serviceRegion = userRedisServiceApi.getCacheUserInfo(projectMap);
                    Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                    if (!JzbTools.isEmpty(serviceMap)) {
                        projectMap.put("saler", serviceMap.get("cname"));
                    }
                    projectList.add(projectMap);
                }
            }
        }else if (!JzbTools.isEmpty(param.get("cname"))) {  // 判断是否根据单位名称查询
            // 调用接口根据单位名称查询单位下的项目
            Response response = tbCompanyProjectApi.getProjectByCname(param);
            List<Map<String, Object>> nList = response.getPageInfo().getList();
            for (int i = 0; i < nList.size(); i++) {
                // 存项目信息
                Map<String, Object> projectMap = new HashMap<>();
                if (!JzbTools.isEmpty(nList.get(i).get("projectid"))) {
                    param.put("projectid", nList.get(i).get("projectid"));
                    param.remove("uid");
                // 查询服务的项目信息
                List<Map<String,Object>> pList = tbCompanyServiceMapper.queryMyProjectList(param);
                for (int j = 0; j < pList.size(); j++) {
                    // 跟进项目id获取项目信息
                    Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                    Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                    if (!JzbTools.isEmpty(omap)) {
                        projectMap.putAll(omap);
                    }
                    Response region = regionBaseApi.getRegionInfo(omap);
                    projectMap.put("region", region.getResponseEntity());
                    // 获取服务记录数
                    int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                    projectMap.put("service", count);

                    projectMap.put("uid", projectMap.get("oneheader"));
                    Response serviceRegion = userRedisServiceApi.getCacheUserInfo(projectMap);
                    Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                    if (!JzbTools.isEmpty(serviceMap)) {
                        projectMap.put("saler", serviceMap.get("cname"));
                    }
                    projectList.add(projectMap);
                }
            }
        }

        }else if (!JzbTools.isEmpty(param.get("saler"))){
            // 根据销售员名称模糊查询项目id
            Response response = tbCompanyProjectApi.getProjectByUname(param);
            List<Map<String,Object>> ulist = response.getPageInfo().getList();
            for (int i = 0; i < ulist.size(); i++) {
                Map<String, Object> projectMap = new HashMap<>();
                Map<String, Object> uMap = ulist.get(i);
                param.put("projectid", uMap.get("projectid"));
                param.remove("uid");
                // 查询已分配售后人员的项目信息
                List<Map<String,Object>> pList = tbCompanyServiceMapper.queryServiceListGroupProject(param);
                for (int j = 0; j < pList.size(); j++) {
                    // 跟进项目id获取项目信息
                    Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                    Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                    if (!JzbTools.isEmpty(omap)) {
                        projectMap.putAll(omap);
                    }
                    Response region = regionBaseApi.getRegionInfo(omap);
                    projectMap.put("region", region.getResponseEntity());
                    // 获取服务记录数
                    int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                    projectMap.put("service", count);

                    projectMap.put("uid", projectMap.get("oneheader"));
                    Response serviceRegion = userRedisServiceApi.getCacheUserInfo(projectMap);
                    Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                    if (!JzbTools.isEmpty(serviceMap)) {
                        projectMap.put("saler", serviceMap.get("cname"));
                    }
                    projectList.add(projectMap);
                }
            }

        }else if (!JzbTools.isEmpty(param.get("cdid"))){
            // 根据部门id获取部门下所有销售员id
            Response response = tbCompanyProjectApi.getProjectByCdid(param);
            List<Map<String,Object>> ulist = response.getPageInfo().getList();
            for (int i = 0; i < ulist.size(); i++) {
                Map<String, Object> projectMap = new HashMap<>();
                Map<String, Object> uMap = ulist.get(i);
                param.put("projectid", uMap.get("projectid"));
                // 查询已分配售后人员的项目信息
                List<Map<String,Object>> pList = tbCompanyServiceMapper.queryServiceListGroupProject(param);
                for (int j = 0; j < pList.size(); j++) {
                    // 跟进项目id获取项目信息
                    Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                    Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                    if (!JzbTools.isEmpty(omap)) {
                        projectMap.putAll(omap);
                    }
                    Response region = regionBaseApi.getRegionInfo(omap);
                    projectMap.put("region", region.getResponseEntity());
                    // 获取服务记录数
                    int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                    projectMap.put("service", count);

                    projectMap.put("uid", projectMap.get("oneheader"));
                    Response serviceRegion = userRedisServiceApi.getCacheUserInfo(projectMap);
                    Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                    if (!JzbTools.isEmpty(serviceMap)) {
                        projectMap.put("saler", serviceMap.get("cname"));
                    }
                    projectList.add(projectMap);
                }
            }
        }else {
            // 查询已分配售后人员的项目信息
            projectList = tbCompanyServiceMapper.queryServiceListGroupProject(param);
            for (int i = 0; i < projectList.size(); i++) {
                Map<String, Object> projectMap = projectList.get(i);
                param.put("projectid", projectMap.get("projectid"));
                // 跟进项目id获取项目信息
                Response res = tbCompanyProjectApi.getServiceProjectInfoByProjectid(param);
                Map<String, Object> omap = (Map<String, Object>) res.getPageInfo().getList().get(0);
                if (!JzbTools.isEmpty(omap)) {
                    projectMap.putAll(omap);
                }
                Response region = regionBaseApi.getRegionInfo(omap);
                projectMap.put("region", region.getResponseEntity());
                // 获取服务记录数
                int count = tbCompanyServiceMapper.queryServiceCount(projectMap);
                projectMap.put("service", count);

                projectMap.put("uid", projectMap.get("oneheader"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(projectMap);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    projectMap.put("saler", serviceMap.get("cname"));
                }
            }
        }
        return projectList;
    }

    public int queryServiceCountGroup(Map<String, Object> param){
        return tbCompanyServiceMapper.queryServiceCountGroup(param);
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
     * 查询当日服务记录总数
     *
     * @return
     */
    public int queryCompanyServiceTypeCount(Map<String, Object> param) {
        return tbCompanyServiceMapper.queryCompanyServiceCount(param);
    }

    /**
     * 修改跟进记录
     *
     * @return
     */
    public boolean upComanyService(Map<String, Object> param) {
        param.put("updtime", System.currentTimeMillis());
        if (param.containsKey("ouid")) {
            param.put("upduid", param.get("ouid"));
        }
        param.put("times", JzbDataType.getInteger(param.get("times")));
        return tbCompanyServiceMapper.upComanyService(param) > 0 ? true : false;
    }

    /**
     * 添加跟进记录
     *
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
     * CRM-销售业主-我服务的业主-服务记录1
     * 显示我服务的所有记录
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getServiceList(Map<String, Object> param) {
        List<Map<String,Object>> fList = new ArrayList<>();
        // 设置分页参数
        param = setPageSize(param);
        param.put("status", "1");
        // 获取所有的服务记录
        List<Map<String, Object>> projectList = tbCompanyServiceMapper.queryServiceList(param);
        if (!JzbDataType.isEmpty(projectList)) {
            for (int i = 0; i < projectList.size(); i++) {
                Map<String, Object> projectMap = new HashMap<>();
                Map<String, Object> project = projectList.get(i);
                projectMap.putAll(project);
                project.put("projectid",project.get("projectid"));
                // 根据服务的项目ID获取项目信息
                Response response = tbCompanyProjectApi.getServiceProjectInfoByProjectid(project);
                // 获取查询到的返回值
                List<Map<String, Object>> list = response.getPageInfo().getList();
                projectMap.putAll(list.get(0));
                // 获取售后人员名称
                project.put("uid",project.get("servicer"));
                Response serviceRegion = userRedisServiceApi.getCacheUserInfo(project);
                Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
                if (!JzbTools.isEmpty(serviceMap)) {
                    projectMap.put("servicer", serviceMap.get("cname"));
                }
                param.put("uid",projectMap.get("oneheader"));
                Response salerRegion = userRedisServiceApi.getCacheUserInfo(param);
                Map<String, Object> salerMap = (Map<String, Object>) salerRegion.getResponseEntity();
                if (!JzbTools.isEmpty(salerMap)) {
                    projectMap.put("saler", salerMap.get("cname"));
                }
                fList.add(projectMap);
            }

//            project.put("pageno",param.get("pageno"));
//            project.put("pagesize",param.get("pagesize"));
            // 根据服务的项目ID获取项目信息
//            Response response = tbCompanyProjectApi.getServiceProjectInfoByProjectid(project);

            // 获取查询到的返回值
//            List<Map<String, Object>> list = response.getPageInfo().getList();
//            param.put("uid",list.get(0).get("oneheader"));
//            Response serviceRegion = userRedisServiceApi.getCacheUserInfo(param);
//            Map<String, Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
//            if (!JzbTools.isEmpty(serviceMap)) {
//                param.put("saler", serviceMap.get("cname"));
//            }
//            for (int i = 0; i < projectList.size(); i++) {
//                Map<String, Object> projectMap = projectList.get(i);
//                Response res = userRedisServiceApi.getCacheUserInfo(projectMap);
//                projectMap.put("uid", res.getResponseEntity());
//                for (int k = 0; k < list.size(); k++) {
//                    Map<String, Object> map = list.get(k);
//                    // 如果项目ID相同则将结果全部加入返回值中
//                    if (JzbDataType.getString(projectMap.get("projectid")).equals(JzbDataType.getString(map.get("projectid")))
//                            && JzbDataType.getString(projectMap.get("cid")).equals(JzbDataType.getString(map.get("cid")))) {
//                        projectMap.putAll(map);
//                        break;
//                    }
//                }
//            }
        }
        return fList;
    }

    /**
     * 销售统计分析服务跟踪记录
     * @param param
     * @return
     */
    public List<Map<String, Object>> queryServiceList(Map<String, Object> param) {
        List<Map<String, Object>> list = tbCompanyServiceMapper.getHandleItem(param);
        for (int i = 0; i < list.size(); i++) {
            //拿到cid 进行销售统计分析的查询
            //HashMap<String, Object> map = new HashMap<>();
            param.put("cid", list.get(i).get("cid"));
            //调用org服务的api进行销售统计分析的数据进行查询
            Response response = tbCompanyListApi.queryCompanyList(param);
            //查询出来的结果转成map
            List<Map<String,Object>> entity = (List<Map<String, Object>>) response.getResponseEntity();
            if (entity.size() <= 0) {
                list.get(i).put("projectname", "");
                list.get(i).put("dictvalue", "");
                list.get(i).put("unitName", "");
                list.get(i).put("contamount", "");
            }
            for (int j = 0; j < entity.size(); j++) {
                if (entity.get(j) != null) {
                    list.get(i).put("projectname", entity.get(j).get("projectname"));
                    list.get(i).put("dictvalue", entity.get(j).get("dictvalue"));
                    list.get(i).put("unitName", entity.get(j).get("cname"));
                    list.get(i).put("contamount", entity.get(j).get("contamount"));
                    break;
                }

                //根据等级进行条件查询
                if (param.get("dictvalue") != null && param.get("dictvalue") != "") {
                    if (entity.get(i) == null) {
                        list.get(i).clear();
                    }
                    //根据业主名称进行条件查询的判断
                } else if (param.get("cname") != null && param.get("cname") != "") {
                    if (entity.get(i) == null) {
                        list.get(i).clear();
                    }
                }
            }
        }
        return list;
    }


    /**
     *
     * @return
     * @param param
     */
    public int saveCompanyService(Map<String, Object> param) {
        //如果该项目的售后人员存在，就进行修改，一个项目只能有一个售后人员
        int count;
        if (tbCompanyServiceMapper.getCompanyService(param).size() > 0) {
            long time = System.currentTimeMillis();
            param.put("updtime", time);
           count = tbCompanyServiceMapper.updateCompanyService(param);
        } else {
            long time = System.currentTimeMillis();
            //如果不存在就进行售后人员的添加
            param.put("typeid", "2");
            param.put("addtime", time);
            param.put("updtime", time);
            count = tbCompanyServiceMapper.saveComanyService(param);
        }
        return count;
    }
}
