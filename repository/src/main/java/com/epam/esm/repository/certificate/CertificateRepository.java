package com.epam.esm.repository.certificate;

import com.epam.esm.model.Filter;
import com.epam.esm.repository.CrudRepository;

import java.util.List;

public interface CertificateRepository<T, E> extends CrudRepository<T, E> {

    List<T> getAll(Filter filter);

    List<T> getByName(String name);

    E getAllCount(Filter filter);

    List<Long> getCertificateIdsByTagId(Integer id);

    List<Integer> getTagIdsByCertificateId(Long id);

    boolean saveCertificateTag(Long certId, Integer tagId);

    boolean deleteCertificateTag(Long certId, Integer tagId);

}
