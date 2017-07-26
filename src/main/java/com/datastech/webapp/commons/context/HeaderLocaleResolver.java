/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastech.webapp.commons.context;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

/**
 *
 * @author dbabich
 */

public class HeaderLocaleResolver extends AcceptHeaderLocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
            final Locale locale;
            if (StringUtils.isEmpty(request.getHeader("Accept-Language"))) {
            return Locale.getDefault();
            }
            locale = StringUtils.parseLocaleString(request.getHeader("Accept-Language"));
            return locale ;
    }
}
