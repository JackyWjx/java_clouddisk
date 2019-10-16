package com.jzb.base.entity.auth;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/***
 *
 *@Author
 *@Data 2019/7/26   14:29
 *@Describe  成员申请/邀请表
 *
 */
public class InviteUser extends JzbBaseBean implements Serializable {
    /**
     * 主键
     */
    private final String INVITE_ID = "id";
    /**
     * 请求类型
     */
    private final String INVITE_REQTYPE = "reqtype";
    /**
     * 批次ID
     */
    private final String INVITE_BATCHID = "batchid";
    /**
     * 邀请人
     */
    private final String INVITE_UID = "uid";
    /**
     * 邀请时间
     */
    private final String INVITE_REQTIME = "reqtime";
    /**
     * 部门
     */
    private final String INVITE_CDID = "cdic";
    /**
     * 被邀人状态
     */
    private final String INVITE_USTATUS = "ustatus";
    /**
     * 响应处理人
     */
    private final String INVITE_RESUID = "resuid";
    /**
     * 电话
     */
    private final String INVITE_RESPHONE = "resphone";
    /**
     * 接受时间
     */
    private final String INVITE_RESTIME = "restime";
    /**
     * 状态
     */
    private final String INVITE_STATUS = "status";
    /**
     * 备注
     */
    private final String INVITE_SUMMARY = "summary";

    /**
     * 设置ID
     * @param
     */
    public void setID(String id) {
        setItem(INVITE_ID, id);
    }

    /**
     * 获取ID
     * @return
     */
    public String getID() {
        return getItemApp(INVITE_ID).getString();
    }

    /**
     * 设置请求类型
     * @param
     */
    public void setReqType(String reqType) {
        setItem(INVITE_REQTYPE, reqType);
    }

    /**
     * 获取请求类型
     * @return
     */
    public String getReqType() {
        return getItemApp(INVITE_REQTYPE).getString();
    }
    /**
     * 设置批次ID
     * @param
     */
    public void setBatchId(String batchId) {
        setItem(INVITE_BATCHID, batchId);
    }
    /**
     * 获取批次ID
     * @return
     */
    public String getBatchId() {
        return getItemApp(INVITE_BATCHID).getString();
    }
    /**
     * 设置邀请人
     * @param
     */
    public void setUID(String uid) {
        setItem(INVITE_UID, uid);
    }

    /**
     * 获取邀请人
     * @return
     */
    public String getUID() {
        return getItemApp(INVITE_UID).getString();
    }

    /**
     * 设置邀请时间
     * @param
     */
    public void setReqTime(String reqTime) {
        setItem(INVITE_REQTIME, reqTime);
    }

    /**
     * 获取邀请时间
     * @return
     */
    public String getReqTime() {
        return getItemApp(INVITE_REQTIME).getString();
    }

    /**
     * 设置部门
     * @param
     */
    public void setCDID(String cdid) {
        setItem(INVITE_CDID, cdid);
    }

    /**
     * 获取部门
     * @return
     */
    public String getCDID() {
        return getItemApp(INVITE_CDID).getString();
    }
    /**
     * 设置被邀人状态
     * @param
     */
    public void setUstatus(String ustatus) {
        setItem(INVITE_USTATUS, ustatus);
    }
    /**
     * 获取被邀人状态
     * @return
     */
    public String getUstatus() {
        return getItemApp(INVITE_USTATUS).getString();
    }
    /**
     * 设置响应处理人
     * @param
     */
    public void setResuId(String resuId) {
        setItem(INVITE_RESUID, resuId);
    }

    /**
     * 获取响应处理人
     * @return
     */
    public String getResuId() {
        return getItemApp(INVITE_RESUID).getString();
    }

    /**
     * 设置电话
     * @param
     */
    public void setResPhone(String resPhone) {
        setItem(INVITE_RESPHONE, resPhone);
    }

    /**
     * 获取电话
     * @return
     */
    public String getResPhone() {
        return getItemApp(INVITE_RESPHONE).getString();
    }

    /**
     * 设置接受时间
     * @param
     */
    public void setResTime(String resTime) {
        setItem(INVITE_RESTIME, resTime);
    }

    /**
     * 获取接受时间
     * @return
     */
    public String getResTime() {
        return getItemApp(INVITE_RESTIME).getString();
    }
    /**
     * 设置状态
     * @param
     */
    public void setStatus(String status) {
        setItem(INVITE_STATUS, status);
    }
    /**
     * 获取状态
     * @return
     */
    public String getStatus() {
        return getItemApp(INVITE_STATUS).getString();
    }
    /**
     * 设置备注
     * @param
     */
    public void setSummary(String summary) {
        setItem(INVITE_SUMMARY, summary);
    }

    /**
     * 获取备注
     * @return
     */
    public String getSummary() {
        return getItemApp(INVITE_SUMMARY).getString();
    }
}
