package com.system.event.platform.dto;

import com.system.event.platform.entities.DistanceUnit;
import lombok.*;

/**
 * @author mark ortiz
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RaceDto {
    private Long id;
    @NonNull private String name;
    @NonNull private Integer numberOfParticipants;
    @NonNull private Double distance;
    @NonNull private DistanceUnit unit;
    @NonNull private Integer minAge;
    @NonNull private Integer maxAge;

    @NonNull private Long eventId;
    private Long editionId;
}
