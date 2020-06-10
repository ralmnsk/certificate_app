package com.epam.esm.service.certificate;


import com.epam.esm.service.Service;

import java.util.List;

public interface CertificateService<T> extends Service<T> {

    List<T> getAll(String tagName, String name, String sortByName, String sortByDate);
}
