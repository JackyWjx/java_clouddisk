package com.jzb.operate.vo;

/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/16 19:09
 *  @Description: 出差申请列表展示-- 审批状态流程
 */


public class ApproverVO {
    private String approveName;

    private String approveStatus;

    public ApproverVO(String approveName, String approveStatus) {
        this.approveName = approveName;
        this.approveStatus = approveStatus;
    }

    public String getApproveName() {
        return approveName;
    }

    public void setApproveName(String approveName) {
        this.approveName = approveName;
    }

    public String getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(String approveStatus) {
        this.approveStatus = approveStatus;
    }
}
