package com.epam.esm.service;


import com.epam.esm.model.Certificate;
import java.util.List;

public interface CertificateService<T> extends Service<T> {
    T getByName(String name);
    List<T> getAll();
}
