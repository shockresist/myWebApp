/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws.rs;

import com.datastech.webapp.commons.dto.HelloMessageDTO;
import com.datastech.webapp.ws.exchange.HelloResponse;
import com.datastech.webapp.ws.exchange.Request;
import com.datastech.webapp.ws.exchange.Response;

/**
 *
 * @author dbabich
 */
public final class RequestProcessor<IN> {
    private final Request<IN> request;

    public static RequestProcessor forRequest(Request request){
        return new RequestProcessor(request);
    }
    
    private RequestProcessor(Request request) {
        this.request = request;
    }
    
    public ProcessResult process(){
        return new ProcessResult(request);
    }
    
    public final class ProcessResult<OUT> {
        private final OUT result;

        public ProcessResult(OUT result) {
            this.result = result;
        }
        
        public <R> R collectResponse(R response){
            return response;
        } 
    }
    
}
