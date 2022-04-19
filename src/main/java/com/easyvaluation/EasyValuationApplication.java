package com.easyvaluation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;

import java.util.Collections;

@SpringBootApplication
public class EasyValuationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyValuationApplication.class, args);
	}

//	@Bean
//	public FilterRegistrationBean filterRegistrationBean(){
//		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
//		filterRegistrationBean.setFilter(new JwtFilter());
//		filterRegistrationBean.setUrlPatterns(Collections.singleton("/projects"));
//		return filterRegistrationBean;
//	}
}
