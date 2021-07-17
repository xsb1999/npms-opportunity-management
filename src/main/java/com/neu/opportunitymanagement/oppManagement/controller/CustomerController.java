package com.neu.opportunitymanagement.oppManagement.controller;


import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.entity.Customer;
import com.neu.opportunitymanagement.oppManagement.service.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/oppManagement/customer")
public class CustomerController {

    @Autowired
    ICustomerService iCustomerService;

    // 根据客户名称查询客户信息
    @GetMapping("getCusNameByCusId")
    public RespBean getCusNameByCusId(@RequestParam String cusName){
        return iCustomerService.getCusNameByCusId(cusName);
    }

}

