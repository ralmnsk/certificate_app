package com.epam.esm.service.certificate;


import com.epam.esm.service.Service;

import java.util.List;

public interface CertificateService<T> extends Service<T> {
    T getByName(String name);
    List<T> getAll();
}
