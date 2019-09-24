package com.jzb.base.entity.organize;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/**
 * 用户实体信息类
 */
public class DeptUser extends JzbBaseBean implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -6556872876024447257L;

    /**
     * 用户ID
     */
    private final String FIELD_UID = "uid";

    /**
     * 用户姓名
     */
    private final String FILED_UNAME = "uname";

    /**
     * 注册电话
     */
    private final String FILED_RELPHONE = "relphone";

    /**
     * 部门名称
     */
    private final String FILED_DNAME = "dname";

    /**
     * 用户头像
     */
    private final String FILED_PORTRAIT = "portrait";

    /**
     * 设置用户ID
     * @param uid
     */
    public void setUid(String uid) {
        setItem(FIELD_UID, uid);
    } // End setUserID

    /**
     * 获取用户ID
     * @return
     */
    public String getUid() {
        return getItemApp(FIELD_UID).getString();
    } // End getUserId

    /**
     * 设置用户姓名
     * @param uname
     */
    public void setUname(String uname) {
        setItem(FILED_UNAME, uname);
    } // End setuname

    /**
     * 获取用户姓名
     * @return
     */
    public String getUname() {
        return getItemApp(FILED_UNAME).getString();
    } // End getuname

    /**
     * 设置注册电话
     * @param phone
     */
    public void setRelphone(String phone) {
        setItem(FILED_RELPHONE, phone);
    } // End setRegPhone

    /**
     * 返回注册电话
     * @return
     */
    public String getRelphone() {
        return getItemApp(FILED_RELPHONE).getString();
    } // End getRegPhone

    /**
     * 设置部门名称
     * @param dname
     */
    public void setDname(String dname) {
        setItem(FILED_DNAME, dname);
    } // End setDname

    /**
     * 获取部门名称
     * @return
     */
    public String getDname() {
        return getItemApp(FILED_DNAME).getString();
    } // End getDname

    /**
     * 设置用户头像
     * @param portrait
     */
    public void setPortrait(String portrait) {
        setItem(FILED_PORTRAIT, portrait);
    } // End setDname

    /**
     * 获取用户头像
     * @return
     */
    public String getPortrait() {
        return getItemApp(FILED_PORTRAIT).getString();
    } // End getportrait
} // End class UserInfo
