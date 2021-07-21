package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.opportunitymanagement.oppManagement.dto.approval.Approval;
import com.neu.opportunitymanagement.oppManagement.dto.approval.ApproveDetail;
import com.neu.opportunitymanagement.oppManagement.dto.approval.Flow;
import com.neu.opportunitymanagement.oppManagement.dto.common.*;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.*;
import com.neu.opportunitymanagement.oppManagement.dto.tracklog.OppTrackMainPage;
import com.neu.opportunitymanagement.oppManagement.entity.*;
import com.neu.opportunitymanagement.oppManagement.mapper.*;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.plugin.javascript.navig.LinkArray;

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
    @Autowired
    OpportunityBufferMapper opportunityBufferMapper;
    @Autowired
    SubOpportunityBufferMapper subOpportunityBufferMapper;
    @Autowired
    CompetitorBufferMapper competitorBufferMapper;
    @Autowired
    PayerBufferMapper payerBufferMapper;
    @Autowired
    ApprovallogMapper approvallogMapper;
    @Autowired
    CustomerMapper customerMapper;



    @Override
    public OppManagePageInfo getMainPage(String emp_id) {

        // 根据员工id获取员工职位
        Employee employee = employeeMapper.selectById(emp_id);
        String emp_position = employee.getEmpPositionId();

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

    @Cacheable(cacheNames="opp_getEmpByDept")
    @Override
    public List<EmpInfo> getEmpByDept(String dept_id) {
        return opportunityMapper.getEmpByDept(dept_id);
    }

    @Cacheable(cacheNames="opp_getProductByType")
    @Override
    public List<ProductInfo> getProductByType(String type_id) {
        return opportunityMapper.getProductByType(type_id);
    }

    @Cacheable(cacheNames="opp_getTypeByProduct")
    @Override
    public List<OppTypeInfo> getTypeByProduct(String pro_id) {
        return opportunityMapper.getTypeByProduct(pro_id);
    }

    @Override
    public OppDetail showOppDetail(String oppId, String empId) {

        Employee e = employeeMapper.selectById(empId);
        String empPositionId = e.getEmpPositionId();

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
        List<OppSearchResult> oppSearchResultList = new ArrayList<>();
        // 查询缓存机会表的机会（新增的机会，属于草稿状态或者流程中或者是退回状态，id为null）
        List<OppSearchResult> oppSearchResultList1 = opportunityMapper.getOpportunityB(oppSearchCondition);
        // 查询普通机会表
        List<OppSearchResult> oppSearchResultList2 = opportunityMapper.getOpportunity(oppSearchCondition);

        oppSearchResultList.addAll(oppSearchResultList1);
        oppSearchResultList.addAll(oppSearchResultList2);

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

    @Override
    public RespBean showUpdatePage(OppIdAndOppBId oppIdAndOppBId) {
        // 机会状态为流程中、暂停、结束的不能修改
        if (oppIdAndOppBId.getOppStatus().equals("20")){
            return RespBean.error(500, "该机会状态为流程中，不能修改！");
        }
        if (oppIdAndOppBId.getOppStatus().equals("40")){
            return RespBean.error(500, "该机会状态为暂停，不能修改！");
        }
        if (oppIdAndOppBId.getOppStatus().equals("50")){
            return RespBean.error(500, "该机会状态为关闭，不能修改！");
        }

        UpdatePageInfo updatePageInfo = new UpdatePageInfo();
        String opp_id = oppIdAndOppBId.getOppId();
        String oppb_id = oppIdAndOppBId.getOppBId();
        // 判断该机会是否为新增的，若机会id为null则是新增的，需要从缓存表中查询
        if (opp_id == null){
            OpportunityBuffer opportunityBuffer;
            List<SubOpportunityBuffer> subOpportunityBufferList;
            List<CompetitorBuffer> competitorBufferList;
            List<PayerBuffer> payerBufferList;

            opportunityBuffer = opportunityBufferMapper.selectById(oppb_id);

            QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
            qw1.eq("sub_oppb_opp_id", oppb_id);
            subOpportunityBufferList = subOpportunityBufferMapper.selectList(qw1);

            QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
            qw2.eq("compb_opp_id", oppb_id);
            competitorBufferList = competitorBufferMapper.selectList(qw2);

            QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
            qw3.eq("pb_opp_id", oppb_id);
            payerBufferList = payerBufferMapper.selectList(qw3);

            BufferOppInfo bufferOppInfo = new BufferOppInfo();
            bufferOppInfo.setOpportunityBuffer(opportunityBuffer);
            bufferOppInfo.setSubOpportunityBufferList(subOpportunityBufferList);
            bufferOppInfo.setCompetitorBufferList(competitorBufferList);
            bufferOppInfo.setPayerBufferList(payerBufferList);

            updatePageInfo.setBufferOppInfo(bufferOppInfo);

        }else {
            Opportunity opportunity;
            List<SubOpportunity> subOpportunityList;
            List<Competitor> competitorList;
            List<Payer> payerList;

            opportunity = opportunityMapper.selectById(opp_id);

            QueryWrapper<SubOpportunity> qw1 = Wrappers.query();
            qw1.eq("sub_opp_opp_id", opp_id);
            subOpportunityList = subOpportunityMapper.selectList(qw1);

            QueryWrapper<Competitor> qw2 = Wrappers.query();
            qw2.eq("comp_opp_id", opp_id);
            competitorList = competitorMapper.selectList(qw2);

            QueryWrapper<Payer> qw3 = Wrappers.query();
            qw3.eq("p_opp_id", opp_id);
            payerList = payerMapper.selectList(qw3);

            CommonOppInfo commonOppInfo = new CommonOppInfo();
            commonOppInfo.setOpportunity(opportunity);
            commonOppInfo.setSubOpportunityList(subOpportunityList);
            commonOppInfo.setCompetitorList(competitorList);
            commonOppInfo.setPayerList(payerList);

            updatePageInfo.setCommonOppInfo(commonOppInfo);
        }

        RespBean respBean = RespBean.ok(200, "ok", updatePageInfo);
        return respBean;
    }

    @Override
    public RespBean getOppTrackMainPage(String oppId) {
        RespBean respBean = null;
        if (oppId == null || oppId == ""){
            respBean = RespBean.error(500, "该机会还未创建成功，无法进行跟踪！");
            return respBean;
        }
        Opportunity opp = opportunityMapper.selectById(oppId);
        String oppName = opp.getOppName();
        List<Trackinglog> oppTrackList;
        QueryWrapper<Trackinglog> qw = Wrappers.query();
        qw.eq("t_opp_id", oppId);
        oppTrackList = trackinglogMapper.selectList(qw);

        OppTrackMainPage oppTrackMainPage = new OppTrackMainPage();
        oppTrackMainPage.setOppId(oppId);
        oppTrackMainPage.setOppName(oppName);
        oppTrackMainPage.setOppTrackList(oppTrackList);

        respBean = RespBean.ok(200, "ok", oppTrackMainPage);
        return respBean;
    }

    @Override
    public RespBean getApprovalPage(String empId) {
        // 根据员工id获取员工职位
        Employee employee = employeeMapper.selectById(empId);
        String empPositionId = employee.getEmpPositionId();
        List<Flow> flowList = opportunityBufferMapper.getApproveOppList(empPositionId);
        for (Flow flow : flowList) {
            if (flow.getOppId() == null){
                flow.setFlowName("机会新增流程");
            }else {
                flow.setFlowName("机会修改流程");
            }
        }
        return RespBean.ok(200, "ok", flowList);
    }

    @Override
    public RespBean showOppApproveDetail(String oppIdB) {
        // 获取缓存表中的机会
        OpportunityBuffer opportunityBuffer = opportunityBufferMapper.selectById(oppIdB);
        // 获取子机会列表
        QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
        qw1.eq("sub_oppb_opp_id", oppIdB);
        List<SubOpportunityBuffer> subOpportunityBufferList = subOpportunityBufferMapper.selectList(qw1);
        // 获取竞争情况列表
        QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
        qw2.eq("compb_opp_id", oppIdB);
        List<CompetitorBuffer> competitorBufferList = competitorBufferMapper.selectList(qw2);
        // 获取购买决策人列表
        QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
        qw3.eq("pb_opp_id", oppIdB);
        List<PayerBuffer> payerBufferList = payerBufferMapper.selectList(qw3);

        String oppId = opportunityBuffer.getOppbOppId();
        // 获取机会跟踪记录列表
        QueryWrapper<Trackinglog> qw4 = Wrappers.query();
        qw4.eq("t_opp_id", oppId);
        List<Trackinglog> trackinglogList = trackinglogMapper.selectList(qw4);
        // 获取机会审批日志列表
        QueryWrapper<Approvallog> qw5 = Wrappers.query();
        qw5.eq("app_opp_id", oppIdB);
        List<Approvallog> approvallogList = approvallogMapper.selectList(qw5);
        // 获取客户信息
        Customer customer = customerMapper.selectById(opportunityBuffer.getOppbCusId());

        ApproveDetail approveDetail = new ApproveDetail();
        approveDetail.setOpportunityBuffer(opportunityBuffer);
        approveDetail.setSubOpportunityBufferList(subOpportunityBufferList);
        approveDetail.setCompetitorBufferList(competitorBufferList);
        approveDetail.setPayerBufferList(payerBufferList);
        approveDetail.setTrackinglogList(trackinglogList);
        approveDetail.setApprovallogList(approvallogList);
        approveDetail.setCustomer(customer);

        RespBean respBean = RespBean.ok(200, "ok", approveDetail);
        return respBean;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public RespBean approval(Approval approval) throws Exception{
        String empName = approval.getEmpName();
        String empPositionId = approval.getEmpPositionId();
        int oppIdB = approval.getOppIdB();
        String approveResult = approval.getApproveResult();
        String approveAdvice = approval.getApproveAdvice();
        String msg = "";

        // 判断当前审批人（营销副总 or 事业部总经理 or 销售总监）
        if (empPositionId.equals("20000010")){
            // 营销副总
            if (approveResult.equals("-1")){
                // 拒绝
                // 直接删除缓存表中的数据
                QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
                qw1.eq("sub_oppb_opp_id", oppIdB);
                subOpportunityBufferMapper.delete(qw1);

                QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
                qw2.eq("compb_opp_id", oppIdB);
                competitorBufferMapper.delete(qw2);

                QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
                qw3.eq("pb_opp_id", oppIdB);
                payerBufferMapper.delete(qw3);

                QueryWrapper<Approvallog> qw4 = Wrappers.query();
                qw4.eq("app_opp_id", oppIdB);
                approvallogMapper.delete(qw4);

                opportunityBufferMapper.deleteById(oppIdB);
                msg = "已拒绝机会！";
            }

            if (approveResult.equals("0")){
                // 退回
                // 修改缓存机会表中该机会的状态为60(退回状态)，并新增审批日志
                OpportunityBuffer opb = opportunityBufferMapper.selectById(oppIdB);
                opb.setOppbStatus("60");
                opportunityBufferMapper.updateById(opb);
                Approvallog approvallog = new Approvallog();
                approvallog.setAppFlowName("机会新增流程");
                approvallog.setAppPeople(empName);
                approvallog.setAppOpinion(approveAdvice);
                approvallog.setAppStatus("初始");
                approvallog.setAppResult("退回");
                approvallog.setAppSubmitDate(opb.getOppbSubmitDate());
                approvallog.setAppOppId(oppIdB);
                // 写入审批日志表
                approvallogMapper.insert(approvallog);
                msg = "已退回机会！";
            }

            if (approveResult.equals("1")){
                // 同意
                // 修改缓存机会表中该机会的审批状态为1，并新增审批日志
                OpportunityBuffer opb = opportunityBufferMapper.selectById(oppIdB);
                opb.setOppbApproveStatus("1");
                opportunityBufferMapper.updateById(opb);
                Approvallog approvallog = new Approvallog();
                approvallog.setAppFlowName("机会新增流程");
                approvallog.setAppPeople(empName);
                approvallog.setAppOpinion(approveAdvice);
                approvallog.setAppStatus("初始");
                approvallog.setAppResult("同意");
                approvallog.setAppSubmitDate(opb.getOppbSubmitDate());
                approvallog.setAppOppId(oppIdB);
                // 写入审批日志表
                approvallogMapper.insert(approvallog);
                msg = "已通过机会！";
            }
        }

        if (empPositionId.equals("10000030") || empPositionId.equals("30000010")){
            // 事业部总经理或销售总监
            if (approveResult.equals("-1")){
                // 拒绝
                // 判断是新增机会还是修改机会
                OpportunityBuffer opb = opportunityBufferMapper.selectById(oppIdB);
                if (opb.getOppbOppId() != null){
                    // 修改机会（需要修改机会表中该机会的状态为“正常"）
                    Opportunity oppo = opportunityMapper.selectById(opb.getOppbOppId());
                    oppo.setOppStatus("30");
                    opportunityMapper.updateById(oppo);
                }
                // 直接删除缓存表中的数据
                QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
                qw1.eq("sub_oppb_opp_id", oppIdB);
                subOpportunityBufferMapper.delete(qw1);

                QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
                qw2.eq("compb_opp_id", oppIdB);
                competitorBufferMapper.delete(qw2);

                QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
                qw3.eq("pb_opp_id", oppIdB);
                payerBufferMapper.delete(qw3);

                QueryWrapper<Approvallog> qw4 = Wrappers.query();
                qw4.eq("app_opp_id", oppIdB);
                approvallogMapper.delete(qw4);

                opportunityBufferMapper.deleteById(oppIdB);
                msg = "已拒绝机会！";
            }

            if (approveResult.equals("0")){
                // 退回
                // 判断是新增机会还是修改机会
                OpportunityBuffer opb = opportunityBufferMapper.selectById(oppIdB);
                Approvallog approvallog = new Approvallog();
                if (opb.getOppbOppId() == null){
                    // 新增机会，需要将机会审批状态设为0
                    opb.setOppbApproveStatus("0");
                    approvallog.setAppFlowName("机会新增流程");
                    approvallog.setAppStatus("复审");
                }else {
                    approvallog.setAppFlowName("机会修改流程");
                    approvallog.setAppStatus("初始");
                }
                // 修改缓存机会表中该机会的状态为60(退回状态)，并新增审批日志
                opb.setOppbStatus("60");
                opportunityBufferMapper.updateById(opb);
                approvallog.setAppPeople(empName);
                approvallog.setAppOpinion(approveAdvice);
                approvallog.setAppResult("退回");
                approvallog.setAppSubmitDate(opb.getOppbSubmitDate());
                approvallog.setAppOppId(oppIdB);
                // 写入审批日志表
                approvallogMapper.insert(approvallog);
                msg = "已退回机会！";
            }

            if (approveResult.equals("1")){
                // 同意
                // 判断是新增机会还是修改机会
                OpportunityBuffer opb = opportunityBufferMapper.selectById(oppIdB);
                if (opb.getOppbOppId() == null){
                    // 新增机会
                    // 实例化4个表的信息
                    Opportunity opp = new Opportunity();
                    List<SubOpportunity> subOpportunityList = new ArrayList<>();
                    List<Competitor> competitorList = new ArrayList<>();
                    List<Payer> payerList = new ArrayList<>();

                    // 1. 生成机会id，并将机会信息从缓存表转移到普通表中
                    String max_opp_id = opportunityMapper.getMaxOppId();
                    String new_opp_id = (Integer.parseInt(max_opp_id)+1) + "";

                    opp.setOppId(new_opp_id);
                    opp.setOppName(opb.getOppbName());
                    opp.setOppSalesDept(opb.getOppbSalesDept());
                    opp.setOppCustomerManagerId(opb.getOppbCustomerManagerId());
                    opp.setOppSignTime(opb.getOppbSignTime());
                    opp.setOppBelonging(opb.getOppbBelonging());
                    opp.setOppStatus("30");
                    opp.setOppPhase(opb.getOppbPhase());
                    opp.setOppType(opb.getOppbType());
                    opp.setOppProduct(opb.getOppbProduct());
                    opp.setOppBackground(opb.getOppbBackground());
                    opp.setOppCigarettes(opb.getOppbCigarettes());
                    opp.setOppCusId(opb.getOppbCusId());
                    opp.setOppOrigin(opb.getOppbOrigin());

                    // 2. 生成子机会id，并将子机会信息从缓存表转移到普通表中
                    QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
                    qw1.eq("sub_oppb_opp_id", oppIdB);
                    List<SubOpportunityBuffer> subOpportunityBufferList = subOpportunityBufferMapper.selectList(qw1);
                    for (int i = 0; i < subOpportunityBufferList.size(); i++) {
                        SubOpportunity s = new SubOpportunity();
                        if (i < 10){
                            s.setSubOppId(new_opp_id+"0"+i);
                        }else {
                            s.setSubOppId(new_opp_id+i);
                        }
                        s.setSubOppName(subOpportunityBufferList.get(i).getSubOppbName());
                        s.setSubOppType(subOpportunityBufferList.get(i).getSubOppbType());
                        s.setSubOppProduct(subOpportunityBufferList.get(i).getSubOppbProduct());
                        s.setSubOppDept(subOpportunityBufferList.get(i).getSubOppbDept());
                        s.setSubOppCurrency(subOpportunityBufferList.get(i).getSubOppbCurrency());
                        s.setSubOppMoney(subOpportunityBufferList.get(i).getSubOppbMoney());
                        s.setSubOppOppId(new_opp_id);
                        s.setSubOppStatus("30");
                        subOpportunityList.add(s);
                    }
                    // 3. 将竞争情况信息从缓存表转移到普通表中
                    QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
                    qw2.eq("compb_opp_id", oppIdB);
                    List<CompetitorBuffer> competitorBufferList = competitorBufferMapper.selectList(qw2);
                    for (CompetitorBuffer c : competitorBufferList) {
                        Competitor competitor = new Competitor();
                        competitor.setCompName(c.getCompbName());
                        competitor.setCompOppId(new_opp_id);
                        competitor.setCompPosition(c.getCompbPosition());
                        competitorList.add(competitor);
                    }
                    // 4. 将购买决策人信息从缓存表转移到普通表中
                    QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
                    qw3.eq("pb_opp_id", oppIdB);
                    List<PayerBuffer> payerBufferList = payerBufferMapper.selectList(qw3);
                    for (PayerBuffer p : payerBufferList) {
                        Payer payer = new Payer();
                        payer.setpName(p.getPbName());
                        payer.setpDept(p.getPbDept());
                        payer.setpPosition(p.getPbPosition());
                        payer.setpPhone(p.getPbPhone());
                        payer.setpDecision(p.getPbDecision());
                        payer.setpEffect(p.getPbEffect());
                        payer.setpAgree(p.getPbAgree());
                        payer.setpOppId(new_opp_id);
                        payerList.add(payer);
                    }

                    // insert到四张表
                    opportunityMapper.insert(opp);
                    for (SubOpportunity s : subOpportunityList) {
                        subOpportunityMapper.insert(s);
                    }
                    for (Competitor c : competitorList) {
                        competitorMapper.insert(c);
                    }
                    for (Payer p : payerList) {
                        payerMapper.insert(p);
                    }

                    // 删除缓存表中的对应数据
                    subOpportunityBufferMapper.delete(qw1);
                    competitorBufferMapper.delete(qw2);
                    payerBufferMapper.delete(qw3);
                    QueryWrapper<Approvallog> qw4 = Wrappers.query();
                    qw4.eq("app_opp_id", oppIdB);
                    approvallogMapper.delete(qw4);
                    opportunityBufferMapper.deleteById(oppIdB);
                }

                else {
                    // 修改机会
                    // update 机会表
                    Opportunity updatedOpp = new Opportunity();
                    List<SubOpportunity> addSubOppList = new ArrayList<>();
                    List<SubOpportunity> updateSubOppList = new ArrayList<>();
                    List<Competitor> addCompetitorList = new ArrayList<>();
                    List<Payer> addPayerList = new ArrayList<>();

                    updatedOpp.setOppId(opb.getOppbOppId());
                    updatedOpp.setOppName(opb.getOppbName());
                    updatedOpp.setOppSalesDept(opb.getOppbSalesDept());
                    updatedOpp.setOppCustomerManagerId(opb.getOppbCustomerManagerId());
                    updatedOpp.setOppSignTime(opb.getOppbSignTime());
                    updatedOpp.setOppBelonging(opb.getOppbBelonging());
                    // 将机会状态重新设为“正常”
                    updatedOpp.setOppStatus("30");
                    updatedOpp.setOppPhase(opb.getOppbPhase());
                    updatedOpp.setOppType(opb.getOppbType());
                    updatedOpp.setOppProduct(opb.getOppbProduct());
                    updatedOpp.setOppBackground(opb.getOppbBackground());
                    updatedOpp.setOppCigarettes(opb.getOppbCigarettes());
                    updatedOpp.setOppCusId(opb.getOppbCusId());
                    updatedOpp.setOppOrigin(opb.getOppbOrigin());

                    // update 子机会表
                    QueryWrapper<SubOpportunityBuffer> qw1 = Wrappers.query();
                    qw1.eq("sub_oppb_opp_id", oppIdB);
                    List<SubOpportunityBuffer> subOpportunityBufferList = subOpportunityBufferMapper.selectList(qw1);
                    for (SubOpportunityBuffer sb : subOpportunityBufferList) {
                        SubOpportunity s = new SubOpportunity();
                        s.setSubOppName(sb.getSubOppbName());
                        s.setSubOppType(sb.getSubOppbType());
                        s.setSubOppProduct(sb.getSubOppbProduct());
                        s.setSubOppDept(sb.getSubOppbDept());
                        s.setSubOppCurrency(sb.getSubOppbCurrency());
                        s.setSubOppMoney(sb.getSubOppbMoney());
                        s.setSubOppOppId(updatedOpp.getOppId());
                        s.setSubOppStatus("30");
                        // 因为在新增子机会时不能填写状态，状态是在审批通过后才加上的，因此可以用状态是否为null来判断是新增还是修改
                        if (sb.getSubOppbStatus() == null){
                            // 新增子机会
                            addSubOppList.add(s);
                        }
                        else {
                            // 更新子机会
                            QueryWrapper<SubOpportunity> qw_s = Wrappers.query();
                            qw_s.eq("sub_opp_name", s.getSubOppName()).eq("sub_opp_opp_id", s.getSubOppOppId());
                            List<SubOpportunity> sbs = subOpportunityMapper.selectList(qw_s);
                            s.setSubOppId(sbs.get(0).getSubOppId());
                            updateSubOppList.add(s);
                        }
                    }

                    // update 竞争情况表
                    QueryWrapper<CompetitorBuffer> qw2 = Wrappers.query();
                    qw2.eq("compb_opp_id", oppIdB);
                    List<CompetitorBuffer> competitorBufferList = competitorBufferMapper.selectList(qw2);
                    for (CompetitorBuffer c : competitorBufferList) {
                        // 判断是否为新增
                        QueryWrapper<Competitor> qw_c = Wrappers.query();
                        qw_c.eq("comp_name", c.getCompbName()).eq("comp_opp_id", opb.getOppbOppId());
                        int num = competitorMapper.selectCount(qw_c);
                        if (num == 0){
                            Competitor competitor = new Competitor();
                            competitor.setCompName(c.getCompbName());
                            competitor.setCompPosition(c.getCompbPosition());
                            competitor.setCompOppId(updatedOpp.getOppId());
                            addCompetitorList.add(competitor);
                        }
                    }

                    // update 购买决策人表
                    QueryWrapper<PayerBuffer> qw3 = Wrappers.query();
                    qw3.eq("pb_opp_id", oppIdB);
                    List<PayerBuffer> payerBufferList = payerBufferMapper.selectList(qw3);
                    for (PayerBuffer p : payerBufferList) {
                        // 判断是否为新增
                        QueryWrapper<Payer> qw_p = Wrappers.query();
                        qw_p.eq("p_name", p.getPbName()).eq("p_opp_id", opb.getOppbOppId());
                        int num = payerMapper.selectCount(qw_p);
                        if (num == 0){
                            Payer payer = new Payer();
                            payer.setpName(p.getPbName());
                            payer.setpDept(p.getPbDept());
                            payer.setpPosition(p.getPbPosition());
                            payer.setpPhone(p.getPbPhone());
                            payer.setpDecision(p.getPbDecision());
                            payer.setpEffect(p.getPbEffect());
                            payer.setpAgree(p.getPbAgree());
                            payer.setpOppId(updatedOpp.getOppId());
                            addPayerList.add(payer);
                        }
                    }

                    // 更新4张表
                    opportunityMapper.updateById(updatedOpp);
                    for (SubOpportunity subOpportunity : addSubOppList) {
                        subOpportunityMapper.insert(subOpportunity);
                    }
                    for (SubOpportunity s : updateSubOppList) {
                        subOpportunityMapper.updateById(s);
                    }
                    for (Competitor competitor : addCompetitorList) {
                        competitorMapper.insert(competitor);
                    }
                    for (Payer payer : addPayerList) {
                        payerMapper.insert(payer);
                    }

                    // 删除缓存表中的对应数据
                    subOpportunityBufferMapper.delete(qw1);
                    competitorBufferMapper.delete(qw2);
                    payerBufferMapper.delete(qw3);
                    QueryWrapper<Approvallog> qw4 = Wrappers.query();
                    qw4.eq("app_opp_id", oppIdB);
                    approvallogMapper.delete(qw4);
                    opportunityBufferMapper.deleteById(oppIdB);
                }

                msg = "已通过机会！";
            }

        }

        return RespBean.ok(200, msg);
    }


}
