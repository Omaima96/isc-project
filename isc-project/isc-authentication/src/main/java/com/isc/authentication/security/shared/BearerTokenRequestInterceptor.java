package com.isc.authentication.security.shared;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

@Component
public class BearerTokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            if (Objects.nonNull(requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION))) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION);
                requestTemplate.header(HttpHeaders.AUTHORIZATION, requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION));
            } else if (Objects.nonNull(requestAttributes.getRequest().getSession().getAttribute(HttpHeaders.AUTHORIZATION))) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION);
                requestTemplate.header(HttpHeaders.AUTHORIZATION, (String) requestAttributes.getRequest().getSession().getAttribute(HttpHeaders.AUTHORIZATION));
            }
        }
    }
}
