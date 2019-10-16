package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbProjectTypeMapper {

    /**
     * 查询公告类型
     * @return
     */
    List<Map<String, Object>> queryAllType();
}
