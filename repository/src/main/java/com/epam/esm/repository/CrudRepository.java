package com.epam.esm.repository;

import java.util.Optional;

/**
 * The interface Crud repository.
 *
 * @param <T> the type parameter for object(item)
 * @param <E> the type parameter for numeric such as Long, Integer, etc.
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
