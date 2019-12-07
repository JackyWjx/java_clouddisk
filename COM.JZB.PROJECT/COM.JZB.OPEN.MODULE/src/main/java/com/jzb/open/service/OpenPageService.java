package com.jzb.open.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.data.code.JzbDataCheck;
import com.jzb.base.util.JzbCheckParam;
import com.jzb.base.util.JzbRandom;
import com.jzb.open.dao.OpenPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/11/21 14:58
 */
@Service
public class OpenPageService {
    @Autowired
    private OpenPageMapper openPageMapper;

    /**
     * 模糊查询开发者应用表
     *
     * @param param
     * @return java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: DingSC
     */
    public List<Map<String, Object>> searchOrgApplication(Map<String, Object> param) {
        return openPageMapper.searchOrgApplication(param);
    }

    /**
     * 模糊查询开发者应用表总数
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int searchOrgApplicationCount(Map<String, Object> param) {
        return openPageMapper.searchOrgApplicationCount(param);
    }

    /**
     * 新增应用菜单表
     *
     * @param param
     * @return
     * @Author: DingSC
     */
    public int insertApplicationMenu(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        param.put("status", "1");
        String mid = JzbDataType.getString(param.get("appid")) + JzbRandom.getRandomNum(4);
        param.put("mid", mid);

        //提交之后添加一个状态返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("summary", "0");
        map.put("appid", param.get("appid"));
        updateOrgApplication(map);
        return openPageMapper.insertApplicationMenu(param);
    }

    /**
     * 应用页面表新增
     *
     * @param param
     * @return int
     * @Author: DingSC
     */
    public int insertApplicationPage(Map<String, Object> param) {
        param.put("time", System.currentTimeMillis());
        param.put("status", "1");
        String pageId = JzbDataType.getString(param.get("mid")) + JzbRandom.getRandomNum(2);
        param.put("pageid", pageId);

        //提交之后添加一个状态返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("summary", "0");
        map.put("appid", param.get("appid"));
        updateOrgApplication(map);
        return openPageMapper.insertApplicationPage(param);
    }

    /**
     * 应用页面的查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getApplicationMenuPage(Map<String, Object> param) {

        List<Map<String, Object>> list = openPageMapper.getApplicationMenuPage(param);
        return list;
    }

    /**
     * 菜单页面的查询
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> serachApplicationMenu(Map<String, Object> param) {
        return openPageMapper.serachApplicationMenu(param);
    }

    /**
     * 查询菜单下面对应的页面
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getApplicationPage(Map<String, Object> param) {
        return openPageMapper.getApplicationPage(param);
    }

    /**
     * 修改菜单
     *
     * @param param
     * @return
     */
    public int updateMenu(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        if (param.get("delete") != null && param.get("delete") != "" && param.get("delete").toString().length() != 0) {
            param.put("status", "2");
        }
        //提交之后添加一个状态返回给前端
        Map<String, Object> map = new HashMap<>();
        map.put("summary", "0");
        map.put("appid", param.get("appid"));
        updateOrgApplication(map);
        return openPageMapper.updateMenu(param);
    }

    /**
     * 修改页面
     *
     * @param param
     * @return
     */
    public int updatePage(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        if (param.get("delete") != null && param.get("delete") != "" && param.get("delete").toString().length() != 0) {
            param.put("status", "2");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("summary", "0");
        map.put("appid", param.get("appid"));
        updateOrgApplication(map);
        return openPageMapper.updatePage(param);
    }

    /**
     * 应用列表的修改
     * @param param
     * @return
     */
    public int updateOrgApplication(Map<String, Object> param) {
        long time = System.currentTimeMillis();
        param.put("updtime", time);
        return openPageMapper.updateOrgApplication(param);
    }



    public Map<String,Object> getOrgApplication(String appid) throws Exception {

        Map<String,Object> map = openPageMapper.getOrgApplication(appid);

        return map;
    }
}
