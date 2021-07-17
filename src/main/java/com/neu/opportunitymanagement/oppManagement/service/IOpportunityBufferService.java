package com.neu.opportunitymanagement.oppManagement.service;

import com.neu.opportunitymanagement.oppManagement.dto.common.RespBean;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.AddOpportunityInfo;
import com.neu.opportunitymanagement.oppManagement.dto.opportunity.UpdateOpportunityInfo;
import com.neu.opportunitymanagement.oppManagement.entity.OpportunityBuffer;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xsb
 * @since 2021-07-15
 */
public interface IOpportunityBufferService extends IService<OpportunityBuffer> {

    // 判断新增机会时机会名称是否重复
    public RespBean testAddRepetition(String oppbName, String cusId);

    // 机会新增
    public RespBean addOpportunity(AddOpportunityInfo addOpportunityInfo);

    // 判断修改机会时机会名称是否重复
    public RespBean testUpdateRepetition(String oppbName, String cusId, String oppbId);

    // 机会修改
    public RespBean updateOpportunity(UpdateOpportunityInfo updateOpportunityInfo);


}
