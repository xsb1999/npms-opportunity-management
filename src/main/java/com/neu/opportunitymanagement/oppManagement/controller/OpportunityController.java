package com.neu.opportunitymanagement.oppManagement.controller;


import com.neu.opportunitymanagement.oppManagement.dto.approval.Approval;
import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.*;
import com.neu.opportunitymanagement.oppManagement.dto.tracklog.UpdateTrackInfo;
import com.neu.opportunitymanagement.oppManagement.entity.Employee;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityBufferService;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import com.neu.opportunitymanagement.oppManagement.service.ITrackinglogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    IOpportunityBufferService iOpportunityBufferService;
    @Autowired
    ITrackinglogService iTrackinglogService;



    // 初始化机会管理页面
    @GetMapping("getMainPage")
    public RespBean getMainPage(@RequestParam String empId) {
        OppManagePageInfo oppManagePageInfo = iOpportunityService.getMainPage(empId);
        RespBean respBean = RespBean.ok(200,"进入机会管理页面",oppManagePageInfo);
        return respBean;
    }


    // 销售部门和客户经理二级联动
    @GetMapping("getEmpByDept")
    public RespBean getEmpByDept(@RequestParam String deptId){
        List<EmpInfo> empInfoList = iOpportunityService.getEmpByDept(deptId);
        RespBean respBean = RespBean.ok(200,"ok",empInfoList);
        return respBean;
    }


    // 机会类型和产品二级联动 (根据机会类型查询产品)
    @GetMapping("getProductByType")
    public RespBean getProductByType(@RequestParam String psoId){
        List<ProductInfo> productInfoList = iOpportunityService.getProductByType(psoId);
        RespBean respBean = RespBean.ok(200,"ok",productInfoList);
        return respBean;
    }


    // 机会类型和产品二级联动 (根据产品查询机会类型)
    @GetMapping("getTypeByProduct")
    public RespBean getTypeByProduct(@RequestParam String cproId){
        List<OppTypeInfo> oppTypeInfoList = iOpportunityService.getTypeByProduct(cproId);
        RespBean respBean = RespBean.ok(200,"ok",oppTypeInfoList);
        return respBean;
    }


    // 点击机会编号展示机会详细信息
    @GetMapping("showOppDetail")
    public RespBean showOppDetail(@RequestParam String oppId, @RequestParam String empId){
        OppDetail oppDetail = iOpportunityService.showOppDetail(oppId, empId);
        RespBean respBean = RespBean.ok(200,"ok",oppDetail);
        return respBean;
    }


    // 机会查询
    @PostMapping("getOpportunity")
    public RespBean getOpportunity(@RequestBody OppSearchCondition oppSearchCondition){
        return iOpportunityService.getOpportunity(oppSearchCondition);
    }


    // 判断新增机会时机会名称是否重复
    @GetMapping("testAddRepetition")
    public RespBean testAddRepetition(@RequestParam String oppbName, @RequestParam String cusId){
        return iOpportunityBufferService.testAddRepetition(oppbName, cusId);
    }


    // 机会新增
    @PostMapping("addOpportunity")
    public RespBean addOpportunity(@RequestBody AddOpportunityInfo addOpportunityInfo){
        RespBean respBean = null;
        try {
            respBean = iOpportunityBufferService.addOpportunity(addOpportunityInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    // 点击“修改”按钮，显示修改页面
    @PostMapping("showUpdatePage")
    public RespBean showUpdatePage(@RequestBody OppIdAndOppBId oppIdAndOppBId){
        return iOpportunityService.showUpdatePage(oppIdAndOppBId);
    }


    // 判断修改机会时机会名称是否重复
    @GetMapping("testUpdateRepetition")
    public RespBean testUpdateRepetition(@RequestBody String oppbName, @RequestBody String cusId, @RequestBody String oppbId){
        return iOpportunityBufferService.testUpdateRepetition(oppbName,cusId,oppbId);
    }


    // 机会修改
    @PostMapping("updateOpportunity")
    public RespBean updateOpportunity(@RequestBody UpdateOpportunityInfo updateOpportunityInfo){
        RespBean respBean = null;
        try {
            respBean = iOpportunityBufferService.updateOpportunity(updateOpportunityInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    // 机会跟踪页面初始化
    @GetMapping("getOppTrackMainPage")
    public RespBean getOppTrackMainPage(@RequestParam String oppId){
        return iOpportunityService.getOppTrackMainPage(oppId);
    }


    // 增删改机会跟踪记录
    @PostMapping("curdTrackinglog")
    public RespBean curdTrackinglog(@RequestBody UpdateTrackInfo updateTrackInfo){
        RespBean respBean = null;
        try {
            respBean = iTrackinglogService.curdTrackinglog(updateTrackInfo);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
        }
        return respBean;
    }


    // 机会审批页面初始化
    @GetMapping("getApprovalPage")
    public RespBean getApprovalPage(@RequestParam String empId){
        return iOpportunityService.getApprovalPage(empId);
    }


    // 点击“审批”按钮，显示机会审批信息
    @GetMapping("showOppApproveDetail")
    public RespBean showOppApproveDetail(@RequestParam String oppIdB){
        return iOpportunityService.showOppApproveDetail(oppIdB);
    }


    // 提交审批意见
    @PostMapping("approval")
    public RespBean approval(@RequestBody Approval approval){
        RespBean respBean = null;
        try {
            respBean = iOpportunityService.approval(approval);
        }catch (Exception e){
            respBean = RespBean.error(500, "系统错误，请联系管理员...");
            System.err.println(e);
        }
        return respBean;
    }






}

