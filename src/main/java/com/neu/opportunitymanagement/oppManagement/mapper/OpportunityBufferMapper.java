package com.neu.opportunitymanagement.oppManagement.mapper;

import com.neu.opportunitymanagement.oppManagement.dto.approval.Flow;
import com.neu.opportunitymanagement.oppManagement.entity.OpportunityBuffer;
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
public interface OpportunityBufferMapper extends BaseMapper<OpportunityBuffer> {

    // 查询获取待审批的机会
    public List<Flow> getApproveOppList(String empPositionId);


}
