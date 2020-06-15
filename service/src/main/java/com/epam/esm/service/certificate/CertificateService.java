package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.model.Filter;

import java.util.List;

public interface CertificateService<T,E> extends CrudService<T,E> {

    List<T> getAll(Filter filter);


}
