package com.jzb.resource.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbPolicyTypeMapper {



    /**
     *     查询政策父子级
     * @param param
     * @return
     */
     List<Map<String, Object>> queryPolicyType(Map<String, Object> param);
}


