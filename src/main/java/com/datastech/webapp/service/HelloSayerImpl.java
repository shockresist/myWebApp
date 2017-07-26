/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.service;

import com.datastech.webapp.commons.context.CommonRequestContext;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 *
 * @author dbabich
 */
@Service
public class HelloSayerImpl implements HelloSayer{

    @Autowired
    private CommonRequestContext commonRequestContext;
    
    @Autowired
    private MessageSource messageSource;
    
    
    public HelloSayerImpl() {
        System.out.println("HelloSayerImpl created");
    }
    
    @Override
    public String say() {
        return messageSource.getMessage("hello", null, commonRequestContext.getLocale());
    }
    
}
