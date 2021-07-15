package com.neu.opportunitymanagement.oppManagement.service;

import com.neu.opportunitymanagement.oppManagement.dto.common.Role;
import com.neu.opportunitymanagement.oppManagement.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
public interface IEmployeeService extends IService<Employee> {
    // 获取员工的roles
    public List<Role> getRoleById(String emp_id);
    // 通过员工id获取姓名
    public String getEmpNameById(String emp_id);
}
