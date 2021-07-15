package com.neu.opportunitymanagement.oppManagement.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
@RestController
@RequestMapping("/oppManagement/opportunity")
public class OpportunityController {

    @GetMapping("test")
    public String test(){
        return "ok";
    }

}

