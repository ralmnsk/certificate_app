package com.epam.esm.repository.certificate;

import com.epam.esm.model.Filter;
import com.epam.esm.repository.CrudRepository;

import java.util.List;

/**
 * The interface Certificate repository.
 *
 * @param <T> the type parameter for objects(items)
 * @param <E> the type parameter for numeric such as Long, Integer, etc.
 */
public interface CertificateRepository<T, E> extends CrudRepository<T, E> {

    /**
     * Gets all.
     *
     * @param filter the filter
     * @return the all items
     */
    List<T> getAll(Filter filter);

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name of an item
     */
    List<T> getByName(String name);

    /**
     * Gets all count.
     *
     * @param filter the filter
     * @return the all count
     */
    E getAllCount(Filter filter);

    /**
     * Save certificate tag boolean.
     *
     * @param certId the cert id
     * @param tagId  the tag id
     * @return the boolean
     */
    boolean saveCertificateTag(Long certId, Integer tagId);

    /**
     * Delete certificate tag boolean.
     *
     * @param certId the cert id
     * @param tagId  the tag id
     * @return the boolean
     */
    boolean deleteCertificateTag(Long certId, Integer tagId);

}
