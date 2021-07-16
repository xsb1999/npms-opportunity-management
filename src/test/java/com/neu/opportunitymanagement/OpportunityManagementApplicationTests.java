package com.neu.opportunitymanagement;

import com.neu.opportunitymanagement.oppManagement.dto.opportunity.OppSearchCondition;
import com.neu.opportunitymanagement.oppManagement.mapper.OpportunityMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OpportunityManagementApplicationTests {

    @Autowired
    OpportunityMapper opportunityMapper;

    @Test
    void contextLoads() {
    }

    @Test
    public void test1(){
        OppSearchCondition condition = new OppSearchCondition();
        condition.setOppSignTime1("2021-05-10 10:00:00");
        condition.setOppSignTime2("2021-05-15 10:00:00");
        System.out.println(opportunityMapper.getOpportunity(condition));
    }

}
