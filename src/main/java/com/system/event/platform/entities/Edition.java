package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "event_edition")
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Edition implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    @NonNull private String name;
    @NonNull private Long startDate;
    @NonNull private Long endDate;
    @NonNull @Builder.Default private Boolean status = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id")
    @JsonBackReference
    @NonNull private Event event;

    @OneToMany(mappedBy = "edition")
    @JsonManagedReference
    private List<Race> races;

    @OneToMany(mappedBy = "edition")
    @JsonManagedReference
    private List<Serie> series;

    @NonNull @Builder.Default private Boolean deleted = false;

    @Override
    public String toString() {
        return "Edition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", event=" + event +
                '}';
    }

    public static Edition emptyEdition() {
        return Edition.builder().name("empty")
                .startDate(System.currentTimeMillis())
                .endDate(System.currentTimeMillis())
                .status(false)
                .event(Event.emptyEvent())
                .build();
    }

    public static Boolean isNotDeleted(Edition edition) {
        return edition != null && edition.getDeleted() !=null && !edition.getDeleted();
    }
}
