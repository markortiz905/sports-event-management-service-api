package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;
import com.system.event.platform.entities.Serie;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author mark ortiz
 */
@Component
public class SerieMapper {
    public Serie updateEntity(@NonNull Serie entity, @NonNull SerieDto request, @NonNull Function<Long, Event> getEvent,
                             @NonNull Function<Long, Edition> getEdition) {
        return entity.toBuilder()
                .id(request.getId())
                .name(request.getName())
                .gender(request.getGender())
                .numberOfParticipants(request.getNumberOfParticipants())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .event(getEvent.apply(request.getEventId()))
                .edition(getEdition.apply(request.getEditionId()))
                .build();
    }
    public Serie toEntity(@NonNull SerieDto request, @NonNull Function<Long, Event> getEvent,
                          @NonNull Function<Long, Edition> getEdition) {
        return Serie.builder()
                .id(request.getId())
                .name(request.getName())
                .gender(request.getGender())
                .numberOfParticipants(request.getNumberOfParticipants())
                .minAge(request.getMinAge())
                .maxAge(request.getMaxAge())
                .event(getEvent.apply(request.getEventId()))
                .edition(getEdition.apply(request.getEditionId()))
                .build();
    }
    public SerieDto toDto(Serie entity) {
        if (entity == null) return null;
        return SerieDto.builder()
                .id(entity.getId())
                .eventId(Optional.ofNullable(entity.getEvent()).orElse(Event.emptyEvent()).getId())
                .editionId(Optional.ofNullable(entity.getEdition()).orElse(Edition.emptyEdition()).getId())
                .name(entity.getName())
                .gender(entity.getGender())
                .numberOfParticipants(entity.getNumberOfParticipants())
                .minAge(entity.getMinAge())
                .maxAge(entity.getMaxAge())
                .build();
    }
}
