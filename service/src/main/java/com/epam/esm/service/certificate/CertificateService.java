package com.epam.esm.service.certificate;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.FilterDto;

import java.util.List;

public interface CertificateService<T, E> extends CrudService<T, E> {

    List<T> getAll(FilterDto filterDto);


}
