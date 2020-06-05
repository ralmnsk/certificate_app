package com.epam.esm.repository;

import com.epam.esm.model.Certificate;

import java.util.List;

public interface CertificateRepository<T> extends Repository<T> {
    List<Certificate> getAll();

    Certificate getByName(String name);
}
