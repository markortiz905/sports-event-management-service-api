package com.system.event.platform.controllers;

import com.system.event.platform.dto.RaceDto;
import com.system.event.platform.dto.RaceMapper;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.services.RaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mark ortiz
 */
@RestController
@SuppressWarnings({"unused"})
public class RaceController {

    @Autowired private RaceService raceService;
    @Autowired private RaceMapper raceMapper;

    @PostMapping("/event/{eventId}/edition/{editionId}/race")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<RaceDto> newRace(@PathVariable Long eventId, @PathVariable Long editionId, @RequestBody RaceDto raceDto) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(raceService.create(eventId, editionId, raceDto,
                        raceMapper::toEntity, raceMapper::toDto));
    }

    @PutMapping("/event/{eventId}/edition/{editionId}/race/{raceId}")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<RaceDto> updateRace(@PathVariable Long eventId, @PathVariable Long editionId,
                                       @PathVariable Long raceId, @RequestBody RaceDto raceDto) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(raceService.update(eventId, editionId, raceId, raceDto,
                        raceMapper::updateEntity,
                        raceMapper::toDto));
    }

    @GetMapping("/event/{eventId}/edition/{editionId}/races")
    ResponseEntity<List<RaceDto>> getAllRace(@PathVariable Long eventId, @PathVariable Long editionId) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(raceService.getAll(eventId, editionId, raceMapper::toDto));
    }

    @GetMapping("/event/{eventId}/edition/{editionId}/race/{raceId}")
    ResponseEntity<RaceDto> getRace(@PathVariable Long eventId, @PathVariable Long editionId, @PathVariable Long raceId) throws BadRequestServiceException, NotFoundServiceException, ServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(raceService.getRace(eventId, editionId, raceId, raceMapper::toDto));
    }

    @DeleteMapping("/event/{eventId}/edition/{editionId}/race/{raceId}")
    ResponseEntity<Void> deleteRace(@PathVariable Long eventId, @PathVariable Long editionId, @PathVariable Long raceId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        raceService.delete(eventId, editionId, raceId);
        return ResponseEntity.ok().build();
    }
}
