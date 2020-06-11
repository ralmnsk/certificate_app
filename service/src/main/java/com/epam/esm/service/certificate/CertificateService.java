package com.epam.esm.service.certificate;


import com.epam.esm.service.Service;
import com.epam.esm.repository.certificate.FilterDto;

import java.util.List;

public interface CertificateService<T> extends Service<T> {

    List<T> getAll(FilterDto filterDto);


}
