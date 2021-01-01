package com.system.event.platform.services;

import com.system.event.platform.dto.BiTransformer;
import com.system.event.platform.dto.EventDto;
import com.system.event.platform.dto.Transformer;
import com.system.event.platform.entities.Category;
import com.system.event.platform.entities.Event;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.EventRepository;
import com.system.event.platform.repositories.InformationRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mark ortiz
 */
@Slf4j
@Service
@Primary
@CacheConfig(cacheNames = "eventCache")
@Transactional(rollbackFor=Exception.class)
@SuppressWarnings({"unused"})
public class EventServiceImpl implements EventService {

    @Autowired private EventRepository eventRepository;
    @Autowired private CategoryService categoryService;
    @Autowired private InformationRepository informationRepository;

    @Override
    @Cacheable(cacheNames = "getAllEvents")
    public List<Event> getAll() throws ServiceException, BadRequestServiceException {
        try {
            return eventRepository.findAllEvents();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get list of " + Event.class.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getAllEventDtos", keyGenerator = "appKeyGenerator")
    public List<EventDto> getAllEventDtos(@NonNull Transformer<EventDto, Event> toDto) throws ServiceException, BadRequestServiceException {
        try {
            log.info("hit getAllEventDtos");
            return eventRepository.findAllEvents().stream().map(toDto::transform).collect(Collectors.toList());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get list of " + Event.class.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEventAndSeriesCache", key = "#eventRequest.id")//update cache if any
    public Event getEventAndSeries(Event eventRequest) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Event event = eventRepository.findById(eventRequest.getId())
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + eventRequest.getId() + " not found."));
            log.debug("Number of series found: " + event.getSeries().size());//do not remove.
            return eventRequest.toBuilder().series(event.getSeries()).build();
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + eventRequest.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEventAndEditionsCache", key = "#eventRequest.id")//update cache if any
    public Event getEventAndEditions(Event eventRequest) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Event event = eventRepository.findById(eventRequest.getId())
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + eventRequest.getId() + " not found."));
            log.debug("Number of editions found: " + event.getEditions().size());//do not remove.
            return eventRequest.toBuilder().editions(event.getEditions()).build();
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + eventRequest.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEventCache", key = "#eventId")
    @Transactional(readOnly = true)
    public Event getEvent(Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return eventRepository.findById(eventId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + eventId + " not found."));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEventDtoCache", key = "#eventId")
    public EventDto getEvent(@NonNull Long eventId, @NonNull Transformer<EventDto, Event> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return toDto.transform(
                    eventRepository.findById(eventId).orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + eventId + " not found.")));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true)
    })
    public Event create(@NonNull Event event) throws ServiceException, BadRequestServiceException {
        try {
            if(event.getInformation() != null) {
                event = event.toBuilder().information(informationRepository.save(event.getInformation())).build();
            }
            return eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Event.class.getName() + " eventName: " + event.getEventName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true)
    })
    public EventDto create(@NonNull Event event, Transformer<EventDto, Event> transformer) throws ServiceException, BadRequestServiceException {
        try {
            if(event.getInformation() != null) {
                event = event.toBuilder().information(informationRepository.save(event.getInformation())).build();
            }
            return transformer.transform(eventRepository.save(event));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Event.class.getName() + " eventName: " + event.getEventName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true)
    })
    public EventDto create(@NonNull String eventName, Transformer<EventDto, Event> transformer) throws ServiceException, BadRequestServiceException {
        try {
            return transformer.transform(eventRepository.save(Event.builder().eventName(eventName).build()));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Event.class.getName() + " eventName: " + eventName;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true)
    })
    public Event create(@NonNull String eventName) throws ServiceException, BadRequestServiceException {
        try {
            Event event = Event.builder().eventName(eventName).build();
            return eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Event.class.getName() + " eventName: " + eventName;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#event.id")
    })
    public Event update(Event event)  throws ServiceException, BadRequestServiceException {
        try {
            return eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to update " + Event.class.getName() + " " + event.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getEventDtoCache", key = "#request.id")
    })
    public EventDto update(@NonNull EventDto request, @NonNull BiTransformer<EventDto, Event> updateObject, @NonNull Transformer<EventDto, Event> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(request.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Event of id null is not found."));
            Event event = eventRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + id + " not found."));
            return toDto.transform(eventRepository.save(updateObject.transform(event, request)));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to update " + Event.class.getName() + " " + request.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#eventId")
    })
    public void addEventToCategory(Long eventId, Long categoryId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + eventId + " not found."));
            Category cat = categoryService.getCategory(categoryId);
            event = event.toBuilder().category(cat).build();
            eventRepository.save(event);
            categoryService.update(cat);//trigger cache refresh, should not hit db if no state change anyway.
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to update " + Event.class.getName() + " " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#event.id"),
            @CacheEvict(value = "getEventDtoCache", key = "#event.id"),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)
    })
    public void delete(Event event) throws ServiceException, BadRequestServiceException {
        try {
            eventRepository.delete(event);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Event.class.getName() + " " + event.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#eventId"),
            @CacheEvict(value = "getEventDtoCache", key = "#eventId")
    })
    public void delete(Long eventId) throws ServiceException, BadRequestServiceException {
        try {
            Event event = getEvent(eventId);
            event = event.toBuilder().deleted(true).build();
            eventRepository.save(event);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Event.class.getName() + " " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

}
