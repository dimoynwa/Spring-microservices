package io.dimo.user.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheConfig {

	public static final String DEPARTMENT_CACHE = "departmentCache";
	
	@Bean
	CacheManager cacheManager() {
	    ConcurrentMapCacheManager concurrentMapCacheManager = new ConcurrentMapCacheManager(DEPARTMENT_CACHE);
	    return concurrentMapCacheManager;
	}
	
	@CacheEvict(allEntries = true, value = {DEPARTMENT_CACHE})
	@Scheduled(fixedDelay = 10 * 60 * 1000 ,  initialDelay = 500)
	public void reportCacheEvict() {
	    log.info("Clear Cache");
	}
	
}
