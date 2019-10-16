package com.jzb.base.entity.organize;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

public class RoleGroup extends JzbBaseBean implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 6661521789455465823L;

    /**
     * 角色组ID
     */
    private final String FILED_CRGID = "crgid";

    /**
     * 角色组名
     */
    private final String FILED_CNAME = "cname";

    /**
     * 设置角色组ID
     * @param crgid
     */
    public void setCrgid(String crgid) {
        setItem(FILED_CRGID, crgid);
    } // End setCrgid

    /**
     * 获取角色组ID
     * @return
     */
    public String getCrgid() {
        return getItemApp(FILED_CRGID).getString();
    } // End getCrgid

    /**
     * 设置角色组名
     * @param cname
     */
    public void setCname(String cname) {
        setItem(FILED_CNAME, cname);
    } // End setCrgid

    /**
     * 获取角色组名
     * @return
     */
    public String getCname() {
        return getItemApp(FILED_CNAME).getString();
    } // End getCrgid

}
