package com.epam.esm.repository;

import java.util.List;

public interface CertificateTagRepository {
    List<Long> getCertificateIdsByTagId(Long id);

    List<Long> getTagIdsByCertificateId(Long id);

    boolean saveCertificateTag(Long certId, Long tagId);

    boolean deleteCertificateTag(Long certId, Long tagId);
}
