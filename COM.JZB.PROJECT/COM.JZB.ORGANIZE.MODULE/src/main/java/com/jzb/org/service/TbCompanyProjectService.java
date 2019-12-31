package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.auth.AuthApi;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
import com.jzb.org.dao.CockpitMapper;
import com.jzb.org.dao.TbCompanyProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TbCompanyProjectService {
    @Autowired
    private TbCompanyProjectMapper tbCompanyProjectMapper;

    @Autowired
    private UserRedisServiceApi userRedisServiceApi;

    @Autowired
    private AuthApi authApi;

    @Autowired
    private    CockpitMapper cockpitMapper;

    /**
     * 模糊查询单位名称
     * @param param
     * @return
     */
    public Map<String , Object> getCompany(Map<String, Object> param){
        return tbCompanyProjectMapper.getCompany(param);
    }

    /**
     * 模糊查询项目名称
     * @param param
     * @return
     */
    public Map<String , Object> getProject(Map<String, Object> param){
        return tbCompanyProjectMapper.getProject(param);
    }

    /**
     * 查询项目中的总数
     *
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
        return tbCompanyProjectMapper.getCount(param);
    }

    /**
     * 销售业主-公海-项目-数据查询和根据名称模糊查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getComProject(Map<String, Object> param) {
        param.put("isrelation",JzbDataType.getInteger(param.get("isrelation")));
        return tbCompanyProjectMapper.getComProject(param);
    }

    /**
     * 项目的添加
     *
     * @param param
     * @return
     */
    public int saveComProject(Map<String, Object> param) {


        long time = System.currentTimeMillis();
        param.put("addtime", time);
        param.put("updtime", time);
        param.put("projectid", JzbRandom.getRandomChar(19));
        //转时间戳
        Date tendertime = JzbDataType.getDateTime(param.get("tendertime"));
        param.put("tendertime", tendertime.getTime());
        return tbCompanyProjectMapper.saveComProject(param);
    }

    /**
     * 关联业主  根据项目id进行项目表的修改
     *
     * @param paramList
     * @return
     */
    public int updateComProject(List<Map<String, Object>> paramList) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).put("updtime", time);
        }

        return tbCompanyProjectMapper.updateComProject(paramList);
    }

    /**
     * 取消业主关联 根据项目id进行修改
     *
     * @param param
     * @return
     */
    public int ComProject(List<Map<String, Object>> param) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < param.size(); i++) {
            param.get(i).put("updtime", time);
        }
        return tbCompanyProjectMapper.ComProject(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息的总数,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public int getServiceProjectListCount(Map<String, Object> param) {
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> list = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < list.size(); i++) {
                list.get(i).remove("pcdid");
                list.get(i).remove("idx");
            }
            param.put("list",list);
        }
        return tbCompanyProjectMapper.getServiceProjectListCount(param);
    }

    /**
     * CRM-销售业主-我服务的业主-1
     * 根据服务的项目ID获取项目信息,后台调用不支持前台调用
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getServiceProjectList(Map<String, Object> param) {
        if (!JzbTools.isEmpty(param.get("cdid"))){
            List<Map<String,Object>> deptChildlist = cockpitMapper.getDeptChild(param);
            for (int i = 0; i < deptChildlist.size(); i++) {
                deptChildlist.get(i).remove("pcdid");
                deptChildlist.get(i).remove("idx");
            }
            param.put("list",deptChildlist);
        }
        List<Map<String, Object>> list = tbCompanyProjectMapper.queryServiceProjectList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> uidMap = list.get(i);
            uidMap.put("uid",uidMap.get("oneheader"));
            Response region = userRedisServiceApi.getCacheUserInfo(uidMap);
            Map<String,Object> map = (Map<String, Object>) region.getResponseEntity();
            if (!JzbTools.isEmpty(map)){
                uidMap.put("saler", map.get("cname"));
            }
            uidMap.put("uid",uidMap.get("dictvalue"));
            Response serviceRegion = userRedisServiceApi.getCacheUserInfo(uidMap);
            Map<String,Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
            if (!JzbTools.isEmpty(serviceMap)){
                uidMap.put("servicer", serviceMap.get("cname"));
            }
        }
        return list;
    }

    /**
     * CRM-销售业主-我服务的业主-2
     * 获取所有人的uid
     *
     * @Author: Kuang Bin
     * @DateTime: 2019/10/19
     */
    public List<Map<String, Object>> getServiceProjectUid(Map<String, Object> param) {
        return tbCompanyProjectMapper.queryServiceProjectUid(param);
    }

    /**
     * 获取今日添加项目的数量
     * @param param
     * @return
     */
    public int getComProjectCount(Map<String, Object> param) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setTimeZone(TimeZone.getDefault());
        String now = sdf.format(date);
        try
        {
            //当前时区下的0时0分0秒
            Date newDate = sdf.parse(now);
            long start1 = newDate.getTime();
            param.put("startTime", start1);
            //获取当天23时59分59秒
            param.put("endTime", start1 + 24 * 60 * 60 * 1000 - 1);
        }
        catch (Exception e)
        {
            JzbTools.logError(e);
        }

        return tbCompanyProjectMapper.getComProjectCount(param);
    }

    public List<Map<String, Object>> getServiceByProjectid(Map<String, Object> param) {
        List<Map<String,Object>> oList = (List<Map<String, Object>>) tbCompanyProjectMapper.getServiceByProjectid(param);
        for (int i = 0; i < oList.size(); i++) {
            Map<String,Object> map = oList.get(i);
            map.put("uid",map.get("oneheader"));
            Response serviceRegion = userRedisServiceApi.getCacheUserInfo(map);
            Map<String,Object> serviceMap = (Map<String, Object>) serviceRegion.getResponseEntity();
            if (!JzbTools.isEmpty(serviceMap)){
                map.put("saler", serviceMap.get("cname"));
            }
        }

        return oList;
    }

    /**
     * 跟进项目id获取项目信息
     * @param param
     * @return
     */
    public List<Map<String, Object>> getServiceProjectInfoByProjectid(Map<String, Object> param) {
        return tbCompanyProjectMapper.getServiceProjectInfoByProjectid(param);

    }

    public List<Map<String, Object>> getProjectidByname(Map<String, Object> param) {
        return tbCompanyProjectMapper.getProjectidByname(param);
    }

    public List<Map<String, Object>> getProjectByCname(Map<String, Object> param) {
        return tbCompanyProjectMapper.getProjectByCname(param);
    }



    public List<Map<String, Object>> getProjectByUname(Map<String, Object> param) {
        List<Map<String,Object>> list = null;
        // 根据销售员名称获取用户id
        Response response = authApi.getUidByUname(param);
        List<Map<String,Object>> uidList =  response.getPageInfo().getList();
        if (!JzbTools.isEmpty(uidList)){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < uidList.size(); i++) {
                sb.append("'"+uidList.get(i).get("uid")+"'");
                if (i != uidList.size() - 1){
                    sb.append(",");
                }
            }
            param.put("uids",sb.toString());
        }
        // 获取销售员的单位里的项目id
       list = tbCompanyProjectMapper.getProjectByUname(param);

        return list;
    }

    public List<Map<String, Object>> getProjectByCdid(Map<String, Object> param) {
        List<Map<String, Object>> cdidlist = cockpitMapper.getDeptChild(param);
        param.put("list",cdidlist);
        List<Map<String, Object>> allDeptUserUidList = cockpitMapper.getAllDeptUser(param);
        if (!JzbTools.isEmpty(allDeptUserUidList)){
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < allDeptUserUidList.size(); i++) {
                sb.append("'"+allDeptUserUidList.get(i).get("uid")+"'");
                if (i != allDeptUserUidList.size() - 1){
                    sb.append(",");
                }
            }
            param.put("uids",sb.toString());
        }
        //获取销售员的单位里的项目id
        List<Map<String,Object>> list = tbCompanyProjectMapper.getProjectByUname(param);
        return list;
    }
}
