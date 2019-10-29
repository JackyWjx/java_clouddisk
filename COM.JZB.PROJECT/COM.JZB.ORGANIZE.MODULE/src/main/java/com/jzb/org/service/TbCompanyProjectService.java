package com.jzb.org.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.org.dao.TbCompanyProjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class TbCompanyProjectService {
    @Autowired
    private TbCompanyProjectMapper tbCompanyProjectMapper;

    /**
     * 查询项目中的总数
     * @param param
     * @return
     */
    public int getCount(Map<String, Object> param) {
    return tbCompanyProjectMapper.getCount(param);
    }

    /**
     * 销售业主-公海-项目-数据查询和根据名称模糊查询
     * @param param
     * @return
     */
    public List<Map<String, Object>> getComProject(Map<String, Object> param) {
        return tbCompanyProjectMapper.getComProject(param);
    }

    /**
     * 项目的添加
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
        return  tbCompanyProjectMapper.saveComProject(param);
    }

    /**
     * 关联业主  根据项目id进行项目表的修改
     * @param paramList
     * @return
     */
    public int updateComProject(List<Map<String, Object>> paramList) {
        long time = System.currentTimeMillis();
        for (int i = 0; i < paramList.size(); i++) {
            paramList.get(i).put("updtime", time);
        }

        return  tbCompanyProjectMapper.updateComProject(paramList);
    }

    /**
     * 取消业主关联 根据项目id进行修改
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
}
