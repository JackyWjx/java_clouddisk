package com.jzb.auth.dao;

import org.apache.commons.math3.optim.nonlinear.scalar.LineSearch;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TbUserControlAuthMapper {

    /**
     * 给单个用户授权
     * @param list
     * @return
     */
    int addUserControlAuth(List<Map<String, Object>> list);
}
