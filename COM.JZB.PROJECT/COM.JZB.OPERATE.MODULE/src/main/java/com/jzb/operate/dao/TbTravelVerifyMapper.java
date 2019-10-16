package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbTravelVerifyMapper {

    /**
     * 添加出差审核记录
     * @param list
     * @return
     */
    int addTravelVerify(List<Map<String, Object>> list);

    /**
     * 修改审核状态
     * @param param
     * @return
     */
    int updateVerifyStatus(Map<String, Object> param);
}
