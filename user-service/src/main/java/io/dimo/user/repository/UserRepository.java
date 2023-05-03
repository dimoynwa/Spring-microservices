package io.dimo.user.repository;

import java.util.UUID;

import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import io.dimo.user.exception.UserNotFoundException;
import io.dimo.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepository {

	private final ReactiveHashOperations<String, String, User> hashOperations;
	
	public UserRepository(ReactiveRedisOperations<String, User> redisOperations) {
		hashOperations = redisOperations.opsForHash();
	}
	
	public static final String KEY = "USERS";
	
    public Mono<User> findById(String id) {
        return hashOperations.get(KEY, id)
        		.switchIfEmpty(Mono.error(new UserNotFoundException(id)));
    }

    public Flux<User> findAll() {
        return hashOperations.values(KEY);
    }

    public Mono<User> save(User user) {
    	if (!StringUtils.hasLength(user.getFirstName()) || !StringUtils.hasLength(user.getEmail()) 
    			|| !StringUtils.hasLength(user.getDepartamentId())) {
    		return Mono.error(new IllegalArgumentException("Cannot be saved: user first name, email and department ID are required, but one or both is empty."));
    	}
    	
    	String id = UUID.randomUUID().toString();
    	user.setId(id);
    	return hashOperations.put(KEY, id, user).thenReturn(user);
    }
    
    public Mono<Void> deleteById(String id) {
        return hashOperations.remove(KEY, id).then();
    }
	
}
