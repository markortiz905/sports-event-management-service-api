package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Race;

import java.util.function.Function;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface UpdateRaceFunction {
    Race toEntity(Race race, RaceDto request, Function<Long, Event> getEvent, Function<Long, Edition> getEdition);
}

