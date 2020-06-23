package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.FilterDto;

import java.util.List;

/**
 * The interface Certificate service for implementation.
 *
 * @param <T> the type parameter for objects(items)
 * @param <E> the type parameter for numeric such as Long, Integer, etc.
 */
public interface CertificateService<T, E> extends CrudService<T, E> {

    /**
     * Gets all.
     *
     * @param filterDto the filter dto to filtrate items
     * @return the all items by filterDto
     */
    List<T> getAll(FilterDto filterDto);

    /**
     * Gets all count.
     *
     * @param filterDto the filter dto
     * @return the count of all items by filterDto
     */
    E getAllCount(FilterDto filterDto);
}
