package io.dimo.department.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.observability.MicrometerTracingAdapter;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import io.dimo.department.entity.Department;
import io.lettuce.core.resource.ClientResources;
import io.micrometer.observation.ObservationRegistry;

@Configuration
public class RedisConfig {

	@Bean
	ClientResources clientResources(ObservationRegistry observationRegistry) {
	    return ClientResources.builder()
	              .tracing(new MicrometerTracingAdapter(observationRegistry, "user-redis-db"))
	              .build();
	}
	
	@Bean
    ReactiveRedisOperations<String, Department> redisOperations(LettuceConnectionFactory connectionFactory) {
        RedisSerializationContext<String, Department> serializationContext = RedisSerializationContext
                .<String, Department>newSerializationContext(new StringRedisSerializer())
                .key(new StringRedisSerializer())
                .value(new GenericToStringSerializer<>(Department.class))
                .hashKey(new StringRedisSerializer())
                .hashValue(new GenericJackson2JsonRedisSerializer())
                .build();
        return new ReactiveRedisTemplate<>(connectionFactory, serializationContext);
    }
	
}
