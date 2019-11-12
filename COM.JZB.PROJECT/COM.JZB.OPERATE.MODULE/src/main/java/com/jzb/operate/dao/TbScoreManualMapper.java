package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @描述
 * @创建人 chenhui
 * @创建时间 2019/11/9
 * @修改人和其它信息
 */
@Mapper
@Repository
public interface TbScoreManualMapper {
    //查询指导手册总数
    Integer getCount(Map<String, Object> paramap);

    //查询指导手册列表
    List<Map<String, Object>> queryScoreManualList(Map<String, Object> paramap);
}
