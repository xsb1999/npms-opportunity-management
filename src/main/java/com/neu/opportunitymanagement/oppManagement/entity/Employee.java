package com.neu.opportunitymanagement.oppManagement.entity;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
public class Employee implements Serializable {

    private static final long serialVersionUID = 1L;

    private String empId;

    private String empName;

    private String empPassword;

    private String empDeptId;

    private String empPositionId;


    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getEmpDeptId() {
        return empDeptId;
    }

    public void setEmpDeptId(String empDeptId) {
        this.empDeptId = empDeptId;
    }

    public String getEmpPositionId() {
        return empPositionId;
    }

    public void setEmpPositionId(String empPositionId) {
        this.empPositionId = empPositionId;
    }

    @Override
    public String toString() {
        return "Employee{" +
        "empId=" + empId +
        ", empName=" + empName +
        ", empPassword=" + empPassword +
        ", empDeptId=" + empDeptId +
        ", empPositionId=" + empPositionId +
        "}";
    }
}
