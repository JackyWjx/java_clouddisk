package com.jzb.auth.dao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author: gongWei
 * @Date: Greated in
 * @Description:
 * @version: 0.0.1
 */
@Repository
public interface UserInfoMapper {

    String getUsernameByUids(Map<String, Object> whereMap);
}
