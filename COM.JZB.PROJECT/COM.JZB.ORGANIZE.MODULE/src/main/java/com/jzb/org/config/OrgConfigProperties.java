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
