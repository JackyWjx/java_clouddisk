package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface TbConsumeVerifyMapper {

    /**
     * 添加报销审核记录
     * @param list
     * @return
     */
    int addConsumeVerify(List<Map<String, Object>> list);

    /**
     * 设置审核状态
     * @param param
     * @return
     */
    int  updateVerifyStatus(Map<String, Object> param);
}
