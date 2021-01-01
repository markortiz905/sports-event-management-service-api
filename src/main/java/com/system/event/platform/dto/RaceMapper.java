package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author mark ortiz
 */
@Component
public class RaceMapper {
    public Race updateEntity(Race entity, RaceDto request, @NonNull Function<Long, Event> getEvent, @NonNull Function<Long, Edition> getEdition) {
        if (entity == null || request == null) return null;
        return entity.toBuilder()
                .id(request.getId())
                .name(request.getName())
                .numberOfParticipants(request.getNumberOfParticipants())
                .distance(request.getDistance())
                .unit(request.getUnit())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .event(getEvent.apply(request.getEventId()))
                .edition(getEdition.apply(request.getEditionId()))
                .build();
    }

    /**
     * toEntity Methods is better used within a @Transactional methods/classes like in service layer
     * @param dto
     * @param getEvent
     * @param getEdition
     * @return
     */
    public Race toEntity(RaceDto dto, @NonNull Function<Long, Event> getEvent, Function<Long, Edition> getEdition) {
        if (dto == null) return null;
        return Race.builder()
                .id(dto.getId())
                .name(dto.getName())
                .numberOfParticipants(dto.getNumberOfParticipants())
                .distance(dto.getDistance())
                .unit(dto.getUnit())
                .minAge(dto.getMinAge())
                .maxAge(dto.getMaxAge())
                .event(getEvent.apply(dto.getEventId()))
                .edition(getEdition.apply(dto.getEditionId()))
                .build();
    }
    public RaceDto toDto(Race entity) {
        if (entity == null) return null;
        return RaceDto.builder()
                .id(entity.getId())
                .eventId(Optional.ofNullable(entity.getEvent()).orElse(Event.emptyEvent()).getId())
                .editionId(Optional.ofNullable(entity.getEdition()).orElse(Edition.emptyEdition()).getId())
                .name(entity.getName())
                .numberOfParticipants(entity.getNumberOfParticipants())
                .distance(entity.getDistance())
                .unit(entity.getUnit())
                .minAge(entity.getMinAge())
                .maxAge(entity.getMaxAge())
                .build();
    }
}
