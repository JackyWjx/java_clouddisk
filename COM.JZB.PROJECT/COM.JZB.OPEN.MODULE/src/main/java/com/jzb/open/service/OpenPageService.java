package com.jzb.open.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.base.util.JzbRandom;
import com.jzb.open.dao.OpenPageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return openPageMapper.insertApplicationPage(param);
    }

    public List<Map<String, Object>> getApplicationMenuPage(Map<String, Object> param){
        return openPageMapper.getApplicationMenuPage(param);
    }
}
