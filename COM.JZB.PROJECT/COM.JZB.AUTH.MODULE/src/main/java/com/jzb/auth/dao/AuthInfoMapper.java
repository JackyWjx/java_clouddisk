package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface AuthInfoMapper {

    String getUserByUids(Map<String, Object> whereMap);
}
