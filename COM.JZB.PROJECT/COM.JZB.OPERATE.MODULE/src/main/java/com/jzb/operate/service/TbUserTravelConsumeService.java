package com.jzb.operate.service;

import com.jzb.base.message.Response;
import com.jzb.operate.dao.TbUserTravelConsumeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbUserTravelConsumeService {

    @Autowired
    private TbUserTravelConsumeMapper tbUserTravelConsumeMapper;



    /**
     * 添加用户出差报销记录
     * @param list
     * @return
     */
    public int addUserTravelConsume(List<Map<String, Object>> list){
        return tbUserTravelConsumeMapper.addUserTravelConsume(list);
    }


    /**
     * 修改用户出差报销记录
     * @param list
     * @return
     */
    public int updateUserTravelConsume(List<Map<String, Object>> list){
        return tbUserTravelConsumeMapper.updateUserTravelConsume(list);
    }


    /**
     * 查询用户出差报销记录
     * @param param
     * @return
     */
    public List<Map<String, Object>> quertUserTravelComsume(Map<String, Object> param){
        return tbUserTravelConsumeMapper.quertUserTravelComsume(param);
    }
}
