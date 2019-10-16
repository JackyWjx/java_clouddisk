package com.jzb.base.entity.auth;

import com.jzb.base.entity.JzbBaseBean;

import java.io.Serializable;

/***
 *
 *@Author
 *@Data 2019/7/26   9:14
 *@Describe  企业表
 *
 */
public class CompanyList extends JzbBaseBean implements Serializable {
    /**
     * 主键
     */
    private final String COMPANY_ID = "id";
    /**
     * 企业ID
     */
    private final String COMPANY_CID = "cid";
    /**
     * 企业名称
     */
    private final String COMPANY_CNAME = "cname";
    /**
     * 别名
     */
    private final String COMPANY_NICKNAME = "nickname";
    /**
     * 地区ID
     */
    private final String COMPANY_REGION  = "region";
    /**
     * 统一代码
     */
    private final String COMPANY_USCC = "uscc";
    /**
     * 排序
     */
    private final String COMPANY_IDX = "idx";
    /**
     * 联系人
     */
    private final String COMPANY_RELPERSON = "relperson";
     /**
     * 联系电话
     */
    private final String COMPANY_RELPHONE = "relphone";
    /**
     * 联系邮箱
     */
    private final String COMPANY_RELMAIL = "relmail";
    /**
     * 创建人
     */
    private final String COMPANY_REGUID = "reguid";
    /**
     * 创建时间
     */
    private final String COMPANY_REGTIMR = "regtime";
    /**
     * 管理员
     */
    private final String COMPANY_MANAGER = "manager";
    /**
     * LOGO
     */
    private final String COMPANY_LOGO = "logo";
    /**
     * 单位图片
     */
    private final String COMPANY_PHOTO = "photo";
    /**
     * 修改时间
     */
    private final String COMPANY_MODTIME = "modtime";
    /**
     * 认证类型
     */
    private final String COMPANY_AUTHID = "authid";
    /**
     * 企业状态
     */
    private final String COMPANY_STATUS = "status";
    /**
     * 备注
     */
    private final String COMPANY_SUMMARY = "summary";
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
     * 设置企业名称
     * @param
     */
    public void setCName(String cname) {
        setItem(COMPANY_CNAME, cname);
    }
    /**
     * 获取企业名称
     * @return
     */
    public String getCName() {
        return getItemApp(COMPANY_CNAME).getString();
    }
    /**
     * 设置别名
     * @param
     */
    public void setNickName(String nickName) {
        setItem(COMPANY_NICKNAME, nickName);
    }

    /**
     * 获取别名
     * @return
     */
    public String getNickName() {
        return getItemApp(COMPANY_NICKNAME).getString();
    }
    /**
     * 设置地区ID
     * @param
     */
    public void setNegion(String negion) {
        setItem(COMPANY_REGION, negion);
    } // End setUserID

    /**
     * 获取地区ID
     * @return
     */
    public String getNegion() {
        return getItemApp(COMPANY_REGION).getString();
    }
    /**
     * 设置统一代码
     * @param
     */
    public void setUscc(String uscc) {
        setItem(COMPANY_USCC, uscc);
    }

    /**
     * 获取统一代码
     * @return
     */
    public String getUscc() {
        return getItemApp(COMPANY_USCC).getString();
    }
    /**
     * 设置排序
     * @param
     */
    public void setIdx(String idx) {
        setItem(COMPANY_IDX, idx);
    }

    /**
     * 获取排序
     * @return
     */
    public String getidx() {
        return getItemApp(COMPANY_IDX).getString();
    }
     /**
     * 设置联系人
     * @param
     */
    public void setRelPerson(String relPerson) {
        setItem(COMPANY_RELPERSON, relPerson);
    }

    /**
     * 获取联系人
     * @return
     */
    public String getRelPerson() {
        return getItemApp(COMPANY_RELPERSON).getString();
    }
     /**
     * 设置联系电话
     * @param
     */
    public void setRelPhone(String relPhone) {
        setItem(COMPANY_RELPHONE, relPhone);
    }

    /**
     * 获取联系电话
     * @return
     */
    public String getRelPhone() {
        return getItemApp(COMPANY_RELPHONE).getString();
    }
    /**
     * 设置联系邮箱
     * @param
     */
    public void setRelMail(String relMail) {
        setItem(COMPANY_RELMAIL, relMail);
    }

    /**
     * 获取联系邮箱
     * @return
     */
    public String getRelMail() {
        return getItemApp(COMPANY_RELMAIL).getString();
    }
    /**
     * 设置管理员
     * @param
     */
    public void setRegUid(String regUid) {
        setItem(COMPANY_REGUID, regUid);
    }

    /**
     * 获取管理员
     * @return
     */
    public String getRegUid() {
        return getItemApp(COMPANY_REGUID).getString();
    }
    /**
     * 设置创建时间
     * @param
     */
    public void setRegTime(String regTime) {
        setItem(COMPANY_REGTIMR, regTime);
    }

    /**
     * 获取创建时间
     * @return
     */
    public String getRegTime() {
        return getItemApp(COMPANY_REGTIMR).getString();
    }
    /**
     * 设置管理员
     * @param
     */
    public void setManager(String manager) {
        setItem(COMPANY_MANAGER, manager);
    }

    /**
     * 获取管理员
     * @return
     */
    public String getManager() {
        return getItemApp(COMPANY_MANAGER).getString();
    }
    /**
     * 设置LOGO
     * @param
     */
    public void setLoGo(String loGo) {
        setItem(COMPANY_LOGO, loGo);
    }

    /**
     * 获取LOGO
     * @return
     */
    public String getLoGo() {
        return getItemApp(COMPANY_LOGO).getString();
    }
    /**
     * 设置单位图片
     * @param
     */
    public void setPhoto(String photo) {
        setItem(COMPANY_PHOTO, photo);
    }

    /**
     * 获取单位图片
     * @return
     */
    public String getPhoto() {
        return getItemApp(COMPANY_PHOTO).getString();
    }
    /**
     * 设置修改时间
     * @param
     */
    public void setModTime(String modTime) {
        setItem(COMPANY_MODTIME, modTime);
    }

    /**
     * 获取修改时间
     * @return
     */
    public String getModTime() {
        return getItemApp(COMPANY_MODTIME).getString();
    }

    /**
     * 设置认证类型
     * @param
     */
    public void setAuthId(String authId) {
        setItem(COMPANY_AUTHID, authId);
    }

    /**
     * 获取认证类型
     * @return
     */
    public String getAuthId() {
        return getItemApp(COMPANY_AUTHID).getString();
    }


    /**
     * 设置企业状态
     * @param
     */
    public void setStatus(String status) {
        setItem(COMPANY_STATUS, status);
    }

    /**
     * 获取企业状态
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
