package com.neu.opportunitymanagement.oppManagement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.AddOpportunityInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.UpdateOpportunityInfo;
import com.neu.opportunitymanagement.oppManagement.entity.*;
import com.neu.opportunitymanagement.oppManagement.mapper.*;
import com.neu.opportunitymanagement.oppManagement.service.IOpportunityBufferService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
public class OpportunityBufferServiceImpl extends ServiceImpl<OpportunityBufferMapper, OpportunityBuffer> implements IOpportunityBufferService {

    @Autowired
    OpportunityBufferMapper opportunityBufferMapper;
    @Autowired
    SubOpportunityBufferMapper subOpportunityBufferMapper;
    @Autowired
    CompetitorBufferMapper competitorBufferMapper;
    @Autowired
    PayerBufferMapper payerBufferMapper;
    @Autowired
    OpportunityMapper opportunityMapper;
    @Autowired
    IOpportunityBufferService iOpportunityBufferService;
    @Autowired
    SubOpportunityMapper subOpportunityMapper;
    @Autowired
    CompetitorMapper competitorMapper;
    @Autowired
    PayerMapper payerMapper;


    @Override
    public RespBean testAddRepetition(String oppbName, String cusId) {
        RespBean respBean = null;
        // 同一个客户的机会名称不能有重复
        QueryWrapper<OpportunityBuffer> qw2 = Wrappers.query();
        qw2.eq("oppb_name", oppbName).eq("oppb_cus_id", cusId);
        List<OpportunityBuffer> list1 = opportunityBufferMapper.selectList(qw2);
        QueryWrapper<Opportunity> qw3 = Wrappers.query();
        qw3.eq("opp_name", oppbName).eq("opp_cus_id", cusId);
        List<Opportunity> list2 = opportunityMapper.selectList(qw3);

        // 这个客户已经存在该机会名称了，新增失败
        if (!list1.isEmpty() || !list2.isEmpty()){
            System.out.println("机会名称重复！");
            respBean = RespBean.error(500, "机会名称重复！");
        }else {
            respBean = RespBean.ok(200, "ok");
        }
        return respBean;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public RespBean addOpportunity(AddOpportunityInfo addOpportunityInfo) {
        // 在新增机会后，点击保存按钮，将信息存入4张缓存表
        OpportunityBuffer opportunityBuffer = addOpportunityInfo.getOpportunityBuffer();
        List<SubOpportunityBuffer> subOpportunityBufferList = addOpportunityInfo.getSubOpportunityBufferList();
        List<CompetitorBuffer> competitorBufferList = addOpportunityInfo.getCompetitorBufferList();
        List<PayerBuffer> payerBufferList = addOpportunityInfo.getPayerBufferList();
        RespBean respBean = null;
        String msg = "";

        // 这是新增的机会
        if (opportunityBuffer.getOppbOppId() == null){
            System.out.println("新增的机会");
//            // 同一个客户的机会名称不能有重复
//            QueryWrapper<OpportunityBuffer> qw2 = Wrappers.query();
//            qw2.eq("oppb_name", opportunityBuffer.getOppbName()).eq("oppb_cus_id", opportunityBuffer.getOppbCusId());
//            List<OpportunityBuffer> list1 = opportunityBufferMapper.selectList(qw2);
//            QueryWrapper<Opportunity> qw3 = Wrappers.query();
//            qw3.eq("opp_name", opportunityBuffer.getOppbName()).eq("opp_cus_id", opportunityBuffer.getOppbCusId());
//            List<Opportunity> list2 = opportunityMapper.selectList(qw3);
//
//            // 这个客户已经存在该机会名称了，新增失败
//            if (!list1.isEmpty() || !list2.isEmpty()){
//                System.out.println("机会名称重复！");
//                respBean = RespBean.error(500, "机会名称重复！");
//                return respBean;
//            }
            // 判断是保存了还是提交了
            if (addOpportunityInfo.getType().equals("0")){
                // 将机会状态设为"10"（草稿）
                opportunityBuffer.setOppbStatus("10");
            }else {
                // 将机会状态设为“20”（流程中）
                opportunityBuffer.setOppbStatus("20");
            }
            // 将机会审批状态设为"0"（0人审批通过）
            opportunityBuffer.setOppbApproveStatus("0");
        }else {
            opportunityBuffer.setOppbStatus("20");
            // 将机会审批状态设为"1"（修改机会时不需要营销副总审批，因此直接设为1）
            opportunityBuffer.setOppbApproveStatus("1");
        }

        // 因为在修改机会时，如果需要审批，则需要将信息写入缓存表，就会调用addOpportunity这个方法

        // 设置提交日期
        Date date = new Date();
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(date);
        opportunityBuffer.setOppbSubmitDate(today);

        // insert机会
        opportunityBufferMapper.insert(opportunityBuffer);

        // 获取刚刚新增到缓存表中的机会id
        QueryWrapper<OpportunityBuffer> qw1 = Wrappers.query();
        qw1.eq("oppb_name", opportunityBuffer.getOppbName()).eq("oppb_cus_id", opportunityBuffer.getOppbCusId());
        OpportunityBuffer opportunityBuffer1 = opportunityBufferMapper.selectOne(qw1);
        int oppb_id = opportunityBuffer1.getOppbId();

        // 设置子机会的对应机会id
        for (SubOpportunityBuffer sob : subOpportunityBufferList) {
            sob.setSubOppbOppId(oppb_id);
        }
        // 设置竞争关系的对应机会id
        for (CompetitorBuffer cb : competitorBufferList) {
            cb.setCompbOppId(oppb_id);
        }
        // 设置购买决策人的对应机会id
        for (PayerBuffer pb : payerBufferList) {
            pb.setPbOppId(oppb_id);
        }

        // insert到3张表
        try {
            // insert 子机会缓存表
            for (SubOpportunityBuffer sob : subOpportunityBufferList) {
                subOpportunityBufferMapper.insert(sob);
            }
            // insert 竞争关系缓存表
            for (CompetitorBuffer cb : competitorBufferList) {
                competitorBufferMapper.insert(cb);
            }
            // insert 购买决策人缓存表
            for (PayerBuffer pb : payerBufferList) {
                payerBufferMapper.insert(pb);
            }
        }catch (Exception e){
            System.out.println("insert fail!");
            throw e;
        }
        System.out.println("insert success!");
        if (opportunityBuffer.getOppbOppId() == null){
            if (addOpportunityInfo.getType().equals("0")){
                msg = "机会新增保存草稿成功！";
            }else {
                msg = "机会新增已提交，请等待审批通过！";
            }
        }else {
            msg = "机会修改已提交，请等待审批通过！";
        }

        respBean = RespBean.ok(200, msg);

        return respBean;
    }


    @Override
    public RespBean testUpdateRepetition(String oppbName, String cusId, String oppbId) {
        RespBean respBean = null;
        // 机会名称不能重复
        QueryWrapper<OpportunityBuffer> qw2 = Wrappers.query();
        qw2.eq("oppb_name", oppbName).eq("oppb_cus_id", cusId);
        List<OpportunityBuffer> list1 = opportunityBufferMapper.selectList(qw2);
        QueryWrapper<Opportunity> qw3 = Wrappers.query();
        qw3.eq("opp_name", oppbName).eq("opp_cus_id", cusId);
        List<Opportunity> list2 = opportunityMapper.selectList(qw3);
        OpportunityBuffer op = opportunityBufferMapper.selectById(oppbId);

        // 这个客户已经存在该机会名称了，新增失败
        boolean f0 = oppbName.equals(op.getOppbName());
        if (!f0){
            if (!list1.isEmpty() || !list2.isEmpty()){

                System.out.println("机会名称重复！");
                respBean = RespBean.error(500, "机会名称重复！");
                return respBean;
            }
        }
        respBean = RespBean.error(200, "ok");
        return respBean;
    }

    @Override
    @Transactional(rollbackFor=Exception.class)
    public RespBean updateOpportunity(UpdateOpportunityInfo updateOpportunityInfo) {
        // 保存 or 提交
        String type = updateOpportunityInfo.getType();
        // 普通表信息
        Opportunity opportunity = updateOpportunityInfo.getOpportunity();
        List<SubOpportunity> updateSubOpportunityList = updateOpportunityInfo.getUpdateSubOpportunityList();
        List<Competitor> updateCompetitorList = updateOpportunityInfo.getUpdateCompetitorList();
        List<Payer> updatePayerList = updateOpportunityInfo.getUpdatePayerList();
        // 缓存表信息
        OpportunityBuffer opportunityBuffer = updateOpportunityInfo.getOpportunityBuffer();
        List<SubOpportunityBuffer> subOpportunityBufferList = updateOpportunityInfo.getSubOpportunityBufferList();
        List<CompetitorBuffer> competitorBufferList = updateOpportunityInfo.getCompetitorBufferList();
        List<PayerBuffer> payerBufferList = updateOpportunityInfo.getPayerBufferList();

        RespBean respBean = null;
        String msg = "";
        opportunityBuffer.setOppbOppId(opportunity.getOppId());

        /**
         *机会修改：
         *  判断修改机会的状态（草稿，退回，正常）
         *  对于草稿和退回这两种机会，因为这时机会还没有审批通过，因此修改后需要更新到缓存表中
         *  草稿修改后仍可以进行保存，而退回的机会在修改后只能提交
         *  对于正常状态的机会，还需要判断是否修改了机会归属或者机会阶段，以此来判断是否需要进行审批
         *  如果需要审批，则将这些修改信息insert到缓存表中，反之则可以直接update机会表
         */

        // 草稿或退回的机会
        if (opportunity.getOppId() == null){

            // 判断是草稿还是退回的机会
            // 草稿
            OpportunityBuffer ob = opportunityBufferMapper.selectById(opportunityBuffer.getOppbId());
            String oppStatus = ob.getOppbStatus();
            if (oppStatus.equals("20")){
                return RespBean.error(500, "该机会状态为流程中，不可修改！");
            }

            if (oppStatus.equals("10")){
//                // 机会名称不能重复
//                QueryWrapper<OpportunityBuffer> qw2 = Wrappers.query();
//                qw2.eq("oppb_name", opportunityBuffer.getOppbName()).eq("oppb_cus_id", opportunityBuffer.getOppbCusId());
//                List<OpportunityBuffer> list1 = opportunityBufferMapper.selectList(qw2);
//                QueryWrapper<Opportunity> qw3 = Wrappers.query();
//                qw3.eq("opp_name", opportunityBuffer.getOppbName()).eq("opp_cus_id", opportunityBuffer.getOppbCusId());
//                List<Opportunity> list2 = opportunityMapper.selectList(qw3);
//                OpportunityBuffer op = opportunityBufferMapper.selectById(opportunityBuffer.getOppbId());
//
//                // 这个客户已经存在该机会名称了，新增失败
//                boolean f0 = opportunityBuffer.getOppbName().equals(op.getOppbName());
//                if (!f0){
//                    if (!list1.isEmpty() || !list2.isEmpty()){
//
//                        System.out.println("机会名称重复！");
//                        respBean = RespBean.error(500, "机会名称重复！");
//                        return respBean;
//                    }
//                }

                // 提交
                if (type.equals("1")) {
                    opportunityBuffer.setOppbStatus("20");
                    msg = "机会修改已提交，请等待审批通过！";
                }else {
                    msg = "机会保存草稿成功！";
                }
            }
            // 退回
            else {
                opportunityBuffer.setOppbStatus("20");
                msg = "机会修改已提交，请等待审批通过！";
            }

            try {
                // 设置提交日期
                Date date = new Date();
                SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd");
                String today = dateFormat.format(date);
                opportunityBuffer.setOppbSubmitDate(today);
                // 更新缓存机会表
                opportunityBufferMapper.updateById(opportunityBuffer);

                // 更新缓存子机会表
                for (SubOpportunityBuffer s : subOpportunityBufferList) {
                    // 新增
                    if (s.getSubOppbId() == null){
                        s.setSubOppbOppId(opportunityBuffer.getOppbId());
                        subOpportunityBufferMapper.insert(s);
                    }
                    // 修改
                    else {
                        subOpportunityBufferMapper.updateById(s);
                    }
                }

                // 更新缓存竞争情况表
                for (CompetitorBuffer c : competitorBufferList) {
                    // 新增
                    if (c.getCompbId() == null){
                        c.setCompbOppId(opportunityBuffer.getOppbId());
                        competitorBufferMapper.insert(c);
                    }
                }

                // 更新缓存竞争情况表
                for (PayerBuffer p : payerBufferList) {
                    // 新增
                    if (p.getPbId() == null){
                        p.setPbOppId(opportunityBuffer.getOppbId());
                        payerBufferMapper.insert(p);
                    }
                }
            }catch (Exception e){
                System.out.println("update fail!");
                throw e;
            }

            respBean = RespBean.ok(200, msg);

        }


        // 正常机会
        else {
            // 判断本次修改是否需要审批
            // 逆向修改机会阶段需要进入审批流程, 机会归属修改后要审批
            // 获取原始的机会阶段和机会归属
            String[] oppPhases = {"E","D","C","B","A","S"};
            List<String> oppPhaseList = Arrays.asList(oppPhases);
            Opportunity testOpp = opportunityMapper.selectById(opportunity.getOppId());
            if (testOpp.getOppStatus().equals("20")){
                return RespBean.error(500, "该机会状态为流程中，不可修改！");
            }
            if (testOpp.getOppStatus().equals("40")){
                return RespBean.error(500, "该机会状态为暂停，不可修改！");
            }
            if (testOpp.getOppStatus().equals("50")){
                return RespBean.error(500, "该机会状态为关闭，不可修改！");
            }

            if (!testOpp.getOppBelonging().equals(opportunity.getOppBelonging()) || oppPhaseList.indexOf(testOpp.getOppPhase()) > oppPhaseList.indexOf(opportunity.getOppPhase())){
                System.out.println("需要审批");
                // 需要审批
                // 先修改普通机会表中的机会状态为“流程中，再insert所有信息到缓存表
                Opportunity opp = new Opportunity();
                opp.setOppId(opportunity.getOppId());
                opp.setOppStatus("20");
                opportunityMapper.updateById(opp);
                AddOpportunityInfo addOpportunityInfo = new AddOpportunityInfo();
                opportunityBuffer.setOppbOppId(opportunity.getOppId());
                addOpportunityInfo.setOpportunityBuffer(opportunityBuffer);
                addOpportunityInfo.setSubOpportunityBufferList(subOpportunityBufferList);
                addOpportunityInfo.setCompetitorBufferList(competitorBufferList);
                addOpportunityInfo.setPayerBufferList(payerBufferList);
                respBean = iOpportunityBufferService.addOpportunity(addOpportunityInfo);
            }
            else {
                System.out.println("不需要审批");
                // 不需要审批
                // 直接update原表
                List<SubOpportunity> addSubOpportunityList = new ArrayList<>();
                List<SubOpportunity> updatedSubOpportunityList = new ArrayList<>();
                List<Competitor> addCompetitorList = new ArrayList<>();
                List<Payer> addPayerList = new ArrayList<>();

                for (SubOpportunity s : updateSubOpportunityList) {
                    if (s.getSubOppId() == null){
                        addSubOpportunityList.add(s);
                    }else {
                        updatedSubOpportunityList.add(s);
                    }
                }
                for (Competitor c : updateCompetitorList) {
                    if (c.getCompId() == null){
                        addCompetitorList.add(c);
                    }
                }
                for (Payer p : updatePayerList) {
                    if (p.getpId() == null){
                        addPayerList.add(p);
                    }
                }

                try{
                    // 更新机会表
                    opportunityMapper.updateById(opportunity);
                    // 新增子机会表
                    for (SubOpportunity s : addSubOpportunityList) {
                        subOpportunityMapper.insert(s);
                    }
                    // 更新子机会表
                    for (SubOpportunity s : updatedSubOpportunityList) {
                        subOpportunityMapper.updateById(s);
                    }
                    // 新增竞争情况表
                    for (Competitor c : addCompetitorList) {
                        competitorMapper.insert(c);
                    }
                    // 新增购买决策人表
                    for (Payer p : addPayerList) {
                        payerMapper.insert(p);
                    }
                }catch (Exception e){
                    System.out.println("update fail!");
                    throw e;
                }

                respBean = RespBean.ok(200,"机会修改成功！");
            }

        }

        return respBean;
    }





}
