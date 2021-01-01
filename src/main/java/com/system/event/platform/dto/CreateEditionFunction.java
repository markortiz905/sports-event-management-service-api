package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;

import java.util.function.Function;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface CreateEditionFunction {
    Edition toEntity(EditionDto request, Function<Long, Event> getEvent) throws NotFoundServiceException, BadRequestServiceException, ServiceException;
}

