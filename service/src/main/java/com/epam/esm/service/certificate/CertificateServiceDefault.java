package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceDefault implements CertificateService<CertificateDto> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceDefault.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Long> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;

    public CertificateServiceDefault(CertificateRepository<Certificate, Long> certificateRepository,
                                     CertificateTagRepository certificateTagRepository,
                                     TagRepository<Tag, Long> tagRepository,
                                     CertificateConverter certificateConverter,
                                     TagConverter tagConverter) {
        this.certificateRepository = certificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
    }

    @Override
    public Optional<CertificateDto> getByName(String name) {
        if (name != null) {
            Optional<Certificate> optionalCertificate = certificateRepository.getByName(name);
            if (optionalCertificate.isPresent()) {
                CertificateDto certificateDto = certificateConverter.toDto(optionalCertificate.get());
                addCollection(certificateDto);
                return Optional.ofNullable(certificateDto);
            }
        }
        return Optional.empty();
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
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            if (certificate != null) {
                Optional<Certificate> certificateOptional = certificateRepository.save(certificate);
                if (certificateOptional.isPresent() && certificate.getName() != null) {
                    saveTags(certificateOptional.get());
                    return Optional.ofNullable(certificateConverter.toDto(certificateOptional.get()));
                }
            }
        }
        return Optional.empty();
    }

    private boolean saveTags(Certificate certificate) {
        if (certificate != null && !certificate.getTags().isEmpty()) {
            certificate.getTags().forEach(t -> {
                if (t != null && t.getName() != null) {
                    Optional<Tag> tagOptional = tagRepository.save(t);
                    if(tagOptional.isPresent()){ //new tag save and connect to the certificate
                        certificateTagRepository.saveCertificateTag(certificate.getId(),tagOptional.get().getId());
                    } else{ //existing tag connecting to the certificate
                        Optional<Tag> tagByNameOptional = tagRepository.getByName(t.getName());
                        if (tagByNameOptional.isPresent()){
                            Tag tag = tagByNameOptional.get();
                            t.setId(tag.getId());
                            certificateTagRepository.saveCertificateTag(certificate.getId(),tag.getId());
                        }
                    }
//                    Optional<Tag> optionalTag = tagRepository.getByName(t.getName());
//                    if (!optionalTag.isPresent()) {
//                        optionalTag = tagRepository.save(t);
//                    } else {
//                        optionalTag = tagRepository.getByName(t.getName());
//                    }
//                    optionalTag
//                            .ifPresent(tag ->
//                            {
//                                boolean isSavedCertificateTag = certificateTagRepository
//                                        .saveCertificateTag(certificate.getId(), tag.getId());
//                                if(!isSavedCertificateTag){
//                                    t
//                                }
//
//                            });
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<Certificate> certificateOptional = certificateRepository.get(id);
        if (certificateOptional.isPresent()) {
            CertificateDto certificateDto = certificateConverter.toDto(certificateOptional.get());
            addCollection(certificateDto);
            return Optional.ofNullable(certificateDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            if (certificate != null) {
                Optional<Certificate> certificateOptional = certificateRepository.update(certificate);
                if (certificateOptional.isPresent()){
                    certificate = certificateOptional.get();
                    saveTags(certificate);
                    certificateDto = certificateConverter.toDto(certificate);
                    if (certificateDto != null){
                        addCollection(certificateDto);
                    }
                    return Optional.ofNullable(certificateDto);
                }
            }
        }
        return Optional.ofNullable(certificateDto);
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
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(tagConverter::toDto)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }


}
