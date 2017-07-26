/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.rs;

import com.datastech.webapp.service.HelloSayer;
import com.datastech.webapp.ws.exchange.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dbabich
 */


@RestController
public class MainController {
    
    static final Logger logger = LoggerFactory.getLogger(MainController.class);
    
    @Autowired
    private HelloSayer helloSayer;
    
    @RequestMapping(value = "main", method = RequestMethod.POST)
    public Message getResponse(){
        logger.info("ALLOU YOBA, ETO TI?");
        return new Message("ALLOU!!");
    }
    
    @RequestMapping(value = "hello", method = RequestMethod.POST)
    public Message getHello(){
        return new Message(helloSayer.say());
    }
}
