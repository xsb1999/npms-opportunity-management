package com.neu.opportunitymanagement.config;

import com.neu.opportunitymanagement.oppManagement.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {



    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
//                // 客户新增（权限：客户经理，销售总监）
//                .antMatchers(HttpMethod.POST,"/cusManagement/customer/addCustomers").hasAnyAuthority("30000010")
//                .antMatchers(HttpMethod.POST,"/cusManagement/customer/addCustomers").hasAnyAuthority("30000030")
//                // 客户修改（权限：客户经理，销售总监，营销专员）
//                .antMatchers(HttpMethod.POST,"/cusManagement/customer/updateCustomers").hasAnyAuthority("30000010")
//                .antMatchers(HttpMethod.POST,"/cusManagement/customer/updateCustomers").hasAnyAuthority("30000030")
////                .antMatchers(HttpMethod.POST,"/cusmanagement/customer/updateCustomers").hasAnyAuthority("20000020")
//                // 客户冻结（权限：营销专员）
//                .antMatchers(HttpMethod.POST,"/cusManagement/customer/frozenCustomer").hasAnyAuthority("20000020")
//                // 导出客户信息（权限：营销副总，营销专员）
//                .antMatchers(HttpMethod.GET,"/cusManagement/customer/export").hasAnyAuthority("20000010")
//                .antMatchers(HttpMethod.GET,"/cusManagement/customer/export").hasAnyAuthority("20000020")
//
//                // 客户移交（权限：营销专员）
//                .antMatchers(HttpMethod.GET,"/cusManagement/handoverlog/**").hasAnyAuthority("20000020")
//                .antMatchers(HttpMethod.POST,"/cusManagement/handoverlog/**").hasAnyAuthority("20000020")

                .antMatchers(
                        HttpMethod.GET,
                        "/**/*.html",
                        "/**/*.css",
                        "/",
//                        测试用
                        "/*.jpg",
                        "/image/*.jpg",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.js"
                ).permitAll()
                .antMatchers("/oppManagement/login").permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutUrl("/oppManagement/logout")
                .and()
                .csrf().disable();
        ;


        http.addFilterAt(customJSONLoginFilter(), UsernamePasswordAuthenticationFilter.class);


    }


    @Autowired
    IEmployeeService iEmployeeService;

    public CustomJsonLoginFilter customJSONLoginFilter(){
        CustomJsonLoginFilter customJsonLoginFilte=    new CustomJsonLoginFilter("/oppManagement/login",iEmployeeService);
        customJsonLoginFilte.setAuthenticationSuccessHandler(new MyAuthenticationSuccessHandler());
        customJsonLoginFilte.setAuthenticationFailureHandler(new MyAuthenticationFailureHandler());
        return customJsonLoginFilte;
    }


    @Bean
    public static PasswordEncoder passwordEncoder(){

        String encoderType=EncryptionAlgorithm.ENCODER_TYPE.get(1);
        PasswordEncoder passwordEncode= EncryptionAlgorithm.ENCODER_MAP.get(encoderType);
        return passwordEncode;

//        return new BCryptPasswordEncoder();

    }


}

