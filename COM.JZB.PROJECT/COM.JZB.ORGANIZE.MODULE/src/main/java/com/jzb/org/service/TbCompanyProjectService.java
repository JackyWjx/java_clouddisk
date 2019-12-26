package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.message.Response;
import com.jzb.base.util.JzbRandom;
import com.jzb.base.util.JzbTools;
import com.jzb.org.api.base.RegionBaseApi;
import com.jzb.org.api.redis.UserRedisServiceApi;
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
        List<Map<String, Object>> list = tbCompanyProjectMapper.queryServiceProjectList(param);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> uidMap = list.get(i);
            uidMap.put("uid",uidMap.get("oneheader"));
            Response region = userRedisServiceApi.getCacheUserInfo(uidMap);
            Map<String,Object> map = (Map<String, Object>) region.getResponseEntity();
            uidMap.put("saler", map.get("cname"));
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

}
