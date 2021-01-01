package com.system.event.platform.repositories;

import com.system.event.platform.entities.Serie;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author mark ortiz
 */
public interface SerieRepository extends CrudRepository<Serie, Long> {

    @Query("SELECT u FROM Serie u WHERE u.deleted = false")
    List<Serie> findAllRaces();

    @Query("SELECT u FROM Serie u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE ev.id = :eventId AND ed.id = :editionId AND u.deleted = false")
    List<Serie> findSeriesAssociated(Long eventId, Long editionId);

    @Override
    @Query("SELECT u FROM Serie u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false AND u.id = :serieId")
    Optional<Serie> findById(Long serieId);

    @Query("SELECT u FROM Serie u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE ev.id = :eventId AND ed.id = :editionId AND u.deleted = false AND u.id = :serieId")
    Optional<Serie> findByIdAssociated(Long eventId, Long editionId, Long serieId);
}