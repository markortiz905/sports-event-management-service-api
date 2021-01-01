package com.system.event.platform.controllers;

import com.system.event.platform.dto.EventDto;
import com.system.event.platform.dto.EventMapper;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author mark ortiz
 */
@RestController
@SuppressWarnings({"unused"})
public class EventController {

    @Autowired private EventService eventService;
    @Autowired private EventMapper eventMapper;

    @PostMapping("/event/{eventName}")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<EventDto> newEventByName(@PathVariable String eventName) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.create(eventName, eventMapper::toDto));
    }

    @PostMapping("/event")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<EventDto> newEvent(@RequestBody EventDto eventRequest) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventService.create(
                        eventMapper.toEntity(
                                eventRequest.toBuilder()
                                .id(null)//id are auto generated.
                                .information(eventRequest.getInformation().toBuilder().id(null).build())
                                .build()),
                        eventMapper::toDto));
    }

    @PutMapping("/event/{eventId}")
    ResponseEntity<EventDto> updateEvent(@PathVariable Long eventId, @RequestBody EventDto eventRequest) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        eventRequest = eventRequest.toBuilder().id(eventId).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.update(eventRequest,
                        eventMapper::updateEntityFromRequest,
                        eventMapper::toDto));
    }

    @GetMapping("/events")
    ResponseEntity<List<EventDto>> getAlEvents() throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getAllEventDtos(eventMapper::toDto));
    }

    @GetMapping("/event/{eventId}")
    ResponseEntity<EventDto> getEvent(@PathVariable Long eventId) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(eventService.getEvent(eventId, eventMapper::toDto));
    }

    @DeleteMapping("/event/{eventId}")
    ResponseEntity<Void>  deleteEvent(@PathVariable Long eventId) throws ServiceException, BadRequestServiceException {
        eventService.delete(eventId);
        return ResponseEntity.ok().build();
    }
}
