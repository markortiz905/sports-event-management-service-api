package com.system.event.platform.services;

import com.system.event.platform.dto.CreateSerieFunction;
import com.system.event.platform.dto.SerieDto;
import com.system.event.platform.dto.Transformer;
import com.system.event.platform.dto.UpdateSerieFunction;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Gender;
import com.system.event.platform.entities.Serie;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import lombok.NonNull;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface SerieService {
    List<SerieDto> getAll(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Transformer<SerieDto, Serie> toDto) throws ServiceException, BadRequestServiceException;
    SerieDto getSerie(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Long id, @NonNull Transformer<SerieDto, Serie> toDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    SerieDto create(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull SerieDto request, @NonNull CreateSerieFunction createSerieFunction, @NonNull Transformer<SerieDto, Serie> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    SerieDto update(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull SerieDto serie, @NonNull UpdateSerieFunction updateSerieFunction, @NonNull Transformer<SerieDto, Serie> toDto) throws NotFoundServiceException, ServiceException, BadRequestServiceException;

    Serie updateSerie(@NonNull SerieDto serie, @NonNull UpdateSerieFunction updateSerieFunction) throws NotFoundServiceException, ServiceException, BadRequestServiceException;

    List<Serie> getSeriesOfEvent(@NonNull Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Serie create(@NonNull String name, @NonNull Gender gender, @NonNull Integer numberOfParticipants, @NonNull Integer minAge, @NonNull Integer maxAge, @NonNull Event event) throws ServiceException, BadRequestServiceException;
    void delete(@NonNull Serie serie) throws ServiceException, BadRequestServiceException;
    void delete(@PathVariable Long eventId, @PathVariable Long editionId, @NonNull Long serieId) throws ServiceException, BadRequestServiceException;
}
