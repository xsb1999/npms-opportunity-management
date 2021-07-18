package com.neu.opportunitymanagement.oppManagement.mapper;

import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchResult;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
@Mapper
@Repository
public interface OpportunityMapper extends BaseMapper<Opportunity> {
    // 获取当前登录人所在部门（根据员工id）
    public List<DeptInfo> getDeptInfoByEmpId(String emp_id);

    // 获取当前登录人的信息（根据员工id和部门id）
    public List<EmpInfo> getEmpInfoByEmpIdAndDeptId(String emp_id, String dept_id);

    // 查询机会归属表
    public List<OppBelongingInfo> getOppBelonging(String dept_name);

    // 根据客户经理id查询机会列表
    public List<OppSearchResult> getOppSearchResult(String emp_id);

    // 根据机会归属id查询机会归属名称
    public String getOppBelongingNameById(String oppb_id);

    // 根据部门id查询部门名称
    public String getDeptNameById(String dept_id);

    // 根据客户经理id查询其姓名
    public String getCusMgrNameById(String cusMgr_id);

    // 销售部门和客户经理二级联动
    public List<EmpInfo> getEmpByDept(String dept_id);

    // 机会类型和产品二级联动 (根据机会类型查询产品)
    public List<ProductInfo> getProductByType(String type_id);

    // 机会类型和产品二级联动 (根据产品查询机会类型)
    public List<OppTypeInfo> getTypeByProduct(String pro_id);

    // 机会查询（查询正常的机会）
    public List<OppSearchResult> getOpportunity(OppSearchCondition searchCondition);
    // 机会查询（查询机会缓存表中的机会（在审批中的机会））
    public List<OppSearchResult> getOpportunityB(OppSearchCondition searchCondition);

    // 获取当前机会表中最大的id
    public String getMaxOppId();



}
