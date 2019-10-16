package com.jzb.base.entity.auth;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.jzb.base.entity.JzbBaseBean;

/**
 * 用户实体信息类
 */
public class UserInfo extends JzbBaseBean implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -2054876988549619823L;

    /**
     * 用户ID
     */
    private final String FIELD_UID = "uid";

    /**
     * 注册ID
     */
    private final String FILED_REGID = "regid";

    /**
     * 注册电话
     */
    private final String FILED_REGPHONE = "regphone";

    /**
     * 注册邮箱
     */
    private final String FILED_REGMAIL = "regmail";

    /**
     * 设置用户ID
     * @param uid
     */
    public void setUserId(String uid) {
        setItem(FIELD_UID, uid);
    } // End setUserID

    /**
     * 获取用户ID
     * @return
     */
    public String getUserId() {
        return getItemApp(FIELD_UID).getString();
    } // End getUserId

    /**
     * 设置注册ID
     * @param rid
     */
    public void setRegID(String rid) {
        setItem(FILED_REGID, rid);
    } // End setRegID

    /**
     * 获取注册ID
     * @return
     */
    public String getRegID() {
        return getItemApp(FILED_REGID).getString();
    } // End getRegID

    /**
     * 设置注册电话
     * @param phone
     */
    public void setRegPhone(String phone) {
        setItem(FILED_REGPHONE, phone);
    } // End setRegPhone

    /**
     * 返回注册电话
     * @return
     */
    public String getRegPhone() {
        return getItemApp(FILED_REGPHONE).getString();
    } // End getRegPhone

    /**
     * 设置注册邮箱
     * @param mail
     */
    public void setRegMail(String mail) {
        setItem(FILED_REGMAIL, mail);
    } // End setRegMail

    /**
     * 获取注册邮箱
     * @return
     */
    public String getRegMail() {
        return getItemApp(FILED_REGMAIL).getString();
    } // End getRegMail
} // End class UserInfo
