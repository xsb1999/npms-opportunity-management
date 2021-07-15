package com.neu.opportunitymanagement.oppManagement.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
public class Approvallog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "app_id", type = IdType.AUTO)
    private Integer appId;

    private String appFlowName;

    private String appOppId;

    private String appSubmitPeople;

    private LocalDateTime appSubmitDate;

    private String appPeople;

    private String appStatus;

    private String appOpinion;


    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getAppFlowName() {
        return appFlowName;
    }

    public void setAppFlowName(String appFlowName) {
        this.appFlowName = appFlowName;
    }

    public String getAppOppId() {
        return appOppId;
    }

    public void setAppOppId(String appOppId) {
        this.appOppId = appOppId;
    }

    public String getAppSubmitPeople() {
        return appSubmitPeople;
    }

    public void setAppSubmitPeople(String appSubmitPeople) {
        this.appSubmitPeople = appSubmitPeople;
    }

    public LocalDateTime getAppSubmitDate() {
        return appSubmitDate;
    }

    public void setAppSubmitDate(LocalDateTime appSubmitDate) {
        this.appSubmitDate = appSubmitDate;
    }

    public String getAppPeople() {
        return appPeople;
    }

    public void setAppPeople(String appPeople) {
        this.appPeople = appPeople;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public String getAppOpinion() {
        return appOpinion;
    }

    public void setAppOpinion(String appOpinion) {
        this.appOpinion = appOpinion;
    }

    @Override
    public String toString() {
        return "Approvallog{" +
        "appId=" + appId +
        ", appFlowName=" + appFlowName +
        ", appOppId=" + appOppId +
        ", appSubmitPeople=" + appSubmitPeople +
        ", appSubmitDate=" + appSubmitDate +
        ", appPeople=" + appPeople +
        ", appStatus=" + appStatus +
        ", appOpinion=" + appOpinion +
        "}";
    }
}
