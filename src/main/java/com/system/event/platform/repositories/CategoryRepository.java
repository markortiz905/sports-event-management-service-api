package com.system.event.platform.repositories;

import com.system.event.platform.entities.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;
/**
 * @author mark ortiz
 */
public interface CategoryRepository extends CrudRepository<Category, Long>  {

    default List<Category> findAllCategory() {
        List<Category> categories = new ArrayList<>();
        findAll().forEach(categories::add);
        return categories;
    }
}
