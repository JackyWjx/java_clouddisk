package com.jzb.media.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 媒体DB
 */
@Mapper
@Repository
public interface MediaMapper {

    /**
     * 添加文件记录
     */
    int  insertMeduia(Map<String,Object> map)throws IOException;

    /**
     * 查询
     */
    List<Map<String , Object>> queryMedia(Map<String , Object> map);

}
