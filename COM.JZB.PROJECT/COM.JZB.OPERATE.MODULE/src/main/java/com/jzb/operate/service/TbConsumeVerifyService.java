package com.jzb.operate.service;

import com.jzb.operate.dao.TbConsumeVerifyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TbConsumeVerifyService {

    @Autowired
    private TbConsumeVerifyMapper tbConsumeVerifyMapper;

    /**
     * 添加报销审核记录
     * @param list
     * @return
     */
    public int addConsumeVerify(List<Map<String, Object>> list){
        return tbConsumeVerifyMapper.addConsumeVerify(list);
    }

    /**
     * 设置审核状态
     * @param param
     * @return
     */
    public int updateVerifyStatus(Map<String, Object> param){
        return tbConsumeVerifyMapper.updateVerifyStatus(param);
    }
}
