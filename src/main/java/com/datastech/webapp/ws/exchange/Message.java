/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.exchange;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author admin
 */

@JsonRootName("messageResponse")
public class Message {
    private String value;

    public Message(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    
}
