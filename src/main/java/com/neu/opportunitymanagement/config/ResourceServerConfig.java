package com.neu.opportunitymanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /**
     * token服务配置
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenServices(tokenServices());
    }

    /**
     * 路由安全认证配置
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 机会新增（权限：客户经理，销售总监）
                .antMatchers(HttpMethod.POST,"/oppManagement/opportunity/addOpportunity").hasAnyRole("30000010","30000030")
                // 显示机会修改页面（权限：客户经理，销售总监）
                .antMatchers(HttpMethod.POST,"/oppManagement/opportunity/showUpdatePage").hasAnyRole("30000010","30000030")
                // 客户修改（权限：客户经理，销售总监）
                .antMatchers(HttpMethod.POST,"/oppManagement/opportunity/updateOpportunity").hasAnyRole("30000010","30000030")
                // 机会跟踪页面初始化（权限：客户经理，销售总监）
                .antMatchers(HttpMethod.GET,"/oppManagement/opportunity/getOppTrackMainPage").hasAnyRole("30000010","30000030")
                // 机会跟踪（权限：客户经理，销售总监）
                .antMatchers(HttpMethod.POST,"/oppManagement/opportunity/curdTrackinglog").hasAnyRole("30000010","30000030")
                // 机会审批页面初始化（权限：销售总监、事业部总经理、营销副总）
                .antMatchers(HttpMethod.GET,"/oppManagement/opportunity/getApprovalPage").hasAnyRole("30000010","10000030","20000010")
                // 点击“审批”按钮，显示机会审批信息（权限：销售总监、事业部总经理、营销副总）
                .antMatchers(HttpMethod.GET,"/oppManagement/opportunity/showOppApproveDetail").hasAnyRole("30000010","10000030","20000010")
                // 提交审批意见（权限：销售总监、事业部总经理、营销副总）
                .antMatchers(HttpMethod.POST,"/oppManagement/opportunity/approval").hasAnyRole("30000010","10000030","20000010")

                .anyRequest().authenticated();
    }

    /**
     * jwt token 校验解析器
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    /**
     * Token转换器必须与认证服务一致
     */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
        accessTokenConverter.setSigningKey("YoCiyy");
        return accessTokenConverter;
    }

    /**
     * 资源服务令牌解析服务
     */
    @Bean
    @Primary
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setCheckTokenEndpointUrl("http://localhost:15000/oauth/check_token");
        remoteTokenServices.setClientId("client_1");
        remoteTokenServices.setClientSecret("123456");
        return remoteTokenServices;
    }
}

