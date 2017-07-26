/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.rs;

import com.datastech.webapp.commons.dto.UserGreetingDTO;
import com.datastech.webapp.ws.exchange.HelloRequest;
import com.datastech.webapp.ws.exchange.HelloResponse;
import com.datastech.webapp.ws.exchange.Response;
import org.junit.Test;

/**
 *
 * @author dbabich
 */
public class RequestProcessorTest {
    
    public RequestProcessorTest() {
    }

    @Test
    public void testSomeMethod() {
        HelloRequest request = new HelloRequest();
        request.setBody(new UserGreetingDTO());
        request.getBody().setGreeting("Привет буфет");
        request.getBody().setUsername("Вильгельм");
        
        Response response = new HelloResponse();
        
        Object result = RequestProcessor.forRequest(request)
                .process()
                .collectResponse(response);
        System.out.println(result);
    }
    
}
