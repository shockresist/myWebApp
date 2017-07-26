/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.commons.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;

/**
 *
 * @author dbabich
 */
@JsonTypeName("userGreeting")
public class UserGreetingDTO implements Serializable{
    private String username;
    private String greeting;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGreeting() {
        return greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }
    
    
}
