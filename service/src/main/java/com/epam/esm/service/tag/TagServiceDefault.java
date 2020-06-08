package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.certificate.CertificateServiceDefault;
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
public class TagServiceDefault implements TagService<TagDto> {
    private static Logger logger = LoggerFactory.getLogger(CertificateServiceDefault.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Long> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;

    public TagServiceDefault(CertificateRepository<Certificate, Long> certificateRepository,
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
    public Optional<TagDto> getByName(String name) {
        if (name != null) {
            Optional<Tag> optionalTag = tagRepository.getByName(name);
            if (optionalTag.isPresent()) {
                TagDto tagDto = tagConverter.toDto(optionalTag.get());
                addCollection(tagDto);
                return Optional.ofNullable(tagDto);
            }
        }
        return Optional.empty();
    }

    private void addCollection(TagDto tagDto) {  //add list to the set of tagDto
        if (tagDto != null) {
            List<CertificateDto> certificateDtos = getCertificatesByTagId(tagDto.getId());
            if (certificateDtos != null && !certificateDtos.isEmpty()) {
                tagDto.getCertificateDtos().addAll(certificateDtos);
            }
        }
    }

    @Override
    public List<TagDto> getAll() {
        List<Tag> tags = tagRepository.getAll();
        if (tags != null && !tags.isEmpty()) {
            return tags.stream().map(tagConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Optional<TagDto> save(TagDto tagDto) {
        if (tagDto != null) {
            tagDto.getCertificateDtos().clear();
            Tag tag = tagConverter.toEntity(tagDto);
            if (tag != null) {
                Optional<Tag> optionalTag = tagRepository.save(tag);
                if (optionalTag.isPresent() && tag.getName() != null) {
//                    saveCertificates(optionalTag.get());
                    return Optional.ofNullable(tagConverter.toDto(optionalTag.get()));
                }
            }
        }
        return Optional.empty();
    }

    private boolean saveCertificates(Tag tag) {
        if (tag != null && !tag.getCertificates().isEmpty()) {
            tag.getCertificates().forEach(c -> {
                if (c != null && c.getName() != null) {
                    Optional<Certificate> optionalCertificate = certificateRepository.getByName(c.getName());
                    if (!optionalCertificate.isPresent()) {
                        optionalCertificate = certificateRepository.save(c);
                        optionalCertificate
                                .ifPresent(certificate ->
                                        certificateTagRepository.saveCertificateTag(certificate.getId(), tag.getId()));
                    }
                }
            });
            return true;
        }
        return false;
    }


    @Override
    public Optional<TagDto> get(Long id) {
        Optional<Tag> tagOptional = tagRepository.get(id);
        if (tagOptional.isPresent()) {
            TagDto tagDto = tagConverter.toDto(tagOptional.get());
            addCollection(tagDto);
            return Optional.ofNullable(tagDto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        //not implemented
        return Optional.empty();
    }

    @Override
    public boolean delete(Long tagId) {
        certificateTagRepository
                .getCertificateIdsByTagId(tagId)
                .forEach(certId -> certificateTagRepository
                        .deleteCertificateTag(certId, tagId));
        return tagRepository.delete(tagId);
    }

    public List<CertificateDto> getCertificatesByTagId(Long id) {
        List<Long> listCertificateIds = certificateTagRepository.getCertificateIdsByTagId(id);
        if (listCertificateIds != null && !listCertificateIds.isEmpty()) {
            return listCertificateIds
                    .stream()
                    .map(certificateRepository::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(certificateConverter::toDto)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
