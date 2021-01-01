package com.system.event.platform.repositories;

import com.system.event.platform.entities.Race;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author mark ortiz
 */
public interface RaceRepository extends CrudRepository<Race, Long> {

    @Query("SELECT u FROM Race u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false")
    List<Race> findAllRaces();

    @Query("SELECT u FROM Race u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE ev.id = :eventId AND ed.id = :editionId AND u.deleted = false")
    List<Race> findAllRacesAssociated(Long eventId, Long editionId);

    @Query("SELECT u FROM Race u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE ev.id = :eventId AND ed.id = :editionId AND u.deleted = false AND u.id = :raceId")
    Optional<Race> findRaceAssociated(Long eventId, Long editionId, Long raceId);

    @Override
    @Query("SELECT u FROM Race u " +
            "LEFT JOIN u.edition ed " +
            "LEFT JOIN u.event ev " +
            "WHERE u.deleted = false AND u.id = :raceId")
    Optional<Race> findById(Long raceId);
}