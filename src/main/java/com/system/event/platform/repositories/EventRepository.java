package com.system.event.platform.repositories;

import com.system.event.platform.entities.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
/**
 * @author mark ortiz
 */
public interface EventRepository extends CrudRepository<Event, Long> {

    @Query("SELECT u FROM Event u WHERE u.deleted = false")
    List<Event> findAllEvents();

    @Override
    @Query("SELECT u FROM Event u WHERE u.deleted = false AND u.id = :eventId")
    Optional<Event> findById(Long eventId);
}
