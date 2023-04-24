package io.dimo.department.service;

import org.springframework.stereotype.Service;

import io.dimo.department.entity.Department;
import io.dimo.department.exception.DepartmentNotFoundException;
import io.dimo.department.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DepartmentService {

	private final DepartmentRepository departmentRepository;
	
	public Flux<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}
	
	public Mono<Department> getDepartmentById(String id) {
		return departmentRepository.findById(id)
				.switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)));
	}

	public Mono<Void> createDepartment(Department department) {
		return departmentRepository.save(department);
	}
	
	public Mono<Void> deleteById(String id) {
		return departmentRepository.deleteById(id);
	}
}
