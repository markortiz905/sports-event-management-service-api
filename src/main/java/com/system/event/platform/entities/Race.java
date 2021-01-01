package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "event_race")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Race implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private String name;
    private Integer numberOfParticipants;
    private Double distance;
    @Enumerated(EnumType.STRING)
    private DistanceUnit unit;
    private Integer minAge;
    private Integer maxAge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    @JsonBackReference
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_edition_id")
    @JsonBackReference
    private Edition edition;

    @NonNull @Builder.Default private Boolean deleted = false;

    public Race(@NonNull String name, @NonNull Integer numberOfParticipants, @NonNull Double distance, @NonNull DistanceUnit unit, @NonNull Integer minAge, @NonNull Integer maxAge, @NonNull Event event) {
        this.name = name;
        this.numberOfParticipants = numberOfParticipants;
        this.distance = distance;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.event = event;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "Race{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfParticipants=" + numberOfParticipants +
                ", distance=" + distance +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", event=" + event +
                '}';
    }

    public static boolean associatedToEdition(Race race) {
        return race != null && race.getEdition() != null;
    }

    public static boolean notAssociatedToEdition(Race race) {
        return race != null && race.getEdition() == null;
    }

    public static boolean isNotDeleted(Race race) {
        return race != null && race.getDeleted() !=null && !race.getDeleted();
    }
}
