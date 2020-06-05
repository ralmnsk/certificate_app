package com.epam.esm.service;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.CertificateTagRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceDefault implements CertificateService<CertificateDto> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceDefault.class);
    private CertificateRepository<Certificate> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;

    public CertificateServiceDefault(CertificateRepository<Certificate> certificateRepository,
                                     CertificateTagRepository certificateTagRepository,
                                     TagRepository<Tag> tagRepository,
                                     CertificateConverter certificateConverter,
                                     TagConverter tagConverter) {
        this.certificateRepository = certificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
    }

    @Override
    public CertificateDto getByName(String name) {
        if (name != null) {
            Certificate certificate = certificateRepository.getByName(name);
            if (certificate != null) {
                CertificateDto certificateDto = certificateConverter.toDto(certificate);
                addCollection(certificateDto);
                return certificateDto;
            }
        }
        return null;
    }

    private void addCollection(CertificateDto certificateDto) {  //add list to the set of certificateDto
        if (certificateDto != null) {
            List<TagDto> tagDtos = getTagsByCertificateId(certificateDto.getId());
            if (tagDtos != null && !tagDtos.isEmpty()) {
                certificateDto.getTagDtos().addAll(tagDtos);
            }
        }
    }

    @Override
    public List<CertificateDto> getAll() {                                  //Lazy get
        List<Certificate> certificates = certificateRepository.getAll();
        if (certificates != null && !certificates.isEmpty()) {
            return certificates.stream().map(certificateConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean save(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            if (certificate != null) {
                boolean isSaved = certificateRepository.save(certificate);
                if (isSaved && certificate.getName() != null) {
                    certificate = certificateRepository.getByName(certificate.getName());
                    saveTags(certificate);
                }
                return isSaved;
            }
        }
        return false;
    }

    private boolean saveTags(Certificate certificate) {
        if (certificate != null && !certificate.getTags().isEmpty()) {
            certificate.getTags().forEach(t -> {
                if (t != null && t.getName() != null) {
                    Tag tag = tagRepository.getByName(t.getName());
                    if (tag == null) {
                        if (tagRepository.save(t)) {
                            tag = tagRepository.getByName(t.getName());
                            certificateTagRepository
                                    .saveCertificateTag(certificate.getId(),
                                            tag.getId());
                        }

                    }
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public CertificateDto get(Long id) {
        Certificate certificate = certificateRepository.get(id);
        if (certificate != null) {
            CertificateDto certificateDto = certificateConverter.toDto(certificate);
            addCollection(certificateDto);
            return certificateDto;
        }
        return null;
    }

    @Override
    public boolean update(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            boolean isUpdated = certificateRepository.update(certificate);
            if (isUpdated && certificate.getName() !=null) {
                certificate = certificateRepository.getByName(certificate.getName());
                saveTags(certificate);
            }
            return isUpdated;
        }
        return false;
    }

    @Override
    public boolean delete(Long certId) {
        certificateTagRepository
                .getTagIdsByCertificateId(certId)
                .forEach(tagId -> certificateTagRepository
                        .deleteCertificateTag(certId, tagId));
        return certificateRepository.delete(certId);
    }

    public List<TagDto> getTagsByCertificateId(Long id) {
        List<Long> listTagIds = certificateTagRepository.getTagIdsByCertificateId(id);
        if (listTagIds != null && !listTagIds.isEmpty()) {
            return listTagIds
                    .stream()
                    .map(tagRepository::get)
                    .filter(Objects::nonNull)
                    .map(tagConverter::toDto)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }


}
