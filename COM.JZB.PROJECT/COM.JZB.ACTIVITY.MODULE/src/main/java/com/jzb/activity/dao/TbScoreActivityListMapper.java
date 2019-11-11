package com.jzb.activity.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
        *@描述
        *@创建人 chenhui
        *@创建时间 2019/11/9
        *@修改人和其它信息
*/

/**
 *
 */
@Mapper
@Repository
public interface TbScoreActivityListMapper {
    /**
     * 查询活动列表
     * @param paramap
     * @return
     */
    List<Map<String, Object>> queryActivityList(Map<String, Object> paramap);

    /**
     * 查询活动总数
     * @param paramap
     * @return
     */
    Integer getCount(Map<String, Object> paramap);
}
