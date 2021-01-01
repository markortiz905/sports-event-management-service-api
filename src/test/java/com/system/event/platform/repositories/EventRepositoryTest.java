package com.system.event.platform.repositories;

import com.system.event.platform.entities.Category;
import com.system.event.platform.entities.Event;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("junit")
public class EventRepositoryTest {
    @Autowired private DataSource dataSource;
    @Autowired private JdbcTemplate jdbcTemplate;
    @Autowired private EntityManager entityManager;
    @Autowired private EventRepository eventRepository;
    @Autowired private CategoryRepository categoryRepository;

    @Test
    void injectedComponentsAreNotNull(){
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(eventRepository).isNotNull();
        assertThat(categoryRepository).isNotNull();
    }

    @Test
    void testCreateEvent() {
        Category category = Category.builder().categoryName("test").shortDescription("desc").build();

        category = categoryRepository.save(category);

        List<Event> events = new ArrayList<>();
        Event event = new Event("event 1");
        event = eventRepository.save(event);
        events.add(event);

        event = new Event("event 2");
        event = eventRepository.save(event);
        events.add(event);

        category = category.toBuilder().events(events).build();

        categoryRepository.save(category);

        Optional<Category> opsCat = categoryRepository.findById(category.getId());
        assertThat(opsCat.isPresent()).isTrue();

        category = opsCat.get();

        assertThat(category.getEvents()).isNotNull();
        assertThat(category.getEvents()).isNotEmpty();

        assertThat(category.getEvents()).contains(event);
    }
}
