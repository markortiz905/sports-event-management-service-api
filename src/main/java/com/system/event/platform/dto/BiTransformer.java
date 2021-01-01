package com.system.event.platform.dto;

import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface BiTransformer<T, V> {
    V transform(V entity, T request) throws NotFoundServiceException, BadRequestServiceException, ServiceException;
}

