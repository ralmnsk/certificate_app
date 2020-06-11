package com.epam.esm.repository.certificate;

import com.epam.esm.repository.Repository;

import java.util.List;

public interface CertificateRepository<T, E> extends Repository<T, E> {

    List<T> getAll(FilterDto filter);

    List<T> getByName(String name);

}
