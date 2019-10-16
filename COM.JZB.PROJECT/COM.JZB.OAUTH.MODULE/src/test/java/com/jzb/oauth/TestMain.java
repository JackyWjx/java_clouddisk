package com.jzb.oauth;

import io.undertow.security.impl.CachedAuthenticatedSessionMechanism;
import org.springframework.beans.factory.support.SecurityContextProvider;
import org.springframework.boot.actuate.audit.listener.AuditListener;
import org.springframework.boot.autoconfigure.security.servlet.SecurityRequestMatcherProviderAutoConfiguration;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class TestMain {
    public static void main(String[] args) {
//        String url = "http://www.jzb.com/login?username=aaaa&password=bbbbb";
//        String paraStr = url.substring(url.indexOf("?") + 1);
//        String[] paras = paraStr.split("[&]");
//        for (int i = 0, l = paras.length; i < l; i++) {
//            String[] para = paras[i].split("[=]");
//            System.out.println("============>> " + para[0] + "\t" + para[1]);
//        }
//        AntPathRequestMatcher;
//        FilterSecurityInterceptor;
//        AuthenticationProvider
//        ExceptionTranslationFilter
    }
}
