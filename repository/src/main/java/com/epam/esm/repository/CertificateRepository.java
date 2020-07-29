package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.filter.CertificateFilter;
import com.epam.esm.model.wrapper.CertificateListWrapper;

public interface CertificateRepository extends CrudRepository<Certificate, Long> {
    CertificateListWrapper getAll(CertificateFilter filter);

}