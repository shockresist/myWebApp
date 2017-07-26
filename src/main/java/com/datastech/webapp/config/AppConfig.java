/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.config;

import com.datastech.webapp.commons.context.HeaderLocaleResolver;
import com.datastech.webapp.config.rs.RsConfig;
import com.datastech.webapp.config.service.ServiceConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;

/**
 *
 * @author dbabich
 */



@Configuration
@Import({RsConfig.class,ServiceConfig.class})
public class AppConfig {
    
    @Bean
    public LocaleResolver localeResolver() {
        return new HeaderLocaleResolver();
    }
    
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.addBasenames("labels");
        return bean;
    }
    
    
}
