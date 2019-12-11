package com.jzb.org.dao;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbDeptUserMapper {

    List<Map<String, Object>> queryUsernameBydept(Map<String, Object> map);

    List<Map<String, Object>> queryOtherPersonByuid(Map<String, Object> param);
}
