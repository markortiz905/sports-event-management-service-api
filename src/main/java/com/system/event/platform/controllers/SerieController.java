package com.system.event.platform.controllers;

import com.system.event.platform.dto.SerieDto;
import com.system.event.platform.dto.SerieMapper;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.services.SerieService;
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
public class SerieController {

    @Autowired SerieService serieService;
    @Autowired SerieMapper serieMapper;

    @PostMapping("/event/{eventId}/edition/{editionId}/serie")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<SerieDto> newSerie(@PathVariable Long eventId, @PathVariable Long editionId,
                                      @RequestBody SerieDto serieDto) throws NotFoundServiceException, BadRequestServiceException, ServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(serieService.create(eventId, editionId, serieDto, serieMapper::toEntity, serieMapper::toDto));
    }

    @PutMapping("/event/{eventId}/edition/{editionId}/serie/{serieId}")
    ResponseEntity<SerieDto> updateEdition(@PathVariable Long eventId, @PathVariable Long editionId,
                                           @RequestBody SerieDto serieDto) throws NotFoundServiceException, BadRequestServiceException, ServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serieService.update(eventId, editionId, serieDto, serieMapper::updateEntity, serieMapper::toDto));
    }

    @GetMapping("/event/{eventId}/edition/{editionId}/series")
    ResponseEntity<List<SerieDto>> getAllSerie(@PathVariable Long eventId, @PathVariable Long editionId) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serieService.getAll(eventId, editionId, serieMapper::toDto));
    }

    @GetMapping("/event/{eventId}/edition/{editionId}/serie/{serieId}")
    ResponseEntity<SerieDto> getSerie(@PathVariable Long eventId, @PathVariable Long editionId,
                                      @PathVariable Long serieId) throws BadRequestServiceException, NotFoundServiceException, ServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(serieService.getSerie(eventId, editionId, serieId, serieMapper::toDto));
    }

    @DeleteMapping("/event/{eventId}/edition/{editionId}/serie/{serieId}")
    ResponseEntity<Void> deleteSerie(@PathVariable Long eventId, @PathVariable Long editionId,
                                     @PathVariable Long serieId) throws ServiceException, BadRequestServiceException {
        serieService.delete(eventId, editionId, serieId);
        return ResponseEntity.ok().build();
    }
}
