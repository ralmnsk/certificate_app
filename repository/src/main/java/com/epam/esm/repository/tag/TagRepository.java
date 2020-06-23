package com.epam.esm.repository.tag;

import com.epam.esm.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 *
 * @param <T> the type parameter for object (items)
 * @param <E> the type parameter for numeric such as Long, Integer, etc.
 */
public interface TagRepository<T, E> extends CrudRepository<T, E> {

    /**
     * Gets all.
     *
     * @return the all items
     */
    List<T> getAll();

    /**
     * Gets by name.
     *
     * @param name the name of the tag
     * @return the by name of the tag
     */
    Optional<T> getByName(String name);
}
