package com.system.event.platform.services;

import com.system.event.platform.entities.Information;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.repositories.InformationRepository;
import lombok.NonNull;
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

/**
 * @author mark ortiz
 */
@Slf4j
@Service
@Primary
@Transactional(rollbackFor = Exception.class)
@CacheConfig(cacheNames = "eventInfoCache")
public class InformationServiceImpl implements InformationService {
    @Autowired private InformationRepository informationRepository;

    @Override
    @Cacheable(cacheNames = "getAllEventInformation")
    public List<Information> getAll() throws ServiceException, BadRequestServiceException {
        return informationRepository.findAll();
    }

    @Override
    @Cacheable(cacheNames = "getInformationCache", key = "#infoId")
    public Information getInformation(@NonNull Long infoId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        try {
            return informationRepository.findById(infoId)
                    .orElseThrow(() ->
                            new NotFoundServiceException("Info of id " + infoId + " not found."));
        } catch (NotFoundServiceException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to get " + Information.class.getName() + " id: " + infoId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventInformation", allEntries = true)
    })
    public Information create(@NonNull Information information) throws ServiceException, BadRequestServiceException {
        try {
            return informationRepository.save(information);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Information.class.getName() + " getShortName: " + information.getShortName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventInformation", allEntries = true),
            @CacheEvict(value = "getInformationCache", key = "#information.id")
    })
    public Information update(@NonNull Information information) throws ServiceException, BadRequestServiceException, NotFoundServiceException {
        try {
            Optional.ofNullable(information.getId())
                    .orElseThrow( () ->
                            new BadRequestServiceException("Information is invalid with null id."));
            informationRepository.findById(information.getId())
                    .orElseThrow(() -> new NotFoundServiceException("Information of id " + information.getId() + " is not found."));
            return informationRepository.save(information);
        } catch (BadRequestServiceException | NotFoundServiceException e) { throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to create " + Information.class.getName() + " getShortName: " + information.getShortName();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventInformation", allEntries = true),
            @CacheEvict(value = "getInformationCache", key = "#information.id")
    })
    public void delete(@NonNull Information information) throws ServiceException, BadRequestServiceException {
        try {
            Optional.ofNullable(information.getId()).orElseThrow(() ->
                    new NotFoundServiceException("information of id null is not found."));
            informationRepository.delete(information);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Information.class.getName() + " " + information.getId();
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "getAllEventInformation", allEntries = true),
            @CacheEvict(value = "getInformationCache", key = "#infoId")
    })
    public void delete(@NonNull Long infoId) throws ServiceException, BadRequestServiceException {
        try {
            informationRepository.deleteById(infoId);
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestServiceException(e, "Request Not Completed");
        } catch (Exception e) {
            String err = "Failed to delete " + Information.class.getName() + " " + infoId;
            log.error(err, e);
            throw new ServiceException(e, err);
        }
    }
}
