package com.epam.esm.service.certificate;


import com.epam.esm.service.Service;

import java.util.List;
import java.util.Optional;

public interface CertificateService<T> extends Service<T> {
    Optional<T> getByName(String name);

    List<T> getAll();
}
