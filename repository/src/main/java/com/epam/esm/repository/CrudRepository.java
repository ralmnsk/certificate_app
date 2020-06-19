package com.epam.esm.repository;

import java.util.Optional;

/**
 * The interface Crud repository.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public interface CrudRepository<T, E> {

    /**
     * Save optional.
     *
     * @param t the t
     * @return the optional
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
     * @return the optional
     */
    Optional<T> update(T t);

    /**
     * Delete boolean.
     *
     * @param id the id
     * @return the boolean
     */
    boolean delete(E id);


}
