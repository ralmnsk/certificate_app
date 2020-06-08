package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface CertificateRepository<T,E> extends Repository<T,E> {

    List<T> getAll();

    Optional<T> getByName(String name);
}
