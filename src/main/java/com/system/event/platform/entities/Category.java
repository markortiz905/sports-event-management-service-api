package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "event_category")
@Builder(toBuilder = true, access = AccessLevel.PUBLIC)
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Category implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;
    private String categoryName;
    private String shortDescription;

    @OneToMany(mappedBy = "category")
    @JsonManagedReference
    private List<Event> events;

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                '}';
    }
}
