package com.rm.connecteducacionalpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.WebFilter;


@Configuration
public class HttpsConfig {
//
//    @Bean
//    public WebFilter httpsRedirectFilter() {
//        return (exchange, chain) -> {
//            ServerHttpRequest request = exchange.getRequest();
//            ServerHttpResponse response = exchange.getResponse();
//            if (!request.getHeaders().containsKey("X-Forwarded-Proto") && "http".equals(request.getURI().getScheme())) {
//                return response.sendRedirect("https://" + request.getURI().getHost() + request.getURI().getPath());
//            }
//            return chain.filter(exchange);
//        };
//    }
}
