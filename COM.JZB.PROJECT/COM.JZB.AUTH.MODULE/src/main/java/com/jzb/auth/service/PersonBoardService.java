package com.jzb.auth.service;

import com.jzb.auth.dao.PersonBoardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2019/12/11
 * @other
 */
@Service
public class PersonBoardService {

    @Autowired
   private  PersonBoardMapper boardMapper;

    // 查询统计用户认证
    public Map<String, Object> getAuthCount(Map<String, Object> param) {
        return boardMapper.getAuthCount(param);
    }

    /**
     * 申请加入单位查询加入单位人的姓名
     * @param param
     * @return
     */
    public Map<String, Object> getCname(Map<String, Object> param) {
        return boardMapper.getCname(param);
    }
}
