package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService<TagDto, Integer> {
    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Integer> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;

    public TagServiceImpl(CertificateRepository<Certificate, Long> certificateRepository,
                          CertificateTagRepository certificateTagRepository,
                          TagRepository<Tag, Integer> tagRepository,
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
        try {
            if (name != null) {
                Optional<Tag> optionalTag = tagRepository.getByName(name);
                if (optionalTag.isPresent()) {
                    TagDto tagDto = tagConverter.toDto(optionalTag.get());
                    addCollection(tagDto);
                    return Optional.ofNullable(tagDto);
                }
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such tag name = {}", name, e);
        }
        return Optional.empty();
    }

    private void addCollection(TagDto tagDto) {
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

        tagDto.getCertificateDtos().clear();
        Tag tag = tagConverter.toEntity(tagDto);
        try {
            Optional<Tag> optionalTag = tagRepository.save(tag);
            if (optionalTag.isPresent() && tag.getName() != null) {
                return Optional.ofNullable(tagConverter.toDto(optionalTag.get()));
            }
        } catch (DuplicateKeyException e) {
            logger.info("This tag already exists: {} {}", tag.getName(), e);
            throw new SaveException("This tag already exists: " + tag.getName() + e);
        }
        throw new SaveException("Tag save exception happened");
    }

    @Override
    public Optional<TagDto> get(Integer id) {
        try {
            Optional<Tag> tagOptional = tagRepository.get(id);
            if (tagOptional.isPresent()) {
                TagDto tagDto = tagConverter.toDto(tagOptional.get());
                addCollection(tagDto);
                return Optional.ofNullable(tagDto);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no tag id = {}", id, e);
            throw new NotFoundException(id, e);
        }

        return Optional.empty();
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer tagId) {
        certificateTagRepository
                .getCertificateIdsByTagId(tagId)
                .forEach(certId -> certificateTagRepository
                        .deleteCertificateTag(certId, tagId));
        return tagRepository.delete(tagId);
    }

    public List<CertificateDto> getCertificatesByTagId(Integer id) {
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
