package com.jzb.auth.service;

import java.util.Map;

/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/11 9:54
 *  @Description:
 */
public interface UserInfoService {


    /**
     * 根据用户id获取用户名称(含多个)
     * @param param
     * @return
     */
    String getUsernameByUids(Map<String, Object> param);


}
