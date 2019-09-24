package com.jzb.base.entity;

/**
 * @author kuangbin
 * @date 2019年8月1日
 */
public class JzbUser {
    /**
     * token属性
     */
    private String token;

    /**
     * 当前用户ID
     */
    private String uid;

    /**
     * 构造方法
     */
    public JzbUser(String uid) {
        this.uid = uid;
    }

    /**
     * 构造方法
     */
    public JzbUser(String token, String uid) {
        this.token = token;
        this.uid = uid;
    }

    /**
     * 获取Token
     * @return
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置token
     * @param token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取Uid
     * @return
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置Uid
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
