package com.jzb.org.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import javax.xml.ws.ResponseWrapper;
import java.util.List;
import java.util.Map;

/**
 *
 */
@Repository
@Mapper
public interface NewTbTrackUserListMapper {

    List<Map<String, Object>> queryTrackUserListByKey(Map<String, Object> param);

    int countTrackUserListByKey(Map<String, Object> param);
}
