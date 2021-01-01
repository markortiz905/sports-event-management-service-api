package com.system.event.platform;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author mark ortiz
 */
@EnableCaching
@Configuration
public class ApplicationCacheConfig extends CachingConfigurerSupport {

    @Bean
    @Primary
    public KeyGenerator appKeyGenerator() {
        return new CustomKeyGenerator();
    }
}
