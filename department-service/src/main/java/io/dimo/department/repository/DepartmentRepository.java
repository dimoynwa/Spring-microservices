package io.dimo.department.repository;

import java.util.UUID;

import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import io.dimo.department.entity.Department;
import io.dimo.department.exception.DepartmentNotFoundException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class DepartmentRepository {

	private final ReactiveHashOperations<String, String, Department> hashOperations;
	
	public DepartmentRepository(ReactiveRedisOperations<String, Department> redisOperations) {
		hashOperations = redisOperations.opsForHash();
	}
	
	public static final String KEY = "DEPARTMENTS";
	
    public Mono<Department> findById(String id) {
        return hashOperations.get(KEY, id)
        		.switchIfEmpty(Mono.error(new DepartmentNotFoundException(id)));
    }

    public Flux<Department> findAll() {
        return hashOperations.values(KEY);
    }

    public Mono<Void> save(Department department) {
    	if (!StringUtils.hasLength(department.getName()) || !StringUtils.hasLength(department.getCode())) {
    		return Mono.error(new IllegalArgumentException("Cannot be saved: department name and code are required, but one or both is empty."))
    				.then();
    	}
    	
    	String id = UUID.randomUUID().toString();
    	department.setId(id);
    	return hashOperations.put(KEY, id, department).then();
    }
    
    public Mono<Void> deleteById(String id) {
        return hashOperations.remove(KEY, id).then();
    }
	
}
