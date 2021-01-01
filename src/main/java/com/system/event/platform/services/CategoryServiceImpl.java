package com.system.event.platform.services;

import com.system.event.platform.entities.Category;
import com.system.event.platform.entities.Event;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author mark ortiz
 */
@Slf4j
@Service
@Primary
@Transactional
@CacheConfig(cacheNames = "categoryCache")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Cacheable(cacheNames = "getAllCategory")
    public List<Category> getAll() throws ServiceException, BadRequestServiceException {
        try {
            return categoryRepository.findAllCategory();
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get list of " + Category.class.getName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getCategoryEventsCache", key = "#id")
    public List<Event> getCategoryEvents(Long id) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return categoryRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Category of id " + id + " not found."))
                    .getEvents().stream().collect(Collectors.toList());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Cacheable(cacheNames = "getCategoryCache", key = "#id")
    public Category getCategory(Long id) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            return categoryRepository.findById(id)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Category of ID " + id + " is not found."));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Event.class.getName() + " id: " + id;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllCategory", allEntries = true)
    })
    public Category create(String categoryName, String shortDescription) throws ServiceException, BadRequestServiceException {
        try {
            return categoryRepository.save(Category.builder()
                    .categoryName(categoryName).shortDescription(shortDescription).build());
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Category.class.getName() + " categoryName: " + categoryName;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllCategory", allEntries = true),
            @CacheEvict(value = "getCategoryCache", key = "#category.id")
    })
    public Category update(Category category)  throws ServiceException, BadRequestServiceException {
        try {
            return categoryRepository.save(category);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to update " + Category.class.getName() + " " + category.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllCategory", allEntries = true),
            @CacheEvict(value = "getCategoryCache", key = "#category.id")
    })
    public void delete(Category category) throws ServiceException, BadRequestServiceException {
        try {
            Optional.ofNullable(category.getId()).orElseThrow(() ->
                    new NotFoundServiceException("category of id null is not found."));
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Category.class.getName() + " " + category.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllCategory", allEntries = true),
            @CacheEvict(value = "getCategoryCache", key = "#catId")
    })
    public void delete(Long catId) throws ServiceException, BadRequestServiceException {
        try {
            categoryRepository.deleteById(catId);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Category.class.getName() + " " + catId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }
}
