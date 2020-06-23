package com.epam.esm.service.converter;

/**
 * The interface Converter for implementation.
 *
 * @param <D> the type parameter
 * @param <E> the type parameter
 */
public interface Converter<D, E> {
    /**
     * To entity e.
     *
     * @param d
     * @return the e
     */
    E toEntity(D d);

    /**
     * To dto d.
     *
     * @param e the e
     * @return the d
     */
    D toDto(E e);
}
