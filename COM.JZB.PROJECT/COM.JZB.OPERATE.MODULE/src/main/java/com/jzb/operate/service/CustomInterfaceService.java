package com.jzb.operate.service;

import com.jzb.base.data.JzbDataType;
import com.jzb.operate.dao.CustomInterfaceMapper;
import com.jzb.operate.dao.TbUserTravelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomInterfaceService {

    @Autowired
    private CustomInterfaceMapper customInterfaceMapper;

    /**
     * 中台首页-用户自定义界面1
     * 查询自定义界面数据
     *
     * @param param
     * @return
     */
    public List<Map<String, Object>> getCustomInterface(Map<String, Object> param) {
        List<Map<String, Object>> list = customInterfaceMapper.queryCustomInterface(param);
        // 初始化数据查询
        List<Map<String, Object>> listParam = customInterfaceMapper.queryConfigPage(param);
        // 按uid查询为空则进行初始化或者查询到的数据和初始化数据不一致,即有增加项
        if (JzbDataType.isEmpty(list) || list.size() != listParam.size()) {
            long addTime = System.currentTimeMillis();
            for (int i = 0; i < listParam.size(); i++) {
                Map<String, Object> map = listParam.get(i);
                // 加入参数
                map.put("uid", param.get("uid"));
                map.put("addtime", addTime);
            }
            // 初始化用户自定义数据
            customInterfaceMapper.insertCustomInterface(listParam);
            list = customInterfaceMapper.queryCustomInterface(param);
        }
        // 初始化适配值
        int fitValue;
        List<Map<String, Object>> parentList = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            Map<String, Object> map = list.get(i);
            // 是否是数字报告
            if ("SZBGJ".equals(JzbDataType.getString(map.get("pageid")))) {
                // 迭代器
                Iterator<Map<String, Object>> it = list.iterator();
                while (it.hasNext()) {
                    Map<String, Object> sublevelMap = it.next();
                    fitValue = JzbDataType.getInteger(sublevelMap.get("fitvalue"));
                    // 小于数字报告的适配值是其子级
                    if (fitValue != 0 && fitValue < JzbDataType.getInteger(map.get("fitvalue"))) {
                        parentList.add(sublevelMap);
                        // 迭代器删除list对象
                        it.remove();
                    }
                }
                // 加入数字报告中
                map.put("subleve", parentList);
                break;
            }
        }
        return list;
    }

    /**
     * 中台首页-用户自定义界面2
     * 修改自定义界面数据
     *
     * @param param
     * @return
     */
    public int modifyCustomInterface(List<Map<String, Object>> param) {
        return customInterfaceMapper.updateCustomInterface(param);
    }
}
