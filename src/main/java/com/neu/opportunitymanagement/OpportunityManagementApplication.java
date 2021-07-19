package com.neu.opportunitymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableEurekaClient
public class OpportunityManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpportunityManagementApplication.class, args);
    }

}
