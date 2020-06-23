package com.example.demo;

import my.app.filter.UserLoginFilter;
import my.app.task.DailyTaskManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("my.app")
@MapperScan("my.app.mapper")
public class DemoApplication
{

    //加入登录检测
    @Bean
    public FilterRegistrationBean setUserLoginFilter()
    {

        FilterRegistrationBean filterBean = new FilterRegistrationBean();
        filterBean.setFilter(new UserLoginFilter());
        filterBean.setName("UserLoginFilter");
        filterBean.addUrlPatterns("/u/*");

        return filterBean;
    }

    //初始化定时任务
    @Bean(initMethod = "init" , destroyMethod = "doQuit")
    DailyTaskManager beanWayService()
    {
        return new DailyTaskManager();
    }

    public static void main(String[] args)
    {
        SpringApplication.run(DemoApplication.class, args);
    }

}
