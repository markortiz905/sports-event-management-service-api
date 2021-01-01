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
import lombok.NonNull;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface EditionService {
    List<Edition> getAll() throws ServiceException, BadRequestServiceException;
    List<Edition> getEditionsOfEvent(Long id) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Edition getEditionAndRacesAndSeries(Edition edition) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Edition getEdition(Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    Edition create(String name, Long startDate, Long endDate, Boolean status, Event event) throws ServiceException, BadRequestServiceException;
    Edition create(Edition edition) throws ServiceException, BadRequestServiceException;
    Edition update(Edition edition) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    void delete(Edition edition) throws ServiceException, BadRequestServiceException;

    EditionDto create(@NonNull Long eventId, @NonNull EditionDto request, @NonNull CreateEditionFunction createEditionFunction, @NonNull Transformer<EditionDto, Edition> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    EditionDto update(@NonNull Long eventId, @NonNull EditionDto edition, @NonNull BiTransformer<EditionDto, Edition> updateObject, @NonNull Transformer<EditionDto, Edition> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    List<EditionDto> getAll(@NonNull Long eventId, @NonNull Transformer<EditionDto, Edition> toDto) throws ServiceException, BadRequestServiceException;
    EditionDto getEditionDto(@NonNull Long eventId, @NonNull Long id, @NonNull Transformer<EditionDto, Edition> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException;

    void delete(@NonNull Long eventId, @NonNull Long editionId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
}
