package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbTravelNotifyMapper {

    /**
     * 添加出差抄送记录
     * @param list
     * @return
     */
    int addTravelNotify(List<Map<String, Object>> list);
}
