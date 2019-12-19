package com.jzb.operate.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbAsApproverMapper {

    List<Map<String, Object>> queryAsApprover(Map<String, Object> param);
}
