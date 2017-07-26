/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.config;

import ch.qos.logback.ext.spring.web.LogbackConfigListener;
import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;
import java.util.function.Function;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 *
 * @author dbabich
 */
public class AppInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws javax.servlet.ServletException {
        final AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));
        servletContext.addListener(new RequestContextListener());

        servletContext.setInitParameter(WebLogbackConfigurer.EXPOSE_WEB_APP_ROOT_PARAM, "false");
        servletContext.addListener(new LogbackConfigListener());
    }
    
    public static class OperationDetails{
        public Integer a;
        public Integer b;

        public OperationDetails(Integer a, Integer b) {
            this.a = a;
            this.b = b;
        }
        
    }
    
    public static Integer doOperation(Function<OperationDetails,Integer > operation,OperationDetails od) {
        return operation.apply(od);
    } 
    
    public Integer plus(OperationDetails od) {
        return (od.a + od.b);
    }
    
    public static void main(String[] args) {
        AppInitializer ai = new AppInitializer();
        System.out.println(doOperation(ai::plus, new OperationDetails(2, 3)));
    }
    
}
