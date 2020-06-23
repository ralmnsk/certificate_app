package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag service for implementation.
 *
 * @param <T> the type parameter for object(item)
 * @param <E> the type parameter for numeric such as Long, Integer, etc.
 */
public interface TagService<T, E> extends CrudService<T, E> {

    /**
     * Gets all.
     *
     * @return the list of all items
     */
    List<T> getAll();

    /**
     * Gets by name.
     *
     * @param name the name of the tag
     * @return the optional of tag by tag name
     */
    Optional<T> getByName(String name);
}
