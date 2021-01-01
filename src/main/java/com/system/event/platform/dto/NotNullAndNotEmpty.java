package com.system.event.platform.dto;

import java.util.List;
import java.util.Map;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface NotNullAndNotEmpty<T> {
    boolean check(T object);

    public static NotNullAndNotEmpty<List> checkList() {
        return (obj) -> obj != null && !obj.isEmpty();
    }

    public static NotNullAndNotEmpty<Map> checkMap() {
        return (obj) -> obj != null && !obj.isEmpty();
    }
}
