package io.dimo.department.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.dimo.department.entity.Department;
import io.dimo.department.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class DepartmentRoutesConfiguration {

	private final DepartmentService departmentService;
	
	@Bean
	RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(RequestPredicates.GET("/departments"), this::allDepartments)
            .andRoute(RequestPredicates.GET("/departments/{id}"), this::departmentById)
            .andRoute(RequestPredicates.POST("/departments"), this::createDepartment)
            .andRoute(RequestPredicates.DELETE("/departments/{id}"), this::deleteDepartmentById);
    }
	
	private Mono<ServerResponse> allDepartments(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(departmentService.getAllDepartments(), Department.class);
	}
	
	private Mono<ServerResponse> departmentById(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(departmentService
				.getDepartmentById(serverRequest.pathVariable("id")), Department.class);
	}
	
	public Mono<ServerResponse> createDepartment(ServerRequest request) {
        Mono<Department> departmentMono = request.bodyToMono(Department.class);
        return departmentMono.flatMap(department -> {
        	// Save the object to the database
            Mono<Void> result = departmentService.createDepartment(department);
            // Return NO_CONTENT status if the object is saved successfully
            return ServerResponse.noContent().build(result);
        });
    }
	
	public Mono<ServerResponse> deleteDepartmentById(ServerRequest request) {
		// Delete object
        Mono<Void> result = departmentService.deleteById(request.pathVariable("id"));
        // Return NO_CONTENT status if the object is deleted successfully
        return ServerResponse.noContent().build(result);
    }
}
