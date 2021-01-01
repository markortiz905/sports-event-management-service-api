package com.system.event.platform.services;

import com.system.event.platform.entities.Category;
import com.system.event.platform.entities.Event;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;

import java.util.List;

/**
 * @author mark ortiz
 */
public interface CategoryService {
    List<Category> getAll() throws ServiceException, BadRequestServiceException;
    List<Event> getCategoryEvents(Long id) throws NotFoundServiceException, ServiceException, BadRequestServiceException;
    Category getCategory(Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException;
    Category create(String categoryName, String shortDescription) throws ServiceException, BadRequestServiceException;
    Category update(Category category) throws ServiceException, BadRequestServiceException;
    void delete(Category category) throws ServiceException, BadRequestServiceException;
    void delete(Long categoryId) throws ServiceException, BadRequestServiceException;
}
