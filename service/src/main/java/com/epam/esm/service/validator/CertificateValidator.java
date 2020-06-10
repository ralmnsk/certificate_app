package com.epam.esm.service.validator;

import com.epam.esm.model.Certificate;
import com.epam.esm.repository.certificate.CertificateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CertificateValidator {
    private static Logger logger = LoggerFactory.getLogger(CertificateValidator.class);
    private CertificateRepository<Certificate, Long> repository;

    public CertificateValidator(CertificateRepository<Certificate, Long> repository) {
        this.repository = repository;
    }

    public boolean isExist(Certificate certificate) {
        if (certificate.getName() != null) {
            try {
                Optional<Certificate> certOptional = repository.getByName(certificate.getName());
                if (certOptional.isPresent()) {
                    Certificate certFound = certOptional.get();
                    return certFound.equals(certificate);
                }
            } catch (EmptyResultDataAccessException e) {
                logger.info(this.getClass() + ": certificate not found:" + certificate);
            }
        }
        return false;
    }

    public boolean isExist(Long certId) {
        if (certId != null && certId > 0) {
            try {
                Optional<Certificate> certOptional = repository.get(certId);
                return certOptional.isPresent();
            } catch (EmptyResultDataAccessException e) {
                logger.info(this.getClass() + ": certificate id not found: " + certId);
            }
        }
        return false;
    }
}
