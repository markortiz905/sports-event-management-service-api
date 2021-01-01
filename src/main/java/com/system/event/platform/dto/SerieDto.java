package com.system.event.platform.dto;

import com.system.event.platform.entities.Gender;
import lombok.*;

/**
 * @author mark ortiz
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SerieDto {
    private Long id;
    @NonNull private String name;
    @NonNull private Gender gender;
    @NonNull @Builder.Default private Integer numberOfParticipants = -1;
    @NonNull @Builder.Default private Integer minAge = 15;
    @NonNull @Builder.Default private Integer maxAge = 100;

    @NonNull private Long eventId;
    private Long editionId;
}
