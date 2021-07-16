package com.neu.opportunitymanagement.oppManagement.service;

import com.neu.opportunitymanagement.oppManagement.dto.common.EmpInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.OppTypeInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.ProductInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppDetail;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppManagePageInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
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
public interface IOpportunityService extends IService<Opportunity> {

    // 初始化客户管理页面
    public OppManagePageInfo getMainPage(String emp_id, String emp_position);

    // 销售部门和客户经理二级联动
    public List<EmpInfo> getEmpByDept(String dept_id);

    // 机会类型和产品二级联动 (根据机会类型查询产品)
    public List<ProductInfo> getProductByType(String type_id);

    // 机会类型和产品二级联动 (根据产品查询机会类型)
    public List<OppTypeInfo> getTypeByProduct(String pro_id);

    // 点击机会编号展示机会详细信息
    public OppDetail showOppDetail(String oppId, String empPositionId);

    // 机会查询
    public RespBean getOpportunity(OppSearchCondition oppSearchCondition);






}
