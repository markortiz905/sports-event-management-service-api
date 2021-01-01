package com.system.event.platform.controllers;

import com.system.event.platform.dto.EditionDto;
import com.system.event.platform.dto.EditionMapper;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.services.EditionService;
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
public class EditionController {

    @Autowired private EditionService editionService;
    @Autowired private EditionMapper editionMapper;

    @PostMapping("/event/{eventId}/edition")
    @ResponseStatus(HttpStatus.CREATED) //override default swagger doc status code.
    ResponseEntity<EditionDto> newEdition(@RequestBody EditionDto editionDto, @PathVariable Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(editionService.create(eventId, editionDto,
                        editionMapper::toEntity, editionMapper::toDto));
    }

    @PutMapping("/event/{eventId}/edition/{editionId}")
    ResponseEntity<EditionDto> updateEdition(@PathVariable Long editionId, @RequestBody EditionDto editionDto, @PathVariable Long eventId) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        editionDto = editionDto.toBuilder().id(editionId).build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(editionService.update(eventId, editionDto,
                        editionMapper::updateEntity, //so im gonna need to update the entity with the values from DTO.
                        editionMapper::toDto)); //then transform it back to dto for rest response.
    }

    @GetMapping("/event/{eventId}/editions")
    ResponseEntity<List<EditionDto>> getAllEdition(@PathVariable Long eventId) throws ServiceException, BadRequestServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(editionService.getAll(eventId, editionMapper::toDto));
    }

    @GetMapping("/event/{eventId}/edition/{editionId}")
    ResponseEntity<EditionDto> getEdition(@PathVariable Long eventId, @PathVariable Long editionId) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(editionService.getEditionDto(eventId, editionId, editionMapper::toDto));
    }

    @DeleteMapping("/event/{eventId}/edition/{editionId}")
    ResponseEntity<Void> deleteEdition(@PathVariable Long eventId, @PathVariable Long editionId) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        editionService.delete(eventId, editionId);
        return ResponseEntity.ok().build();
    }
}
