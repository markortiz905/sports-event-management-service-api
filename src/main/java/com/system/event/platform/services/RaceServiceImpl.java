package com.system.event.platform.services;

import com.system.event.platform.dto.*;
import com.system.event.platform.entities.DistanceUnit;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.RaceRepository;
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
@CacheConfig(cacheNames = "raceCache")
public class RaceServiceImpl implements RaceService {
    @Autowired private RaceRepository raceRepository;
    @Autowired private EventService eventService;
    @Autowired private EditionService editionService;

    @Override
    @Cacheable(cacheNames = "getAllRaces")
    public List<Race> getAll() throws ServiceException, BadRequestServiceException {
        return raceRepository.findAllRaces();
    }

    @Override
    @Cacheable(cacheNames = "getAllRacesDto", keyGenerator = "appKeyGenerator")
    public List<RaceDto> getAll(@NonNull Long eventId, @NonNull Long editionId,
                                @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException {
        return raceRepository.findAllRacesAssociated(eventId, editionId).stream().map(toDto::transform).collect(Collectors.toList());
    }

    @Override
    @Cacheable(cacheNames = "getRacesOfEventCache", key = "#eventId")
    public List<Race> getRacesOfEvent(@NonNull Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return eventService.getEvent(eventId).getRaces();
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + eventId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getRaceCache", key = "#id")
    public Race getRace(@NonNull Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return raceRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Race of id " + id + " not found."));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Race.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getRaceDtoCache", key = "#raceId")
    public RaceDto getRace(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId, @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return toDto.transform(raceRepository.findRaceAssociated(eventId, editionId, raceId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Race of id " + raceId + " not found.")));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Race.class.getName() + " id: " + raceId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllRaces", allEntries = true),
            @CacheEvict(value = "getAllRacesDto", allEntries = true)
    })
    public Race create(String name, Integer numberOfParticipants, Double distance, DistanceUnit unit, Integer minAge, Integer maxAge, Event event) throws ServiceException, BadRequestServiceException {
        try {
            return raceRepository.save(new Race(name, numberOfParticipants, distance, unit, minAge, maxAge, event));
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
            @CacheEvict(value = "getAllRaces", allEntries = true),
            @CacheEvict(value = "getAllRacesDto", allEntries = true),
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true),
    })
    public RaceDto create(@NonNull Long eventId, @NonNull Long editionId, @NonNull RaceDto request, @NonNull CreateRaceFunction createRaceFunction, @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException {
        try {
            request = request.toBuilder().eventId(eventId).editionId(editionId).build();
            return toDto.transform(
                    raceRepository.save(
                            createRaceFunction.toEntity(
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
            @CacheEvict(value = "getAllRaces", allEntries = true),
            @CacheEvict(value = "getAllRacesDto", allEntries = true),
            @CacheEvict(value = "getRacesOfEventCache", key = "#request.eventId"),
            @CacheEvict(value = "getRace", key = "#request.id"),
            @CacheEvict(value = "getRaceDtoCache", key = "#request.id"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)

    })
    public RaceDto update(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId, @NonNull RaceDto request, @NonNull UpdateRaceFunction updateObject, @NonNull Transformer<RaceDto, Race> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(request.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Race of id null is not found."));
            Race race = raceRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Race of id " + id + " not found."));
            return toDto.transform(
                    raceRepository.save(
                            updateObject.toEntity(
                                    race,
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
        } catch (NotFoundServiceException e) { throw e;
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
            @CacheEvict(value = "getAllRaces", allEntries = true),
            @CacheEvict(value = "getAllRacesDto", allEntries = true),
            @CacheEvict(value = "getRacesOfEventCache", key = "#race.event.id"),
            @CacheEvict(value = "getRace", key = "#race.id"),
            @CacheEvict(value = "getRaceDtoCache", key = "#race.id"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)
    })
    public void delete(@NonNull Race race) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Long id = Optional.ofNullable(race.getId()).orElseThrow(() ->
                    new NotFoundServiceException("Race of id null is not found."));
            race = raceRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Race of id " + id + " not found."));
            race = race.toBuilder().deleted(true).build();
            raceRepository.save(race);
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Race.class.getName() + " " + race.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllRaces", allEntries = true),
            @CacheEvict(value = "getAllRacesDto", allEntries = true),
            @CacheEvict(value = "getRace", key = "#raceId"),
            @CacheEvict(value = "getRaceDtoCache", key = "#raceId"),
            //always evict events collection any any additions
            @CacheEvict(value = "getAllEvents", allEntries = true),
            @CacheEvict(value = "getAllEventDtos", allEntries = true)
    })
    public void delete(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            Race race = raceRepository.findRaceAssociated(eventId, editionId, raceId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Race of id " + raceId + " not found."));
            race = race.toBuilder().deleted(true).build();
            raceRepository.save(race);
        } catch (NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Race.class.getName() + " " + raceId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }
}
