package com.jzb.operate.vo;

/**
 *  @author: gongWei
 *  @Date: Created in 2019/12/16 19:09
 *  @Description: 出差申请列表展示-- 审批状态流程
 */


public class ApproverVO {
    private String approveName;

    private String approveStatus;

    private int idx;

    public ApproverVO(String approveName, String approveStatus, int idx) {
        this.approveName = approveName;
        this.approveStatus = approveStatus;
        this.idx = idx;
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

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    @Override
    public String toString() {
        return "ApproverVO{" +
                "approveName='" + approveName + '\'' +
                ", approveStatus='" + approveStatus + '\'' +
                ", idx=" + idx +
                '}';
    }
}
