package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.entities.Serie;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mark ortiz
 */
@Component
public final class EventMapper {
    @Autowired private InformationMapper informationMapper;
    @Autowired private EditionMapper editionMapper;
    @Autowired private RaceMapper raceMapper;
    @Autowired private SerieMapper serieMapper;

    public Event updateEntityFromRequest(@NonNull Event entity, @NonNull EventDto request) {
        return entity.toBuilder()
                .id(request.getId())
                .eventName(request.getEventName())
                .information(informationMapper.toEntity(request.getInformation()))
                .build();
    }

    public Event toEntity(EventDto dto) {
        if (dto == null) return null;
        return Event.builder()
                .id(dto.getId())
                .eventName(dto.getEventName())
                .information(informationMapper.toEntity(dto.getInformation()))
                .build();
    }
    public EventDto toDto(Event entity) {
        if (entity == null) return null;
        return EventDto.builder()
                .id(entity.getId())
                .eventName(entity.getEventName())
                .information(informationMapper.toDto(entity.getInformation()))
                // TODO: don't use hibernate lazy collection, directly use service getBy ID., possibly by adding functions in the
                //  method argument.
                .editions(Optional.ofNullable(entity.getEditions()).orElse(Collections.emptyList())
                        .stream().filter(Edition::isNotDeleted).map(editionMapper::toDto).collect(Collectors.toList()))
                .races(Optional.ofNullable(entity.getRaces()).orElse(Collections.emptyList())
                        .stream().filter(Race::isNotDeleted).filter(Race::notAssociatedToEdition).map(raceMapper::toDto).collect(Collectors.toList()))
                .series(Optional.ofNullable(entity.getSeries()).orElse(Collections.emptyList())
                        .stream().filter(Serie::isNotDeleted).filter(Serie::notAssociatedToEdition).map(serieMapper::toDto).collect(Collectors.toList()))
                .build();
    }
}
