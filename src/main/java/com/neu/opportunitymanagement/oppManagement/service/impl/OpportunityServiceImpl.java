package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppManagePageInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchResult;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.neu.opportunitymanagement.oppManagement.mapper.EmployeeMapper;
import com.neu.opportunitymanagement.oppManagement.mapper.OpportunityMapper;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
