/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.commons.rs.filter;

import com.datastech.webapp.commons.context.CommonRequestContext;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.LocaleResolver;

/**
 *
 * @author dbabich
 */
@Component("commonApplicationFilter")
public class CommonApplicationFilter extends OncePerRequestFilter {

    @Autowired
    private CommonRequestContext commonRequestContext;
    
    @Autowired
    private LocaleResolver localeResolver;
    
    @Override
    protected void doFilterInternal(HttpServletRequest hsr, HttpServletResponse hsr1, FilterChain fc) throws ServletException, IOException {
        commonRequestContext.setLocale(localeResolver.resolveLocale(hsr));
        fc.doFilter(hsr,hsr1 );
    }
    
}
