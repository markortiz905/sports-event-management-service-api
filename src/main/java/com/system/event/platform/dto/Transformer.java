package com.system.event.platform.dto;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface Transformer<T,V> {
    T transform(V source);
}

