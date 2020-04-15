package com.example.bbs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import my.app.filter.BootFilter;
import my.app.filter.UserLoginFilter;

@SpringBootApplication
@ComponentScan("my")
public class BbsApplication {

	//加入登录检测
	@Bean
    public FilterRegistrationBean setUserLoginFilter(){

        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new UserLoginFilter());
        filterBean.setName("UserLoginFilter");
        filterBean.addUrlPatterns("/u/*");
        
        return filterBean;
    }
	
	//加入定期任务
	@Bean
    public FilterRegistrationBean setBootFilter(){

        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new BootFilter());
        filterBean.setName("BootFilter");
        filterBean.addUrlPatterns("/*");
        
        return filterBean;
    }
	
	
	public static void main(String[] args) {
		SpringApplication.run(BbsApplication.class, args);
	}

}
