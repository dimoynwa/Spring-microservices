package io.dimo.user.router;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import io.dimo.user.model.User;
import io.dimo.user.model.UserDepartment;
import io.dimo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class UserRoutesConfiguration {

	private final UserService userService;
	
	@Bean
	RouterFunction<ServerResponse> routes() {
        return RouterFunctions.route(RequestPredicates.GET("/users"), this::allUsers)
            .andRoute(RequestPredicates.GET("/users/{id}"), this::userById)
            .andRoute(RequestPredicates.POST("/users"), this::createUser)
            .andRoute(RequestPredicates.DELETE("/users/{id}"), this::deleteUserById);
    }
	
	private Mono<ServerResponse> allUsers(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(userService.getAllUsers(), UserDepartment.class);
	}
	
	private Mono<ServerResponse> userById(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService
				.getUserById(serverRequest.pathVariable("id")), UserDepartment.class);
	}
	
	public Mono<ServerResponse> createUser(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        return userMono.flatMap(user -> {
        	// Save the object to the database
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userService.createUser(user), User.class);
        });
    }
	
	public Mono<ServerResponse> deleteUserById(ServerRequest request) {
		// Delete object
        Mono<Void> result = userService.deleteById(request.pathVariable("id"));
        // Return NO_CONTENT status if the object is deleted successfully
        return ServerResponse.noContent().build(result);
    }
}
