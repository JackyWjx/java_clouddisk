package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.*;

@Mapper
@Repository
public interface TbTrackUserListMapper {


    /**
     * 查询所有跟进记录
     * @return
     */
    List<Map<String, Object>> findTrackList(Map<String, Object> param);

    /**
     * 查询所有跟进记录总数
     * @return
     */
    int findTrackListCount();

    /**
     * 查询所有跟进记录带条件
     * @param param
     * @return
     */
    List<Map<String, Object>> findTrackListByKeywords(Map<String, Object> param);


    /**
     * 查询所有跟进记录总数带条件
     * @param param
     * @return
     */
    int findTrackListCountByKeywords(Map<String, Object> param);

    /**
     * 根据跟用户姓名查询用户信息
     * @param param
     * @return
     */
    List<Map<String, Object>> findUnameLike(Map<String, Object> param);

    /**
     * 根据单位名称查询单位信息
     * @param param
     * @return
     */
    List<Map<String, Object>> findCnameLike(Map<String, Object> param);
}
