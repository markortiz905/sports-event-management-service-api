package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.entities.Serie;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author mark ortiz
 */
@Component
public class EditionMapper {
    @Autowired private RaceMapper raceMapper;
    @Autowired private SerieMapper serieMapper;
    public Edition toEntity(EditionDto dto, Function<Long, Event> getEvent) throws NotFoundServiceException, BadRequestServiceException, ServiceException {
        if (dto == null || getEvent == null) return null;
        return Edition.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .event(getEvent.apply(dto.getEventId()))
                .build();
    }

    /**
     * @param entity
     * @param request
     * @return
     */
    public Edition updateEntity(Edition entity, EditionDto request) {
        if (entity == null || request == null) return null;
        return entity.toBuilder()
                .id(request.getId())
                .name(request.getName())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus())
                .build();
    }
    public Edition toEntity(EditionDto dto) {
        if (dto == null) return null;
        return Edition.builder()
                .id(dto.getId())
                .name(dto.getName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .build();
    }
    public EditionDto toDto(Edition entity) {
        if (entity == null) return null;
        return EditionDto.builder()
                .id(entity.getId())
                .eventId(Optional.ofNullable(entity.getEvent()).orElse(Event.emptyEvent()).getId())
                .name(entity.getName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .races(Optional.ofNullable(entity.getRaces()).orElse(Collections.emptyList())
                        .stream().filter(Race::isNotDeleted).filter(Race::associatedToEdition).map(raceMapper::toDto).collect(Collectors.toList()))
                .series(Optional.ofNullable(entity.getSeries()).orElse(Collections.emptyList())
                        .stream().filter(Serie::isNotDeleted).filter(Serie::associatedToEdition).map(serieMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
