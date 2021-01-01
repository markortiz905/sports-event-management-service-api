package com.system.event.platform.dto;

import lombok.*;

import java.util.List;

/**
 * @author mark ortiz
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EditionDto {
    private Long id;
    @NonNull
    private String name;
    @NonNull private Long startDate;
    @NonNull private Long endDate;
    @NonNull @Builder.Default private Boolean status = false;
    @NonNull private Long eventId;
    private List<RaceDto> races;
    private List<SerieDto> series;
}
