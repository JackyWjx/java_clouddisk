package com.jzb.operate.service;

import com.jzb.operate.dao.TbTravelVerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbTravelVerifyService {

    @Autowired
    private TbTravelVerifyMapper tbTravelVerifyMapper;

    /**
     * 添加审核记录
     * @param list
     * @return
     */
    public int addTravelVerify(List<Map<String, Object>> list) {
        // 获取当前系统时间作为添加时间
        long time=System.currentTimeMillis();

        // 循环添加addtime
        for (int i = 0, l = list.size(); i < l; i++) {
            list.get(i).put("addtime",time+1);
        }

        return tbTravelVerifyMapper.addTravelVerify(list);
    }


    /**
     * 修改审核状态
     * @param param
     * @return
     */
    public int updateVerifyStatus(Map<String, Object> param){
        return tbTravelVerifyMapper.updateVerifyStatus(param);
    }
}
