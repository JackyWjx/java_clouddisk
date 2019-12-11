package com.jzb.auth.dao;

import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author chenhui
 * @description
 * @time 2019/12/11
 * @other
 */
@Repository
public interface PersonBoardMapper {
    Map<String, Object> getAuthCount(Map<String, Object> param);
}
