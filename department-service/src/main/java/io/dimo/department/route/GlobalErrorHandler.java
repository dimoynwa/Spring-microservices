package io.dimo.department.route;

import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dimo.department.exception.DepartmentNotFoundException;
import io.dimo.department.exception.ErrorResource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class GlobalErrorHandler implements ErrorWebExceptionHandler {
	
	private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    	log.error("Error thrown: ", ex);
        ErrorResource error = null;
        HttpStatus status = HttpStatus.OK;
        
        if (ex instanceof DepartmentNotFoundException departmentNotFound) {
            status = HttpStatus.NOT_FOUND;
            error = new ErrorResource("DEPARTMENT_NOT_FOUND", "Department with that id cannot be found");
        } else if (ex instanceof IllegalArgumentException illegalArgumentExceptuion) {
        	status = HttpStatus.BAD_REQUEST;
            error = new ErrorResource("BAD_REQUEST", illegalArgumentExceptuion.getMessage());
        } else {
        	status = HttpStatus.INTERNAL_SERVER_ERROR;
            error = new ErrorResource("GENERAL_ERROR", "Something went wrong");
        }
        
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        byte[] responseBytes = new byte[0];
		try {
			responseBytes = objectMapper.writeValueAsBytes(error);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(responseBytes)));
    }
}
