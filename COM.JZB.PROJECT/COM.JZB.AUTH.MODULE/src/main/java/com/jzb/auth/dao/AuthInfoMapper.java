package com.jzb.auth.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface AuthInfoMapper {

    Map<String, Object> getUserByUids(Map<String, Object> whereMap);

    Map<String, Object> queryUserInfoByUid(Map<String, Object> param);
}
