package com.epam.esm.repository.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.Repository;

import java.util.List;

public interface CertificateRepository<T> extends Repository<T> {

    List<T> getAll();

    T getByName(String name);
}
