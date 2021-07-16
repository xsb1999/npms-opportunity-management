package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppDetail;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppManagePageInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchResult;
import com.neu.opportunitymanagement.oppManagement.entity.*;
import com.neu.opportunitymanagement.oppManagement.mapper.*;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Wrapper;
import java.util.ArrayList;
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
public class OpportunityServiceImpl extends ServiceImpl<OpportunityMapper, Opportunity> implements IOpportunityService {

    @Autowired
    OpportunityMapper opportunityMapper;
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    SubOpportunityMapper subOpportunityMapper;
    @Autowired
    CompetitorMapper competitorMapper;
    @Autowired
    PayerMapper payerMapper;
    @Autowired
    TrackinglogMapper trackinglogMapper;


    @Override
    public OppManagePageInfo getMainPage(String emp_id, String emp_position) {

        List<DeptInfo> deptInfoList = new ArrayList<>();
        List<EmpInfo> empInfoList = new ArrayList<>();
        List<OppBelongingInfo> oppBelongingList = new ArrayList<>();
        List<OppSearchResult> oppSearchResultList = new ArrayList<>();

        // 事业部总经理（展示该部门下客户经理的客户列表，且机会归属为当前事业部）
        if (emp_position.equals("10000030")){
            // 获取当前登录人所在部门
            deptInfoList = opportunityMapper.getDeptInfoByEmpId(emp_id);
            // 获取该部门下的所有的客户经理
            empInfoList = opportunityMapper.getEmpInfoByEmpIdAndDeptId(null,deptInfoList.get(0).getDeptId());
            // 机会归属为当前事业部
            oppBelongingList = opportunityMapper.getOppBelonging(deptInfoList.get(0).getDeptName());
            // 获取全部的机会列表（默认显示为空）
            oppSearchResultList = new ArrayList<>();
        }

        // 销售总监（展示该部门下客户经理的客户列表，机会归属为所有）
        if (emp_position.equals("30000010")){
            // 获取当前登录人所在部门
            deptInfoList = opportunityMapper.getDeptInfoByEmpId(emp_id);
            // 获取该部门下的所有的客户经理
            empInfoList = opportunityMapper.getEmpInfoByEmpIdAndDeptId(null,deptInfoList.get(0).getDeptId());
            // 机会归属为所有部门
            oppBelongingList = opportunityMapper.getOppBelonging(null);
            // 获取该销售总监的机会列表
            oppSearchResultList = opportunityMapper.getOppSearchResult(emp_id);
            for (OppSearchResult result : oppSearchResultList) {
                // 设定部门名称
                result.setOppSalesDeptName(deptInfoList.get(0).getDeptName());
                // 设定客户经理姓名
                result.setOppCustomerManagerName(employeeMapper.getEmpNameById(emp_id));
                // 设定机会归属名称
                result.setOppBelongingName(opportunityMapper.getOppBelongingNameById(result.getOppBelonging()));
            }

        }

        // 客户经理（展示本人的客户列表，如果是事业部，则机会归属为当前事业部，反之则为所有）
        if (emp_position.equals("30000030")){
            // 获取当前登录人所在部门
            deptInfoList = opportunityMapper.getDeptInfoByEmpId(emp_id);
            // 获取该客户经理的信息
            empInfoList = opportunityMapper.getEmpInfoByEmpIdAndDeptId(emp_id, null);
            // 判断该客户经理是否是事业部的，如果是，则机会归属为当前事业部，反之则为所有
            if (deptInfoList.get(0).getDeptId().substring(0, 3).equals("802")){
                oppBelongingList = opportunityMapper.getOppBelonging(null);
            }else {
                oppBelongingList = opportunityMapper.getOppBelonging(deptInfoList.get(0).getDeptName());
            }
            // 获取该客户经理的机会列表
            oppSearchResultList = opportunityMapper.getOppSearchResult(emp_id);
            for (OppSearchResult result : oppSearchResultList) {
                // 设定部门名称
                result.setOppSalesDeptName(deptInfoList.get(0).getDeptName());
                // 设定客户经理姓名
                result.setOppCustomerManagerName(empInfoList.get(0).getEmpName());
                // 设定机会归属名称
                result.setOppBelongingName(opportunityMapper.getOppBelongingNameById(result.getOppBelonging()));
            }

        }

        // 营销专员、营销副总和高层领导（可以看到所有部门和客户经理，机会归属为所有）
        if (emp_position.equals("20000010") || emp_position.equals("20000020") || emp_position.equals("50000000")){
            // 获取所有部门
            deptInfoList = opportunityMapper.getDeptInfoByEmpId(null);
            // 获取所有的客户经理
            empInfoList = opportunityMapper.getEmpInfoByEmpIdAndDeptId(null,null);
            // 机会归属为所有部门
            oppBelongingList = opportunityMapper.getOppBelonging(null);
            // 获取全部的机会列表（默认显示为空）
            oppSearchResultList = new ArrayList<>();
        }

        OppManagePageInfo oppManagePageInfo = new OppManagePageInfo();
        oppManagePageInfo.setDeptInfoList(deptInfoList);
        oppManagePageInfo.setEmpInfoList(empInfoList);
        oppManagePageInfo.setOppBelongingList(oppBelongingList);
        oppManagePageInfo.setOppSearchResultList(oppSearchResultList);

        return oppManagePageInfo;
    }

    @Override
    public List<EmpInfo> getEmpByDept(String dept_id) {
        return opportunityMapper.getEmpByDept(dept_id);
    }

    @Override
    public List<ProductInfo> getProductByType(String type_id) {
        return opportunityMapper.getProductByType(type_id);
    }

    @Override
    public List<OppTypeInfo> getTypeByProduct(String pro_id) {
        return opportunityMapper.getTypeByProduct(pro_id);
    }

    @Override
    public OppDetail showOppDetail(String oppId, String empPositionId) {

        Opportunity opportunity = new Opportunity();
        List<SubOpportunity> subOpportunityList = new ArrayList<>();
        List<Competitor> competitorList = new ArrayList<>();
        List<Payer> payerList = new ArrayList<>();
        List<Trackinglog> trackinglogList = new ArrayList<>();

        // 获取机会信息
        opportunity = opportunityMapper.selectById(oppId);
        // 获取子机会列表
        QueryWrapper<SubOpportunity> qw = Wrappers.query();
        qw.eq("sub_opp_opp_id", oppId);
        subOpportunityList = subOpportunityMapper.selectList(qw);
        // 获取竞争情况列表
        QueryWrapper<Competitor> qw1 = Wrappers.query();
        qw1.eq("comp_opp_id", oppId);
        competitorList = competitorMapper.selectList(qw1);
        // 获取购买决策人列表
        QueryWrapper<Payer> qw2 = Wrappers.query();
        qw2.eq("p_opp_id", oppId);
        payerList = payerMapper.selectList(qw2);
        // 获取机会跟踪日志列表（只有营销副总和高层领导可以看到机会跟踪记录信息）
        if (empPositionId.equals("20000010") || empPositionId.equals("50000000")){
            QueryWrapper<Trackinglog> qw3 = Wrappers.query();
            qw3.eq("t_opp_id", oppId);
            trackinglogList = trackinglogMapper.selectList(qw3);
        }

        OppDetail oppDetail = new OppDetail();
        oppDetail.setOpportunity(opportunity);
        oppDetail.setSubOpportunityList(subOpportunityList);
        oppDetail.setCompetitorList(competitorList);
        oppDetail.setPayerList(payerList);
        oppDetail.setTrackinglogList(trackinglogList);

        return oppDetail;
    }

    @Override
    public RespBean getOpportunity(OppSearchCondition oppSearchCondition) {
        List<OppSearchResult> oppSearchResultList = opportunityMapper.getOpportunity(oppSearchCondition);
        // 获取部门名称，客户经理姓名，机会归属名称
        for (OppSearchResult osr : oppSearchResultList) {
            osr.setOppSalesDeptName(opportunityMapper.getDeptNameById(osr.getOppSalesDept()));
            osr.setOppCustomerManagerName(opportunityMapper.getCusMgrNameById(osr.getOppCustomerManagerId()));
            osr.setOppBelongingName(opportunityMapper.getOppBelongingNameById(osr.getOppBelonging()));
        }
        String msg = "";
        // 若查询结果为空，则提示“未查询到结果！”
        if (oppSearchResultList.isEmpty()){
            msg = "未查询到结果！";
        }else {
            msg = "查询成功！";
        }
        RespBean respBean = RespBean.ok(200, msg, oppSearchResultList);
        return respBean;
    }


}
