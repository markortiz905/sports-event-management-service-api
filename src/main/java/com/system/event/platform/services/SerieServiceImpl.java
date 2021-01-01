package com.system.event.platform.services;

import com.system.event.platform.dto.CreateSerieFunction;
import com.system.event.platform.dto.SerieDto;
import com.system.event.platform.dto.Transformer;
import com.system.event.platform.dto.UpdateSerieFunction;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Gender;
import com.system.event.platform.entities.Race;
import com.system.event.platform.entities.Serie;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.SerieRepository;
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
import org.springframework.web.bind.annotation.PathVariable;

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
@CacheConfig(cacheNames = "serieCache")
public class SerieServiceImpl implements SerieService {
    @Autowired private SerieRepository serieRepository;
    @Autowired private EventService eventService;
    @Autowired private EditionService editionService;

    @Override
    @Cacheable(cacheNames = "getAllSeriesDtoCache")
    public List<SerieDto> getAll(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Transformer<SerieDto, Serie> toDto) throws ServiceException, BadRequestServiceException {
        return serieRepository.findSeriesAssociated(eventId, editionId).stream().map(toDto::transform).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "getSeriesOfEventCache", key = "#eventId")
    public List<Serie> getSeriesOfEvent(Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return eventService.getEvent(eventId).getSeries();
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get series from " + Event.class.getName() + " id: " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getSerieCache", key = "#serieId")
    public SerieDto getSerie(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Long serieId,
                             @NonNull Transformer<SerieDto, Serie> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return toDto.transform(serieRepository.findByIdAssociated(eventId, editionId, serieId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Serie of id " + serieId + " not found.")));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Serie.class.getName() + " id: " + serieId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)
    })
    public Serie create(@NonNull String name, @NonNull Gender gender, @NonNull Integer numberOfParticipants, @NonNull Integer minAge, @NonNull Integer maxAge, @NonNull Event event) throws ServiceException, BadRequestServiceException {
        try {
            return serieRepository.save(Serie.builder().name(name).gender(gender).numberOfParticipants(numberOfParticipants)
                    .minAge(minAge).maxAge(maxAge).event(event).build());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Race.class.getName() + " name: " + name;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)
    })
    public SerieDto create(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull SerieDto request, @NonNull CreateSerieFunction createSerieFunction, @NonNull Transformer<SerieDto, Serie> toDto) throws ServiceException, BadRequestServiceException {
        try {
            request = request.toBuilder().eventId(eventId).editionId(editionId).build();
            return toDto.transform(
                    serieRepository.save(
                        createSerieFunction.toEntity(
                                request,
                                (eId) -> {
                                    try {
                                        return eventService.getEvent(eId);
                                    } catch (Exception e) {
                                        /**
                                         * what ever reason, race entity wont build with null
                                         * event entity.
                                         */
                                        log.error(e.getMessage(), e);
                                    }
                                    return null;
                                },
                                (edId) -> {
                                    try {
                                        return editionService.getEdition(edId);
                                    } catch (Exception e) {
                                        /**
                                         * what ever reason, edition can be optional.
                                         */
                                        log.error(e.getMessage(), e);
                                    }
                                    return null;
                                })));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Race.class.getName() + " name: " + request.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            @CacheEvict(value = "getSeriesOfEventCache", key = "#eventId"),
            @CacheEvict(value = "getSerieCache", key = "#request.id"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
            @CacheEvict(value = "getEventDtoCache", key = "#eventId"),
            @CacheEvict(value = "getEditionDtoCache", key = "#editionId")
    })
    public SerieDto update(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull SerieDto request,
                           @NonNull UpdateSerieFunction updateSerieFunction, @NonNull Transformer<SerieDto, Serie> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            request = request.toBuilder().eventId(eventId).editionId(editionId).build();
            Long id = Optional.ofNullable(request.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Serie of id null is not found."));
            Serie serie = serieRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Serie of id " + id + " not found."));
            return toDto.transform(serieRepository.save(
                    updateSerieFunction.toEntity(
                            serie,
                            request,
                            (eId) -> {
                                try {
                                    return eventService.getEvent(eId);
                                } catch (Exception e) {
                                    /**
                                     * what ever reason, race entity wont build with null
                                     * event entity.
                                     */
                                    log.error(e.getMessage(), e);
                                }
                                return null;
                            },
                            (edId) -> {
                                try {
                                    return editionService.getEdition(edId);
                                } catch (Exception e) {
                                    /**
                                     * what ever reason, edition can be optional.
                                     */
                                    log.error(e.getMessage(), e);
                                }
                                return null;
                            })));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Serie.class.getName() + " name: " + request.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            @CacheEvict(value = "getSeriesOfEventCache", key = "#request.eventId"),
            @CacheEvict(value = "getSerieCache", key = "#request.id"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
    })
    public Serie updateSerie(@NonNull SerieDto request, @NonNull UpdateSerieFunction updateSerieFunction) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(request.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Serie of id null is not found."));
            Serie serie = serieRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Serie of id " + id + " not found."));
            return serieRepository.save(
                    updateSerieFunction.toEntity(
                            serie,
                            request,
                            (eventId) -> {
                                try {
                                    return eventService.getEvent(eventId);
                                } catch (Exception e) {
                                    /**
                                     * what ever reason, race entity wont build with null
                                     * event entity.
                                     */
                                    log.error(e.getMessage(), e);
                                }
                                return null;
                            },
                            (editionId) -> {
                                try {
                                    return editionService.getEdition(editionId);
                                } catch (Exception e) {
                                    /**
                                     * what ever reason, edition can be optional.
                                     */
                                    log.error(e.getMessage(), e);
                                }
                                return null;
                            }));
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Serie.class.getName() + " name: " + request.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            @CacheEvict(value = "getSeriesOfEventCache", key = "#serie.event.id"),
            @CacheEvict(value = "getSerieCache", key = "#serie.id"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
    })
    public void delete(Serie serie) throws ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(serie.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Serie of id null is not found."));
            serie = serieRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Serie of id " + id + " not found."));
            serie = serie.toBuilder().deleted(true).build();
            serieRepository.save(serie);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Serie.class.getName() + " name: " + serie.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllSeriesDtoCache", allEntries = true),
            @CacheEvict(value = "getSerieCache", key = "#serieId"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
    })
    public void delete(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Long serieId) throws ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(serieId).orElseThrow(() ->
                    new NotFoundServiceException("Serie of id null is not found."));
            Serie serie = serieRepository.findByIdAssociated(eventId, editionId, serieId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Serie of id " + id + " not found."));
            serie = serie.toBuilder().deleted(true).build();
            serieRepository.save(serie);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Serie.class.getName() + " ID: " + serieId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }
}
