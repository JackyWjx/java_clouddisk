package com.jzb.org.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author dell
 * @Version 1.0
 * @Since 1.0
 * @Date: 2019/8/20 17:46
 */
@Component
@ConfigurationProperties(prefix = "com.jzb.org")
public class OrgConfigProperties {
    private int rowSize;

    /**
     * 导入用户的保存路径
     */
    private String importPath;

    /**
     * 发送邀请短信模板
     */
    private String template;

    /**
     * 发送注册用户的邀请短信模板
     */
    private String invite;

    /**
     * 新建单位短信模块
     */
    private String addCompany;

    /**
     * 邀请用户加入单位
     * 您好,您在(XXX时间)接收到了来自(XXX单位)的邀请.请登录系统确认是否加入!
     */
    private String invitationToJoin;

    /**
     * 取消邀请用户
     * 您好,(XXX企业)在(xxx时间)取消了对您的邀请.请登录查看详情!
     */
    private String disinvite;

    /**
     * 通过申请加入单位
     * 您好,您申请加入(XXX单位)的审核已在(XXX时间)通过.请您尽快登录,核对信息!
     */
    private String applicantPass;

    /**
     * 拒绝用户加入单位
     * 您好,您申请加入(XXX单位)的审核在(XXX时间)已被拒绝.请登录查看详情!
     */
    private String applicantRefuse;

    public String getApplicantRefuse() {
        return applicantRefuse;
    }

    public void setApplicantRefuse(String applicantRefuse) {
        this.applicantRefuse = applicantRefuse;
    }

    public String getApplicantPass() {
        return applicantPass;
    }

    public void setApplicantPass(String applicantPass) {
        this.applicantPass = applicantPass;
    }

    public String getDisinvite() {
        return disinvite;
    }

    public void setDisinvite(String disinvite) {
        this.disinvite = disinvite;
    }

    public String getInvitationToJoin() {
        return invitationToJoin;
    }

    public void setInvitationToJoin(String invitationToJoin) {
        this.invitationToJoin = invitationToJoin;
    }

    public String getAddCompany() {
        return addCompany;
    }

    public void setAddCompany(String addcompany) {
        this.addCompany = addcompany;
    }


    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    /**
     * CRMId
     */
    private String crmId;

    public String getCrmId() {
        return crmId;
    }

    public void setCrmId(String crmId) {
        this.crmId = crmId;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public int getRowSize() {
        return rowSize;
    }

    public void setRowSize(int rowSize) {
        this.rowSize = rowSize;
    }
}
