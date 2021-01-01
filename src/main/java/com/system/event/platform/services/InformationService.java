package com.system.event.platform.services;

import com.system.event.platform.entities.Information;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import lombok.NonNull;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface InformationService {
    List<Information> getAll() throws ServiceException, BadRequestServiceException;
    Information getInformation(@NonNull Long eventId) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Information create(@NonNull Information information) throws ServiceException, BadRequestServiceException;
    Information update(@NonNull Information event) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    void delete(@NonNull Information event) throws ServiceException, BadRequestServiceException;
    void delete(@NonNull Long eventId) throws ServiceException, BadRequestServiceException;
}
