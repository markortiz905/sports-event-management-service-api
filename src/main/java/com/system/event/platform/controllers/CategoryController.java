package com.system.event.platform.controllers;

import com.system.event.platform.entities.Category;
import com.system.event.platform.exception.BadRequestServiceException;
import com.system.event.platform.exception.NotFoundServiceException;
import com.system.event.platform.exception.ServiceException;
import com.system.event.platform.services.CategoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author mark ortiz
 */
@RestController
@SuppressWarnings({"unused"})
public class CategoryController {

    private static final Logger logger = LogManager.getLogger(CategoryController.class);

    @Autowired
    private CategoryService categoryService;
    
    @PostMapping("/category/{categoryName}/{shortDescription}")
    ResponseEntity<Category> newEvent(@PathVariable String categoryName, @PathVariable String shortDescription) throws ServiceException, BadRequestServiceException {
        return ResponseEntity.ok(categoryService.create(categoryName, shortDescription));
    }

    @GetMapping("/category/{categoryId}")
    ResponseEntity<Category> getEvent(@PathVariable Long categoryId) throws NotFoundServiceException, ServiceException, BadRequestServiceException {
        return ResponseEntity.ok(categoryService.getCategory(categoryId));
    }

    @GetMapping("/category")
    ResponseEntity<List<Category>> getAllEvent() throws ServiceException, BadRequestServiceException {
        return ResponseEntity.ok(categoryService.getAll());
    }
}
