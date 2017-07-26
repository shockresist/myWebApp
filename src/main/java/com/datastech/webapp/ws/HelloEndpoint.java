/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.ws;

import org.springframework.ws.server.endpoint.annotation.Endpoint;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author dbabich
 */
@Endpoint
public class HelloEndpoint {


    public static void main(String[] args) {
        String source = "Ваш платеж выдан[[НАЗВАНИЕ ТОБО]]";
        Pattern pattern = Pattern.compile("\\[([^\\[\\]]*?)\\]");
        Matcher matcher = pattern.matcher(source);
        while(matcher.find()){
            System.out.println(matcher.group());
        }

        String myString = "МАСЛО жожоба жеминь жебао";
        myString.chars().forEach(intChr -> {
            System.out.println((char)intChr);
        });


    }
}
