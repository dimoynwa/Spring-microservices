package io.dimo.user.config;

import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;

import io.dimo.user.filter.LoggingFilter;
import io.github.resilience4j.core.registry.EntryAddedEvent;
import io.github.resilience4j.core.registry.EntryRemovedEvent;
import io.github.resilience4j.core.registry.EntryReplacedEvent;
import io.github.resilience4j.core.registry.RegistryEventConsumer;
import io.github.resilience4j.retry.Retry;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class WebClientConfig {

	@Bean
	WebClient webClient( ReactorLoadBalancerExchangeFilterFunction lbFunction) {
		return WebClient.builder().filter(lbFunction)
				.filter(traceparentExchangeFilterFunction())
			            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			            .build();
	}

	@Bean
	ExchangeFilterFunction traceparentExchangeFilterFunction() {
		return (ClientRequest request, ExchangeFunction next) -> {
			return Mono.deferContextual(contextView -> {
				ClientRequest mutatedRequest = ClientRequest.from(request)
						.headers(headers -> headers.add(LoggingFilter.TRACEPARENT, contextView.getOrDefault(LoggingFilter.TRACEPARENT, null)))
						.build();
				return next.exchange(mutatedRequest);
			});
		};
	}
	
	@Bean
    RegistryEventConsumer<Retry> myRetryRegistryEventConsumer() {

        return new RegistryEventConsumer<Retry>() {
            @Override
            public void onEntryAddedEvent(EntryAddedEvent<Retry> entryAddedEvent) {
                entryAddedEvent.getAddedEntry().getEventPublisher()
                   .onEvent(event -> log.info(event.toString()));
            }

            @Override
            public void onEntryRemovedEvent(EntryRemovedEvent<Retry> entryRemoveEvent) {

            }

            @Override
            public void onEntryReplacedEvent(EntryReplacedEvent<Retry> entryReplacedEvent) {

            }
        };
    }
}
