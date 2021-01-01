package com.system.event.platform.repositories;

import com.system.event.platform.entities.Information;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mark ortiz
 */
public interface InformationRepository extends CrudRepository<Information, Long> {

    default List<Information> findAll() {
        List<Information> info = new ArrayList<>();
        findAll().forEach(info::add);
        return info;
    }
}
