package com.neu.opportunitymanagement.oppManagement.service;

import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
public interface ICustomerService extends IService<Customer> {

    // 根据客户名称查询客户信息
    public RespBean getCusNameByCusId(String cus_name);
}
