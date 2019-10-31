package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbHandleItemMapper {

    /**
     * 跟进人详情查询
     * @param param
     * @return
     */
    Map<String, Object> getHandleItem(Map<String, Object> param);
}
