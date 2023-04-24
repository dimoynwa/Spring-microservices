package io.dimo.user.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import io.dimo.user.config.CacheConfig;
import io.dimo.user.model.Department;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class DepartmentService {

	private static final String DEPARTMENT_CB = "departmentCB";
	private static final String DEPARTMENT_URL = "http://DEPARTMENT-SERVICE/departments/";
	
	private final WebClient webClient;
	
	@Cacheable(cacheNames = CacheConfig.DEPARTMENT_CACHE, key = "#departmentId")
	@CircuitBreaker(name = DEPARTMENT_CB)
	@Retry(name = DEPARTMENT_CB)
	public Mono<Department> getDepartment(String departmentId) {
		return callDepartment(departmentId);
    }
	
	
	private Mono<Department> callDepartment(String departmentId) {
		return webClient.get()
	        	.uri(DEPARTMENT_URL + departmentId)
	        	.retrieve()
	        	.onStatus(HttpStatusCode::is4xxClientError, response -> {
	        		log.error("Bad request from department service: ", response);
	        		int status = response.statusCode().value();
	        	    return switch(status) {
	        	      case 404 -> Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(404)));
	        	      default -> Mono.error(new ResponseStatusException(HttpStatusCode.valueOf(400)));
	        	    };
	        	})
	        	.bodyToMono(Department.class);
	}
}
