package com.system.event.platform.repositories;

import com.system.event.platform.entities.Edition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
/**
 * @author mark ortiz
 */
public interface EditionRepository extends CrudRepository<Edition, Long> {

    @Query("SELECT u FROM Edition u " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false AND ev.id = :eventId")
    List<Edition> findAllEditions(Long eventId);

    @Override
    @Query("SELECT u FROM Edition u " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false AND u.id = :editionId")
    Optional<Edition> findById(Long editionId);

    @Query("SELECT u FROM Edition u " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false AND u.id = :editionId AND ev.id = :eventId")
    Optional<Edition> findByIdAndEventId(Long editionId, Long eventId);
}