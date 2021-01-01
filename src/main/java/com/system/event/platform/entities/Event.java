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
@Table(name = "event")
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    private String eventName;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER) //one to one so should be fine.
    @JoinColumn(name = "event_information_id", referencedColumnName = "id")
    @JsonManagedReference
    private Information information;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="event_category_id", referencedColumnName = "id")
    @JsonBackReference
    private Category category;

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Edition> editions;

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Race> races;

    @OneToMany(mappedBy = "event")
    @JsonManagedReference
    private List<Serie> series;

    @NonNull
    @Builder.Default
    private Boolean deleted = false;

    public Event(@NonNull String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + getId() +
                ", eventName='" + getEventName() + '\'' +
                ", information=" + getInformation() +
                ", category=" + getCategory() +
                '}';
    }

    public static Event emptyEvent() {
        return Event.builder().id(null).eventName("empty").information(null).build();
    }

    public static boolean isActive(Event event) {
        return event != null && !event.getDeleted();
    }
}
