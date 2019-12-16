package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface TbAppVersionListMapper {


    /** 查询最新app版本 */
    Map<String, Object> queryAppVersion();

}
