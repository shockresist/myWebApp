/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.service;

import com.datastech.webapp.commons.context.CommonRequestContext;
import com.datastech.webapp.service.config.HelloSayerITConfig;
import java.util.Locale;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.mockito.Mockito.when;
import org.springframework.util.StringUtils;

/**
 *
 * @author dbabich
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {HelloSayerITConfig.class})
@ActiveProfiles("test")
public class HelloSayerIT {
    
    @Mock
    private CommonRequestContext commonRequestContext;
    
    @Autowired
    @InjectMocks
    private HelloSayer helloSayer;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(commonRequestContext.getLocale()).thenReturn(StringUtils.parseLocaleString("ukr"));
        System.out.println(commonRequestContext);
    }
    
    
    @Test
    public void testSomeMethod() {
        System.out.println(helloSayer.say());
    }
    
}
