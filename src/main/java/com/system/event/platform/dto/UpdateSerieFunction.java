package com.system.event.platform.dto;

import com.system.event.platform.entities.Edition;
import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Serie;

import java.util.function.Function;

/**
 * @author mark ortiz
 */
@FunctionalInterface
public interface UpdateSerieFunction {
    Serie toEntity(Serie race, SerieDto request, Function<Long, Event> getEvent, Function<Long, Edition> getEdition);
}

