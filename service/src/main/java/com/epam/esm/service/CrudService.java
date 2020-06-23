package com.epam.esm.service;

import java.util.Optional;

/**
 * The interface Crud service.
 * The base interface for extension.
 *
 * @param <T> the type parameter for objects(items)
 * @param <E> the type parameter numeric such as Long, Integer, etc.
 */
public interface CrudService<T, E> {

    /**
     * Save optional.
     *
     * @param t the t
     * @return the optional of the t
     */
    Optional<T> save(T t);

    /**
     * Get optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<T> get(E id);

    /**
     * Update optional.
     *
     * @param t the t
     * @return the optional of the t
     */
    Optional<T> update(T t);

    /**
     * Delete method return true if deletion is successful,
     * and false if deletion is unsuccessful.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(E id);

}
