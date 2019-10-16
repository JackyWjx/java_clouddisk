package com.jzb.base.entity;

import com.jzb.base.entity.auth.UserInfo;

/**
 * 针对User进行封装
 * @author kuangbin
 * @date 2019年8月1日
 */
public class JzbUserBean {
    /**
     * 数据对象
     */
    private UserInfo userInfo;

    /**
     * 默认构造函数，创建一个Map存储容器对象
     */
    private JzbUserBean() {
        userInfo = new UserInfo();
    } // End JzbUserBean

    public static final JzbUserBean getTokenUser(){
        return new JzbUserBean();
    }

    /**
     * 获取UserInfo对象
     * @return
     */
    public UserInfo getUserInfo() {
        return userInfo;
    }

    /**
     * 设置UserInfo对象
     * @param userInfo
     */
    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
