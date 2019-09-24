package com.jzb.base.entity.auth;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/**
 * 企业实体信息类
 */
public class CompanyInfo extends JzbBaseBean implements Serializable {
    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 2015826680460808622L;

    /**
     * 企业ID
     */
    private final String FIELD_CID = "cid";

    /**
     * 企业名称
     */
    private final String FIELD_CNAME = "cname";

    /**
     * 企业别名
     */
    private final String FIELD_NICKNAME = "nickname";

    /**
     * 地区ID
     */
    private final String FIELD_REGION = "region";

    /**
     * 地区名
     */
    private final String FIELD_REGIONJSON = "regionjson";

    /**
     * 统一代码
     */
    private final String FIELD_USCC = "uscc";

    /**
     * 联系人
     */
    private final String FIELD_RELPERSON = "relperson";

    /**
     * 联系电话
     */
    private final String FIELD_RELPHONE = "relphone";

    /**
     * 管理人ID
     */
    private final String FIELD_MANAGEID = "manageid";

     /**
     * 管理人姓名
     */
    private final String FIELD_MANAGER = "manager";

     /**
     * 企业状态
     */
    private final String FIELD_STATUS = "status";

     /**
     * 排序
     */
    private final String FIELD_IDX = "idx";

     /**
     * 创建人
     */
    private final String FIELD_REGUID = "reguid";

     /**
     * 创建时间
     */
    private final String FIELD_REGTIME = "regtime";

    /**
     * 修改时间
     */
    private final String FIELD_MODTIME = "modtime";

    /**
     * 备注
     */
    private final String FIELD_SUMMARY = "summary";

    /**
     * 用户姓名
     */
    private final String FIELD_UNAME = "uname";

    /**
     * 设置用户姓名
     * @param uname
     */
    public void setUname(String uname) {
        setItem(FIELD_UNAME, uname);
    } // End setUname

    /**
     * 获取用户姓名
     * @return
     */
    public String getUname() {
        return getItemApp(FIELD_UNAME).getString();
    } // End getUname

    /**
     * 设置备注
     * @param summary
     */
    public void setSummary(String summary) {
        setItem(FIELD_SUMMARY, summary);
    } // End setSummary

    /**
     * 获取备注
     * @return
     */
    public String getSummary() {
        return getItemApp(FIELD_SUMMARY).getString();
    } // End getSummary

    /**
     * 设置创建时间
     * @param modtime
     */
    public void setModtime(String modtime) {
        setItem(FIELD_MODTIME, modtime);
    } // End setModtime

    /**
     * 获取创建时间
     * @return
     */
    public Long getModtime() {
        return getItemApp(FIELD_MODTIME).getLong();
    } // End getModtime

    /**
     * 设置创建时间
     * @param regtime
     */
    public void setRegtime(String regtime) {
        setItem(FIELD_REGTIME, regtime);
    } // End setRegtime

    /**
     * 获取创建时间
     * @return
     */
    public Long getRegtime() {
        return getItemApp(FIELD_REGTIME).getLong();
    } // End getRegtime

    /**
     * 设置创建人
     * @param reguid
     */
    public void setReguid(String reguid) {
        setItem(FIELD_REGUID, reguid);
    } // End setReguid

    /**
     * 获取创建人
     * @return
     */
    public String getReguid() {
        return getItemApp(FIELD_REGUID).getString();
    } // End getReguid

    /**
     * 设置排序
     * @param idx
     */
    public void setIdx(String idx) {
        setItem(FIELD_IDX, idx);
    } // End setIdx

    /**
     * 获取排序
     * @return
     */
    public Integer getIdx() {
        return getItemApp(FIELD_IDX).getInteger();
    } // End getIdx

    /**
     * 设置企业ID
     * @param cid
     */
    public void setCid(String cid) {
        setItem(FIELD_CID, cid);
    } // End setCompanyId

    /**
     * 获取企业ID
     * @return
     */
    public String getCid() {
        return getItemApp(FIELD_CID).getString();
    } // End getCompanyId

    /**
     * 设置企业名称
     * @param cname
     */
    public void setCname(String cname) {
        setItem(FIELD_CNAME, cname);
    } // End setCompanyCname

    /**
     * 获取企业名称
     * @return
     */
    public String getCname() {
        return getItemApp(FIELD_CNAME).getString();
    } // End getCname

    /**
     * 设置企业别名
     * @param nickname
     */
    public void setNickname(String nickname) {
        setItem(FIELD_NICKNAME, nickname);
    } // End setNickname

    /**
     * 返回企业别名
     * @return
     */
    public String getNickname() {
        return getItemApp(FIELD_NICKNAME).getString();
    } // End getNickname

    /**
     * 设置地区id
     * @param region
     */
    public void setRegion(String region) {
        setItem(FIELD_REGION,region);
    } // End setRegion

    /**
     * 获取地区id
     * @return
     */
    public String getRegion() {
        return getItemApp(FIELD_REGION).getString();
    } // End getRegion

    /**
     * 设置地区名
     * @param regionjson
     */
    public void setRegionjson(String regionjson) {
        setItem(FIELD_REGIONJSON,regionjson);
    } // End setRegionjson

    /**
     * 获取地区名
     * @return
     */
    public String getRegionjson() {
        return getItemApp(FIELD_REGIONJSON).getString();
    } // End getRegionjson

    /**
     * 设置地区名
     * @param uscc
     */
    public void setUscc(String uscc) {
        setItem(FIELD_USCC,uscc);
    } // End setUscc

    /**
     * 获取地区名
     * @return
     */
    public String getUscc() {
        return getItemApp(FIELD_USCC).getString();
    } // End getUscc

    /**
     * 设置联系人
     * @param relperson
     */
    public void setRelperson(String relperson) {
        setItem(FIELD_RELPERSON,relperson);
    } // End setRelperson

    /**
     * 获取联系人
     * @return
     */
    public String getRelperson() {
        return getItemApp(FIELD_RELPERSON).getString();
    } // End getRelperson

    /**
     * 设置联系电话
     * @param relphone
     */
    public void setRelphone(String relphone) {
        setItem(FIELD_RELPHONE,relphone);
    } // End setRelphone

    /**
     * 获取联系电话
     * @return
     */
    public String getRelphone() {
        return getItemApp(FIELD_RELPHONE).getString();
    } // End getRelphone

    /**
     * 设置管理人ID
     * @param manageid
     */
    public void setManageId(String manageid) {
        setItem(FIELD_MANAGEID,manageid);
    } // End setManageid

    /**
     * 获取管理人ID
     * @return
     */
    public String getManageId() {
        return getItemApp(FIELD_MANAGEID).getString();
    } // End getManageid

    /**
     * 设置管理人姓名
     * @param manager
     */
    public void setManager(String manager) {
        setItem(FIELD_MANAGER,manager);
    } // End setManager

    /**
     * 获取管理人姓名
     * @return
     */
    public String getManager() {
        return getItemApp(FIELD_MANAGER).getString();
    } // End getManager

    /**
     * 设置企业状态
     * @param status
     */
    public void setStatus(String status) {
        setItem(FIELD_STATUS,status);
    } // End setStatus

    /**
     * 获取企业状态
     * @return
     */
    public String getStatus() {
        return getItemApp(FIELD_STATUS).getString();
    } // End getStatus

} // End class Region
