package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "event_serie")
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    @NonNull private String name;
    @Enumerated(EnumType.STRING)
    @NonNull private Gender gender;
    @NonNull @Builder.Default private Integer numberOfParticipants = -1;
    @NonNull @Builder.Default private Integer minAge = 15;
    @NonNull @Builder.Default private Integer maxAge = 100;

    @ManyToOne
    @JoinColumn(name="event_id")
    @JsonBackReference
    private Event event;

    @ManyToOne
    @JoinColumn(name="event_edition_id")
    @JsonBackReference
    private Edition edition;

    @NonNull @Builder.Default private Boolean deleted = false;

    @Override
    public String toString() {
        return "Serie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", numberOfParticipants=" + numberOfParticipants +
                ", minAge=" + minAge +
                ", maxAge=" + maxAge +
                ", event=" + event +
                '}';
    }

    public static boolean associatedToEdition(Serie serie) {
        return serie != null && serie.getEdition() != null;
    }

    public static boolean notAssociatedToEdition(Serie serie) {
        return serie != null && serie.getEdition() == null;
    }

    public static boolean isNotDeleted(Serie serie) {
        return serie != null && serie.getDeleted() !=null && !serie.getDeleted();
    }
}
