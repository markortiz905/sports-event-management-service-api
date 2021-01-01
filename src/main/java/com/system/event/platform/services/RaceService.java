package com.system.event.platform.services;

import com.system.event.platform.dto.*;
import com.system.event.platform.entities.DistanceUnit;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import lombok.NonNull;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface RaceService {
    List<Race> getAll() throws ServiceException, BadRequestServiceException;
    List<Race> getRacesOfEvent(@NonNull Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Race getRace(@NonNull Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    Race create(String name, Integer numberOfParticipants, Double distance, DistanceUnit unit, Integer minAge, Integer maxAge, Event event) throws ServiceException, BadRequestServiceException;
    void delete(@NonNull Race race) throws NotFoundServiceException, ServiceException, BadRequestServiceException;

    RaceDto getRace(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId, @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    List<RaceDto> getAll(@NonNull Long eventId, @NonNull Long editionId, @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException;
    RaceDto update(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId, @NonNull RaceDto request, @NonNull UpdateRaceFunction updateRaceFunction, @NonNull Transformer<RaceDto, Race> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    RaceDto create(@NonNull Long eventId, @NonNull Long editionId, @NonNull RaceDto request, @NonNull CreateRaceFunction createRaceFunction, @NonNull Transformer<RaceDto, Race> toDto) throws ServiceException, BadRequestServiceException;
    void delete(@NonNull Long eventId, @NonNull Long editionId, @NonNull Long raceId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;

}
