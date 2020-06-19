package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.FilterDto;

import java.util.List;

/**
 * The interface Certificate service.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public interface CertificateService<T, E> extends CrudService<T, E> {

    /**
     * Gets all.
     *
     * @param filterDto the filter dto
     * @return the all
     */
    List<T> getAll(FilterDto filterDto);

    /**
     * Gets all count.
     *
     * @param filterDto the filter dto
     * @return the all count
     */
    E getAllCount(FilterDto filterDto);
}
