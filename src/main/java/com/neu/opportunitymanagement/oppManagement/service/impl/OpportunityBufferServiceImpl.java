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

import java.util.ArrayList;
import java.util.Arrays;
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
            // 同一个客户的机会名称不能有重复
            QueryWrapper<OpportunityBuffer> qw2 = Wrappers.query();
            qw2.eq("oppb_name", opportunityBuffer.getOppbName()).eq("oppb_cus_id", opportunityBuffer.getOppbCusId());
            List<OpportunityBuffer> list1 = opportunityBufferMapper.selectList(qw2);
            QueryWrapper<Opportunity> qw3 = Wrappers.query();
            qw3.eq("opp_name", opportunityBuffer.getOppbName()).eq("opp_cus_id", opportunityBuffer.getOppbCusId());
            List<Opportunity> list2 = opportunityMapper.selectList(qw3);

            // 这个客户已经存在该机会名称了，新增失败
            if (!list1.isEmpty() || !list2.isEmpty()){
                System.out.println("机会名称重复！");
                respBean = RespBean.error(500, "机会名称重复！");
                return respBean;
            }
            // 判断是保存了还是提交了
            if (addOpportunityInfo.getType().equals("0")){
                // 将机会状态设为"10"（草稿）
                opportunityBuffer.setOppbStatus("10");
            }else {
                // 将机会状态设为“20”（流程中）
                opportunityBuffer.setOppbStatus("20");
            }
        }else {
            opportunityBuffer.setOppbStatus("20");
        }

        // 因为在修改机会时，如果需要审批，则需要将信息写入缓存表，就会调用addOpportunity这个方法

        // 将机会审批状态设为"0"（0人审批通过）
        opportunityBuffer.setOppbApproveStatus("0");

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
    public RespBean updateOpportunity(UpdateOpportunityInfo updateOpportunityInfo) {
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

        // 判断本次修改是否需要审批
        // 逆向修改机会阶段需要进入审批流程, 机会归属修改后要审批
        // 获取原始的机会阶段和机会归属
        String[] oppPhases = {"E","D","C","B","A","S"};
        List<String> oppPhaseList = Arrays.asList(oppPhases);
        Opportunity testOpp = opportunityMapper.selectById(opportunity.getOppId());
        if (!testOpp.getOppBelonging().equals(opportunity.getOppBelonging()) || oppPhaseList.indexOf(testOpp.getOppPhase()) > oppPhaseList.indexOf(opportunity.getOppPhase())){
            System.out.println("需要审批");
            // 需要审批
            // 直接insert到缓存表
            AddOpportunityInfo addOpportunityInfo = new AddOpportunityInfo();
            addOpportunityInfo.setOpportunityBuffer(opportunityBuffer);
            System.out.println("=========================");
            System.out.println(opportunityBuffer);
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

        return respBean;
    }





}
