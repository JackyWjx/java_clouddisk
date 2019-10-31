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

    /**
     * 点击服务跟踪记录
     * @param param
     * @return
     */
    Map<String, Object> getService(Map<String, Object> param);

    /**
     * 点击项目查询项目跟踪记录
     * @param param
     * @return
     */
    Map<String, Object> getItem(Map<String, Object> param);

}
