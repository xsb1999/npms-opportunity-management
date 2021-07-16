package com.neu.opportunitymanagement.oppManagement.controller;


import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppDetail;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppIdAndEmpPosition;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppManagePageInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.entity.Employee;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Autowired
    IOpportunityService iOpportunityService;

    // 初始化机会管理页面
    @GetMapping("getMainPage")
    public RespBean getMainPage(@RequestBody Employee employee) {
        String emp_id = employee.getEmpId();
        String emp_position = employee.getEmpPositionId();
        OppManagePageInfo oppManagePageInfo = iOpportunityService.getMainPage(emp_id, emp_position);
        RespBean respBean = RespBean.ok(200,"进入机会管理页面",oppManagePageInfo);
        return respBean;
    }

    // 销售部门和客户经理二级联动
    @GetMapping("getEmpByDept")
    public RespBean getEmpByDept(@RequestBody DeptInfo dept){
        List<EmpInfo> empInfoList = iOpportunityService.getEmpByDept(dept.getDeptId());
        RespBean respBean = RespBean.ok(200,"ok",empInfoList);
        return respBean;
    }

    // 机会类型和产品二级联动 (根据机会类型查询产品)
    @GetMapping("getProductByType")
    public RespBean getProductByType(@RequestBody OppTypeInfo oppTypeInfo){
        String type_id = oppTypeInfo.getPsoId();
        List<ProductInfo> productInfoList = iOpportunityService.getProductByType(type_id);
        RespBean respBean = RespBean.ok(200,"ok",productInfoList);
        return respBean;
    }

    // 机会类型和产品二级联动 (根据产品查询机会类型)
    @GetMapping("getTypeByProduct")
    public RespBean getTypeByProduct(@RequestBody ProductInfo productInfo){
        String pro_id = productInfo.getCproId();
        List<OppTypeInfo> oppTypeInfoList = iOpportunityService.getTypeByProduct(pro_id);
        RespBean respBean = RespBean.ok(200,"ok",oppTypeInfoList);
        return respBean;
    }

    // 点击机会编号展示机会详细信息
    @GetMapping("showOppDetail")
    public RespBean showOppDetail(@RequestBody OppIdAndEmpPosition op){
        String oppId = op.getOppId();
        String empPositionId = op.getEmpPositionId();
        OppDetail oppDetail = iOpportunityService.showOppDetail(oppId, empPositionId);
        RespBean respBean = RespBean.ok(200,"ok",oppDetail);
        return respBean;
    }

    // 机会查询
    @GetMapping("getOpportunity")
    public RespBean getOpportunity(@RequestBody OppSearchCondition oppSearchCondition){
        return iOpportunityService.getOpportunity(oppSearchCondition);
    }





}

