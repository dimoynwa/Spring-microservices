package io.dimo.user.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import io.micrometer.context.ContextSnapshot;
import io.micrometer.observation.contextpropagation.ObservationThreadLocalAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
public class LoggingFilter implements WebFilter {
	
	public static final String TRACEPARENT = "traceparent";
	
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    	return Mono.deferContextual(contextView -> {
            try (ContextSnapshot.Scope scope = ContextSnapshot.setThreadLocalsFrom(contextView,
                    ObservationThreadLocalAccessor.KEY)) {
            	log.info("Request: {} {}, Headers: {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI(),
                		exchange.getRequest().getHeaders());
            	
            	ServerHttpResponse response = exchange.getResponse();
                HttpHeaders headers = response.getHeaders();
                
                return chain.filter(exchange).doFinally(signalType -> {
    				log.info("Response: {} {}", response.getStatusCode(), headers.getContentType());
                }).contextWrite(Context.of(TRACEPARENT, exchange.getRequest().getHeaders().getFirst(TRACEPARENT)));
            }
        });
    }
}