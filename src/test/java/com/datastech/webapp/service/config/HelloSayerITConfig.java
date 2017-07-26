/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.service.config;

import com.datastech.webapp.config.service.ServiceConfig;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 *
 * @author dbabich
 */
@Configuration
@Import(ServiceConfig.class)
@Profile("test")
public class HelloSayerITConfig {
    
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource bean = new ResourceBundleMessageSource();
        bean.addBasenames("labels");
        return bean;
    }
}
