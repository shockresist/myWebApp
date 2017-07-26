/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.exchange;

import com.datastech.webapp.commons.dto.UserGreetingDTO;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 *
 * @author dbabich
 */
@JsonRootName("helloRequest")
public class HelloRequest extends Request<UserGreetingDTO> {
    
}
