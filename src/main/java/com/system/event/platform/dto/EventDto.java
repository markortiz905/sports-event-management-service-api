package com.system.event.platform.dto;

import lombok.*;

import java.io.Serializable;
import java.util.List;

/**
 * @author mark ortiz
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EventDto implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String eventName;
    private InformationDto information;
    private List<EditionDto> editions;
    private List<RaceDto> races;
    private List<SerieDto> series;
}
