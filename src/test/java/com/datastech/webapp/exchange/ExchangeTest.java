/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.exchange;

import com.datastech.webapp.commons.dto.HelloMessageDTO;
import com.datastech.webapp.ws.exchange.HelloResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.stream.IntStream;

/**
 *
 * @author dbabich
 */
public class ExchangeTest {

    private String myVar = "ororor";
    
    public static void main(String[] args) throws JsonProcessingException {
        ExchangeTest cls = new ExchangeTest();
        cls.executeInnerClassMethod();
        ExchangeTest tst = (ExchangeTest) new Object();
    }

    public void executeInnerClassMethod() {
        class MyLocalClass {
            String yoba;
            public void azaza(){

            }
        }

        MyLocalClass localClass = new MyLocalClass();

        MyInnerClass cls = new MyInnerClass();
        cls.printOuterClassVar();
    }


    private class MyInnerClass {


        public void printOuterClassVar(){
            System.out.println(ExchangeTest.this.myVar);
            System.out.println(myVar);
        }

    }


}
