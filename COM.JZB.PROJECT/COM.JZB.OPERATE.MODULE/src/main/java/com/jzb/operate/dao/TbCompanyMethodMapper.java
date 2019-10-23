package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbCompanyMethodMapper {

    /**
     * 添加单位方法论（修改同步）
     * @param list
     * @return
     */
    int addCompanyMethod(List<Map<String, Object>> list);
}
