package com.neu.opportunitymanagement.oppManagement.service;

import com.neu.opportunitymanagement.oppManagement.dto.approval.Approval;
import com.neu.opportunitymanagement.oppManagement.dto.common.EmpInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.OppTypeInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.ProductInfo;
import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppDetail;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppIdAndOppBId;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppManagePageInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.entity.Opportunity;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    public OppManagePageInfo getMainPage(String emp_id);

    // 销售部门和客户经理二级联动
    public List<EmpInfo> getEmpByDept(String dept_id);

    // 机会类型和产品二级联动 (根据机会类型查询产品)
    public List<ProductInfo> getProductByType(String type_id);

    // 机会类型和产品二级联动 (根据产品查询机会类型)
    public List<OppTypeInfo> getTypeByProduct(String pro_id);

    // 点击机会编号展示机会详细信息
    public OppDetail showOppDetail(String oppId, String empId);

    // 机会查询
    public RespBean getOpportunity(OppSearchCondition oppSearchCondition);

    // 点击“修改”按钮，显示修改页面
    public RespBean showUpdatePage(OppIdAndOppBId oppIdAndOppBId);

    // 机会跟踪页面初始化
    public RespBean getOppTrackMainPage(String oppId);

    // 机会审批页面初始化
    public RespBean getApprovalPage(String empId);

    // 点击“审批”按钮，显示机会审批信息
    public RespBean showOppApproveDetail(String oppIdB);

    // 提交审批意见
    public RespBean approval(Approval approval) throws Exception;





}
