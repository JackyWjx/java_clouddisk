package com.jzb.base.entity.open;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/**
 * API文档类型信息类
 */
public class OpenApiType extends JzbBaseBean implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4511474704988187768L;
    /**
     * 开放类型ID
     */
    private final String FILED_APITYPE = "apitype";

    /**
     * 开放类型
     */
    private final String FILED_NAME = "name";

    /**
     * 父类型
     */
    private final String FILED_POTID = "potid";

    /**
     * 操作人
     */
    private final String FILED_OUID = "ouid";

    /**
     * 加入时间
     */
    private final String FILED_ADDTIME = "addtime";

    /**
     * 操作时间
     */
    private final String FILED_UPDTIME = "updtime";

    /**
     * 状态
     */
    private final String FILED_STATUS = "status";

    /**
     * 备注
     */
    private final String FILED_SUMMARY = "summary";

    /**
     * 设置开放类型ID
     * @param apitype
     */
    public void setApitype(String apitype) {
        setItem(FILED_APITYPE, apitype);
    } // End setapitype

    /**
     * 获取开放类型ID
     * @return
     */
    public String getApitype() {
        return getItemApp(FILED_APITYPE).getString();
    } // End getOtid

    /**
     * 设置开放类型
     * @param name
     */
    public void setName(String name) {
        setItem(FILED_NAME, name);
    } // End setname

    /**
     * 获取开放类型
     * @return
     */
    public String getName() {
        return getItemApp(FILED_NAME).getString();
    } // End getname

    /**
     * 设置父类型
     * @param potid
     */
    public void setPotid(String potid) {
        setItem(FILED_POTID,potid);
    } // End setpotid

    /**
     * 返回父类型
     * @return
     */
    public String getPotid() {
        return getItemApp(FILED_POTID).getString();
    } // End getpotid

    /**
     * 设置操作人
     * @param ouid
     */
    public void setOuid(String ouid) {
        setItem(FILED_OUID, ouid);
    } // End setouid

    /**
     * 获取操作人
     * @return
     */
    public String getOuid() {
        return getItemApp(FILED_OUID).getString();
    } // End getouid

    /**
     * 设置加入时间
     * @param addtime
     */
    public void setAddtime(String addtime) {
        setItem(FILED_ADDTIME, addtime);
    } // End setaddtime

    /**
     * 获取加入时间
     * @return
     */
    public String getAddtime() {
        return getItemApp(FILED_ADDTIME).getString();
    } // End getaddtime

    /**
     * 设置操作时间
     * @param updtime
     */
    public void setUpdtime(String updtime) {
        setItem(FILED_UPDTIME, updtime);
    } // End setupdtime

    /**
     * 获取操作时间
     * @return
     */
    public String getUpdtime() {
        return getItemApp(FILED_UPDTIME).getString();
    } // End getupdtime

    /**
     * 设置状态
     * @param status
     */
    public void setStatus(String status) {
        setItem(FILED_STATUS, status);
    } // End setstatus

    /**
     * 获取状态
     * @return
     */
    public String getStatus() {
        return getItemApp(FILED_STATUS).getString();
    } // End getstatus

    /**
     * 设置状态
     * @param summary
     */
    public void setSummary(String summary) {
        setItem(FILED_SUMMARY, summary);
    } // End setsummary

    /**
     * 获取状态
     * @return
     */
    public String getSummary() {
        return getItemApp(FILED_SUMMARY).getString();
    } // End getsummary
} // End class UserInfo
