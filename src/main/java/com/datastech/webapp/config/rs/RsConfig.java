/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.config.rs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *
 * @author dbabich
 */
@EnableWebMvc
@Configuration
@ComponentScan({"com.datastech.webapp.ws.rs",
                "com.datastech.webapp.commons"})
public class RsConfig extends WebMvcConfigurerAdapter {
    
}
