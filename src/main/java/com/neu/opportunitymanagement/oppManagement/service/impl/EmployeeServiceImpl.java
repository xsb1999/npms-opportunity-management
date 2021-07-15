package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.neu.opportunitymanagement.oppManagement.dto.Role;
import com.neu.opportunitymanagement.oppManagement.entity.Employee;
import com.neu.opportunitymanagement.oppManagement.mapper.EmployeeMapper;
import com.neu.opportunitymanagement.oppManagement.service.IEmployeeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements IEmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public List<Role> getRoleById(String id) {
        return employeeMapper.getRoleById(id);
    }

    @Override
    public String getEmpNameById(String emp_id) {
        return employeeMapper.getEmpNameById(emp_id);
    }
}
