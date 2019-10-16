package com.jzb.base.entity.auth;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/***
 *
 *@Author
 *@Data 2019/7/26   10:14
 *@Describe 企业产品表
 *
 */
public class CompanyProduct extends JzbBaseBean implements Serializable {

    /**
     * 主键
     */
    private final String COMPANY_ID = "id";
    /**
     * 企业ID
     */
    private final String COMPANY_CID = "cid";
    /**
     * 产品ID
     */
    private final String COMPANY_PID = "pid";
    /**
     * 用户端URL
     */
    private final String COMPANY_WEBURL= "weburl";
    /**
     * 管理端URL
     */
    private final String COMPANY_MANURL  = "manurl";
    /**
     * 加入人
     */
    private final String COMPANY_OUID = "ouid";
    /**
     * 有效期
     */
    private final String COMPANY_LIMITDAY = "limitday";
    /**
     * 加入时间
     */
    private final String COMPANY_ADDTIME = "addtime";
    /**
     * 更新时间
     */
    private final String COMPANY_UPDTIME = "updtime";
    /**
     * 状态
     */
    private final String COMPANY_STATUS = "status";
    /**
     * 状态
     */
    private final String COMPANY_SUMMARY  = "summary";


    /**
     * 设置ID
     * @param
     */
    public void setID(String id) {
        setItem(COMPANY_ID, id);
    }

    /**
     * 获取ID
     * @return
     */
    public String getID() {
        return getItemApp(COMPANY_ID).getString();
    }

    /**
     * 设置企业ID
     * @param
     */
    public void setCID(String cid) {
        setItem(COMPANY_CID, cid);
    }

    /**
     * 获取企业ID
     * @return
     */
    public String getCID() {
        return getItemApp(COMPANY_CID).getString();
    }

    /**
     * 设置产品ID
     * @param
     */
    public void setPID(String pid) {
        setItem(COMPANY_PID, pid);
    }

    /**
     * 获取产品ID
     * @return
     */
    public String getPID() {
        return getItemApp(COMPANY_PID).getString();
    }

    /**
     * 设置用户端URL
     * @param
     */
    public void setWebUrl(String webUrl) {
        setItem(COMPANY_WEBURL, webUrl);
    }

    /**
     * 获取用户端URL
     * @return
     */
    public String getWebUrl() {
        return getItemApp(COMPANY_WEBURL).getString();
    }

    /**
     * 设置用户端URL
     * @param
     */
    public void setManUrl(String manUrl) {
        setItem(COMPANY_MANURL, manUrl);
    }

    /**
     * 获取用户端URL
     * @return
     */
    public String getManUrl() {
        return getItemApp(COMPANY_MANURL).getString();
    }

    /**
     * 设置加入人
     * @param
     */
    public void setOUID(String ouid) {
        setItem(COMPANY_OUID, ouid);
    }

    /**
     * 获取加入人
     * @return
     */
    public String getOUID() {
        return getItemApp(COMPANY_OUID).getString();
    }

    /**
     * 设置有效期
     * @param
     */
    public void setLimitday(String limitday) {
        setItem(COMPANY_LIMITDAY, limitday);
    }

    /**
     * 获取有效期
     * @return
     */
    public String getLimitday() {
        return getItemApp(COMPANY_LIMITDAY).getString();
    }

    /**
     * 设置加入时间
     * @param
     */
    public void setAddTime(String addTime) {
        setItem(COMPANY_ADDTIME, addTime);
    }

    /**
     * 获取加入时间
     * @return
     */
    public String getAddTime() {
        return getItemApp(COMPANY_ADDTIME).getString();
    }

    /**
     * 设置更新时间
     * @param
     */
    public void setUpdTime(String updTime) {
        setItem(COMPANY_UPDTIME, updTime);
    }

    /**
     * 获取更新时间
     * @return
     */
    public String getUpdTime() {
        return getItemApp(COMPANY_UPDTIME).getString();
    }

    /**
     * 设置状态
     * @param
     */
    public void setStatus(String status) {
        setItem(COMPANY_STATUS, status);
    }

    /**
     * 获取状态
     * @return
     */
    public String getStatus() {
        return getItemApp(COMPANY_STATUS).getString();
    }

    /**
     * 设置备注
     * @param
     */
    public void setSummary(String summary) {
        setItem(COMPANY_SUMMARY, summary);
    }

    /**
     * 获取备注
     * @return
     */
    public String getSummary() {
        return getItemApp(COMPANY_SUMMARY).getString();
    }


}
