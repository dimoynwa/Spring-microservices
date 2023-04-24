package io.dimo.user.service;

import org.springframework.stereotype.Service;

import io.dimo.user.exception.UserNotFoundException;
import io.dimo.user.model.User;
import io.dimo.user.model.UserDepartment;
import io.dimo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;
	private final DepartmentService departmentService;
	
	public Flux<UserDepartment> getAllUsers() {
		return userRepository.findAll()
				.flatMap(this::enrichUser);
	}
	
	public Mono<UserDepartment> getUserById(String id) {
		log.info("Get user by id: ", id);
		return userRepository.findById(id)
				.switchIfEmpty(Mono.error(new UserNotFoundException(id)))
				.flatMap(this::enrichUser);
	}

	public Mono<Void> createUser(User user) {
		return userRepository.save(user);
	}
	
	public Mono<Void> deleteById(String id) {
		return userRepository.deleteById(id);
	}
	
	private Mono<UserDepartment> enrichUser(User user) {
		return departmentService.getDepartment(user.getDepartamentId()).map(department -> UserDepartment.builder()
        			.id(user.getId()).email(user.getEmail()).firstName(user.getFirstName())
        			.id(user.getLastName()).departament(department).build());
	}

}
