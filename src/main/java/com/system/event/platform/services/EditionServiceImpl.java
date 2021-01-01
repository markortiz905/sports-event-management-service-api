package com.system.event.platform.services;

import com.system.event.platform.dto.BiTransformer;
import com.system.event.platform.dto.CreateEditionFunction;
import com.system.event.platform.dto.EditionDto;
import com.system.event.platform.dto.Transformer;
import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.EditionRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
@Transactional
@CacheConfig(cacheNames = "editionCache")
public class EditionServiceImpl implements EditionService {

    @Autowired private EditionRepository editionRepository;
    @Autowired private EventService eventService;

    @Override
    @Cacheable("getAllEditionsCache")
    public List<Edition> getAll() throws ServiceException, BadRequestServiceException {
        return null;//editionRepository.findAllEditions();
    }

    @Override
    @Cacheable(cacheNames = "getEditionsOfEventCache", key = "#id")
    @Transactional(readOnly = true)
    public List<Edition> getEditionsOfEvent(Long id) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return eventService.getEvent(id).getEditions();
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Edition.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEditionAndRacesAndSeriesCache", key = "#edition.id")
    @Transactional(readOnly = true)
    public Edition getEditionAndRacesAndSeries(Edition edition) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return editionRepository.findById(edition.getId()).map( e -> {
                e.getSeries().size();e.getRaces().size();
                return e;
            }).orElseThrow( () -> new NotFoundServiceException("Edition of id " + edition.getId() + " not found."));
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Edition.class.getName() + " id: " + edition.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEditionCache", key = "#id")
    @Transactional(readOnly = true)
    public Edition getEdition(Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return editionRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Edition of id " + id + " not found."));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Edition.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getEditionDtoCache", key = "#id")
    @Transactional(readOnly = true)
    public EditionDto getEditionDto(@NonNull Long eventId, @NonNull Long id, @NonNull Transformer<EditionDto, Edition> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return toDto.transform(editionRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Event of id " + id + " not found.")));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Edition.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsCache", allEntries = true),
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true)
    })
    public Edition create(String name, Long startDate, Long endDate, Boolean status, Event event) throws ServiceException, BadRequestServiceException {
        try {
            return editionRepository.save(Edition.builder()
                    .name(name).startDate(startDate).endDate(endDate).status(status).event(event).build());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Edition.class.getName() + " name: " + name;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsCache", allEntries = true),
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true)
    })
    public Edition create(@NonNull Edition edition) throws ServiceException, BadRequestServiceException {
        try {
            return editionRepository.save(edition);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Edition.class.getName() + " name: " + edition.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsCache", allEntries = true),
            @CacheEvict(value = "getEditionCache", key = "#edition.id"),
            @CacheEvict(value = "getEditionDtoCache", key = "#edition.id"),
            @CacheEvict(value = "getEditionsOfEventCache", key = "#edition.event.id"),
            @CacheEvict(value = "getEditionAndRacesAndSeriesCache", key = "#edition.id"),
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true)
    })
    public Edition update(@NonNull Edition edition) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(edition.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Edition of id null is not found."));
            editionRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Edition of id " + id + " not found."));
            return editionRepository.save(edition);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Edition.class.getName() + " name: " + edition.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true),
            @CacheEvict(value = "getAllEditionsCache", allEntries = true),
            @CacheEvict(value = "getEditionCache", key = "#edition.id"),
            @CacheEvict(value = "getEditionDtoCache", key = "#edition.id"),
            @CacheEvict(value = "getEditionsOfEventCache", key = "#edition.event.id"),
            @CacheEvict(value = "getEditionAndRacesAndSeriesCache", key = "#edition.id")
    })
    public void delete(Edition edition) throws ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(edition.getId()).orElseThrow(() ->
                    new BadRequestServiceException("edition of id null is not found."));
            edition = editionRepository.findById(id).orElseThrow(() ->
                    new NotFoundServiceException("Event of id " + id + " not found."));
            edition = edition.toBuilder().deleted(true).build();
            editionRepository.save(edition);
        } catch (BadRequestServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Edition.class.getName() + " " + edition.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true),
            @CacheEvict(value = "getAllEditionsCache", allEntries = true),
            @CacheEvict(value = "getEditionCache", key = "#editionId"),
            @CacheEvict(value = "getEditionDtoCache", key = "#editionId"),
            @CacheEvict(value = "getEditionsOfEventCache", key = "#editionId"),
            @CacheEvict(value = "getEditionAndRacesAndSeriesCache", key = "#editionId")
    })
    public void delete(@NonNull Long eventId, @NonNull Long editionId) throws ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(editionId).orElseThrow(() ->
                    new BadRequestServiceException("edition of id null is not found."));
            Edition edition = editionRepository.findByIdAndEventId(id, eventId).orElseThrow(() ->
                    new NotFoundServiceException("Event of id " + id + " not found."));
            edition = edition.toBuilder().deleted(true).build();
            editionRepository.save(edition);
        } catch (BadRequestServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Edition.class.getName() + " " + editionId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable( cacheNames = "getAllEditionsDtoCache", keyGenerator = "appKeyGenerator")
    public List<EditionDto> getAll(@NonNull Long eventId, Transformer<EditionDto, Edition> toDto) throws ServiceException, BadRequestServiceException {
        return editionRepository.findAllEditions(eventId).stream().map(toDto::transform).collect(Collectors.toList());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#request.eventId"),
            @CacheEvict(cacheNames = "getEventDtoCache", key = "#request.eventId"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
    })
    public EditionDto create(@NonNull Long eventId, @NonNull EditionDto request, @NonNull CreateEditionFunction createEditionFunction, @NonNull Transformer<EditionDto, Edition> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            request = request.toBuilder().eventId(eventId).build();
            return toDto.transform(
                    editionRepository.save(
                            createEditionFunction.toEntity(
                                    request,
                                    (eId) -> {
                                        try {
                                            return eventService.getEvent(eId);
                                        } catch (Exception e) {
                                            log.error(e.getMessage(), e);
                                        }
                                        return null;
                                    })));
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Edition.class.getName() + " name: " + request.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEditionsDtoCache", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getEventCache", key = "#editionDto.eventId"),
            @CacheEvict(cacheNames = "getEventDtoCache", key = "#editionDto.eventId"),
            @CacheEvict(value = "getEditionCache", key = "#editionDto.id"),
            @CacheEvict(value = "getEditionDtoCache", key = "#editionDto.id")
    })
    public EditionDto update(@NonNull Long eventId, @NonNull EditionDto editionDto, @NonNull BiTransformer<EditionDto, Edition> updateObject, @NonNull Transformer<EditionDto, Edition> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            editionDto = editionDto.toBuilder().eventId(eventId).build();
            Long id = Optional.ofNullable(editionDto.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Edition of id null is not found."));
            Edition edition = editionRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Edition of id " + id + " not found."));
            return toDto.transform(editionRepository.save(updateObject.transform(edition, editionDto)));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Edition.class.getName() + " name: " + editionDto.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }
}
