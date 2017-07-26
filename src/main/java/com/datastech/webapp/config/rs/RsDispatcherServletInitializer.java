/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.config.rs;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.Filter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CompositeFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.FrameworkServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * @author dbabich
 */
public class RsDispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer
implements WebApplicationInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{RsConfig.class};
    }

    @Override
    protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        FrameworkServlet frameworkServlet = super.createDispatcherServlet(servletAppContext);
        frameworkServlet.setThreadContextInheritable(true);
        return frameworkServlet;
    }
    
    @Override
    protected Filter[] getServletFilters() {
        List<Filter> filters = new ArrayList<>();
        filters.add(new DelegatingFilterProxy("commonApplicationFilter"));
        CompositeFilter orderedFilters = new CompositeFilter();
        orderedFilters.setFilters(filters);
        return new Filter[] { orderedFilters };
    }
    
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
    
    @Override
    protected String getServletName() {
        return "rsDispatcher";
    }
    
}
