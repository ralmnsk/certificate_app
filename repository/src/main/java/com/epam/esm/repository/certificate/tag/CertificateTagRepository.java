package com.epam.esm.repository.certificate.tag;

import java.util.List;

public interface CertificateTagRepository {
    List<Long> getCertificateIdsByTagId(Integer id);

    List<Integer> getTagIdsByCertificateId(Long id);

    boolean saveCertificateTag(Long certId, Integer tagId);

    boolean deleteCertificateTag(Long certId, Integer tagId);
}
