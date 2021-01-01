package com.system.event.platform;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author mark ortiz
 */
@Slf4j
public class CustomKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        log.debug("CustomKeyGenerator.generate started");
        String joining = target.getClass().getSimpleName() + "_"
                + method.getName() + "_"
                + Arrays.asList(params)
                .stream()
                .filter(this::isNotLambda)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
        log.debug("CustomKeyGenerator.generate ended");
        return joining;
    }

    public boolean isNotLambda(Object param) {
        String l = param.getClass().toString();
        log.debug("param type : " + l);
        return !l.contains("$$Lambda$"); //dirty way to do it but should work.
    }
}
