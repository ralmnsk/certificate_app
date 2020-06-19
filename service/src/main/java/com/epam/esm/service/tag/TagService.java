package com.epam.esm.service.tag;


import com.epam.esm.service.CrudService;

import java.util.List;
import java.util.Optional;

/**
 * The interface Tag service.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public interface TagService<T, E> extends CrudService<T, E> {

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
