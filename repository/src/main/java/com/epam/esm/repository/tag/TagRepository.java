package com.epam.esm.repository.tag;

import com.epam.esm.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag repository.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public interface TagRepository<T, E> extends CrudRepository<T, E> {

    /**
     * Gets all.
     *
     * @return the all
     */
    List<T> getAll();

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Optional<T> getByName(String name);
}
