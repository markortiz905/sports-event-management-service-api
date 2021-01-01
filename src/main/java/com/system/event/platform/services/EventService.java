package com.system.event.platform.services;

import com.system.event.platform.dto.BiTransformer;
import com.system.event.platform.dto.EventDto;
import com.system.event.platform.dto.Transformer;
import com.system.event.platform.entities.Event;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import lombok.NonNull;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface EventService {
    List<Event> getAll() throws ServiceException, BadRequestServiceException;
    List<EventDto> getAllEventDtos(@NonNull Transformer<EventDto, Event> toDto) throws ServiceException, BadRequestServiceException;
    Event getEventAndEditions(Event event) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Event getEventAndSeries(Event event) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Event getEvent(Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Event create(@NonNull String eventName) throws ServiceException, BadRequestServiceException;
    Event create(@NonNull Event event) throws ServiceException, BadRequestServiceException;
    Event update(Event event) throws ServiceException, BadRequestServiceException;
    void addEventToCategory(Long eventId, Long categoryId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    void delete(Event event) throws ServiceException, BadRequestServiceException;
    void delete(Long eventId) throws ServiceException, BadRequestServiceException;

    EventDto getEvent(@NonNull Long eventId, Transformer<EventDto, Event> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    EventDto update(@NonNull EventDto request, @NonNull BiTransformer<EventDto, Event> updateObject, @NonNull Transformer<EventDto, Event> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    EventDto create(@NonNull Event event, @NonNull Transformer<EventDto, Event> toDto) throws ServiceException, BadRequestServiceException;
    EventDto create(@NonNull String eventName, @NonNull Transformer<EventDto, Event> toDto) throws ServiceException, BadRequestServiceException;
}
