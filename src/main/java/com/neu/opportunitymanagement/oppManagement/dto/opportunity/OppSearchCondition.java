package com.neu.opportunitymanagement.oppManagement.dto.opportunity;

import java.io.Serializable;

public class OppSearchCondition implements Serializable {
    private String oppId;   // 机会id
    private String oppName; // 机会名称
    private String oppSalesDept;    // 销售部门id
    private String oppCustomerManagerId;    // 客户经理id
    private String oppSignTime1;    // 预签时间from
    private String oppSignTime2;    // 预签时间to
    private String oppBelonging;    // 机会归属id
    private String oppStatus;   // 机会状态id
    private String oppPhase;    // 机会阶段
    private String oppType;     // 机会类型id
    private String oppProduct;  // 产品id

    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    public String getOppName() {
        return oppName;
    }

    public void setOppName(String oppName) {
        this.oppName = oppName;
    }

    public String getOppSalesDept() {
        return oppSalesDept;
    }

    public void setOppSalesDept(String oppSalesDept) {
        this.oppSalesDept = oppSalesDept;
    }

    public String getOppCustomerManagerId() {
        return oppCustomerManagerId;
    }

    public void setOppCustomerManagerId(String oppCustomerManagerId) {
        this.oppCustomerManagerId = oppCustomerManagerId;
    }

    public String getOppSignTime1() {
        return oppSignTime1;
    }

    public void setOppSignTime1(String oppSignTime1) {
        this.oppSignTime1 = oppSignTime1;
    }

    public String getOppSignTime2() {
        return oppSignTime2;
    }

    public void setOppSignTime2(String oppSignTime2) {
        this.oppSignTime2 = oppSignTime2;
    }

    public String getOppBelonging() {
        return oppBelonging;
    }

    public void setOppBelonging(String oppBelonging) {
        this.oppBelonging = oppBelonging;
    }

    public String getOppStatus() {
        return oppStatus;
    }

    public void setOppStatus(String oppStatus) {
        this.oppStatus = oppStatus;
    }

    public String getOppPhase() {
        return oppPhase;
    }

    public void setOppPhase(String oppPhase) {
        this.oppPhase = oppPhase;
    }

    public String getOppType() {
        return oppType;
    }

    public void setOppType(String oppType) {
        this.oppType = oppType;
    }

    public String getOppProduct() {
        return oppProduct;
    }

    public void setOppProduct(String oppProduct) {
        this.oppProduct = oppProduct;
    }

    @Override
    public String toString() {
        return "OppSearchCondition{" +
                "oppId='" + oppId + '\'' +
                ", oppName='" + oppName + '\'' +
                ", oppSalesDept='" + oppSalesDept + '\'' +
                ", oppCustomerManagerId='" + oppCustomerManagerId + '\'' +
                ", oppSignTime1='" + oppSignTime1 + '\'' +
                ", oppSignTime2='" + oppSignTime2 + '\'' +
                ", oppBelonging='" + oppBelonging + '\'' +
                ", oppStatus='" + oppStatus + '\'' +
                ", oppPhase='" + oppPhase + '\'' +
                ", oppType='" + oppType + '\'' +
                ", oppProduct='" + oppProduct + '\'' +
                '}';
    }
}
