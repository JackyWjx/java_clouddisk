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
    List<Map<String, Object>> getHandleItem(Map<String, Object> param);


    /**
     * 所有业主-销售统计分许-根据页面的超链接进行查询
     * @param param
     * @return
     */
    List<Map<String, Object>> queryHandleItem(Map<String, Object> param);
}
