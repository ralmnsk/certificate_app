package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.InconsistencyIdException;
import com.epam.esm.service.exception.NewTagHasIdInCertificateException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.UpdateException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * The type Certificate service implementation.
 * Tag service uses {@link CertificateRepository}, {@link TagRepository}
 * for database crud operations with models(entities),
 * {@link TagConverter}, {@link CertificateConverter} for converting
 * DTO into model and vice versa.
 */
@Service
@Transactional
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private TagRepository<Tag, Integer> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;
    private ModelMapper modelMapper;

    /**
     * Instantiates a new Certificate service.
     * Spring injects parameters into constructor automatically.
     *
     * @param certificateRepository the certificate repository
     * @param tagRepository         the tag repository
     * @param certificateConverter  the certificate converter
     * @param tagConverter          the tag converter
     * @param modelMapper           the model mapper
     */
    public CertificateServiceImpl(CertificateRepository<Certificate, Long> certificateRepository,
                                  TagRepository<Tag, Integer> tagRepository,
                                  CertificateConverter certificateConverter,
                                  TagConverter tagConverter,
                                  ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<CertificateDto> getAll(FilterDto filterDto) {
        Filter filter = modelMapper.map(filterDto, Filter.class);
        List<Certificate> certificates = certificateRepository.getAll(filter);
        if (certificates != null && !certificates.isEmpty()) {
            return certificates
                    .stream()
                    .map(Certificate::getId)
                    .map(this::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        Certificate certificate = certificateConverter.toEntity(certificateDto);
        Optional<Certificate> certificateOptional = certificateRepository.save(certificate);
        if (certificateOptional.isPresent()) {
            certificate.setId(certificateOptional.get().getId());
            CertificateDto savedCertificateDto = certificateConverter.toDto(certificateOptional.get());
            List<TagDto> tagsDtos = updateTags(certificate);
            if (tagsDtos != null) {
                savedCertificateDto.getTags().addAll(tagsDtos);
            }
            return Optional.ofNullable(savedCertificateDto);
        }
        return Optional.empty();
    }


    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<Certificate> certificateOptional = Optional.empty();
        try {
            certificateOptional = certificateRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate id = {}", id, e);
            throw new NotFoundException(id);
        }
        Optional<CertificateDto> certificateOptionalDto = Optional.empty();
        if (certificateOptional.isPresent()) {
            CertificateDto certificateDto = certificateConverter.toDto(certificateOptional.get());
            addTagsToCertificate(certificateDto);
            return Optional.ofNullable(certificateDto);
        }
        return certificateOptionalDto;
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        Certificate certificate = certificateConverter.toEntity(certificateDto);
        Set<Tag> tags = certificate.getTags();
        Optional<Certificate> certificateOptional = Optional.empty();

        try {
            certificateOptional = certificateRepository.update(certificate);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate with id = {}", certificateDto.getId(), e);
            throw new UpdateException(certificateDto.getId());
        }

        if (certificateOptional.isPresent()) {
            certificate = certificateOptional.get();
            certificate.getTags().addAll(tags);
            List<TagDto> tagsDtos = updateTags(certificate);
            certificateDto = certificateConverter.toDto(certificate);
            certificateDto.getTags().clear();
            if (tagsDtos != null) {
                certificateDto.getTags().addAll(tagsDtos);
            }
            return Optional.ofNullable(certificateDto);
        }

        return Optional.empty();
    }


    @Override
    public boolean delete(Long certId) {
        return certificateRepository.delete(certId);
    }

    /**
     * Gets tags by certificate id.
     *
     * @param id the id
     * @return the tags by certificate id
     */
    public List<TagDto> getTagsByCertificateId(Long id) {
        List<Tag> tags = tagRepository.getTagsByCertificateId(id);
        if (tags != null && !tags.isEmpty()) {
            return tags
                    .stream()
                    .map(tagConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Long getAllCount(FilterDto filterDto) {
        Filter filter = modelMapper.map(filterDto, Filter.class);
        return certificateRepository.getAllCount(filter);
    }

    private boolean saveTags(Certificate certificate) {
        if (certificate.getTags().isEmpty()) {
            return false;
        }

        certificate.getTags()
                .stream()
                .filter(Objects::nonNull)
                .filter(t -> t.getName() != null || (t.getId() != null && t.getId() > 0))
                .forEach(t -> {
                    Optional<Tag> foundTagOptional = getOptionalTag(t);
                    if (!foundTagOptional.isPresent()) {
                        isIdInNewTag(t);
                        saveNewTag(certificate, t);
                    } else {
                        saveExistingTag(certificate, t, foundTagOptional);
                    }

                });
        return true;
    }

    private boolean isIdInNewTag(Tag t) {
        if (t.getId() != null && t.getId() > 0) {
            throw new NewTagHasIdInCertificateException("New Tag can not have id because it doesn't present in a database, id:" + t.getId() + ", name:" + t.getName());
        }
        return false;
    }

    private Optional<Tag> getOptionalTag(Tag t) {
        Optional<Tag> foundTagOptional = Optional.empty();
        try {
            if (t.getId() != null && t.getId() > 0) {
                foundTagOptional = tagRepository.get(t.getId());
            }
            foundTagOptional.ifPresent(tag -> isEqualsTags(t, tag));
            if (t.getName() != null) {
                foundTagOptional = tagRepository.getByName(t.getName());
            }
            foundTagOptional.ifPresent(tag -> isEqualsTags(t, tag));
        } catch (EmptyResultDataAccessException e) {
            logger.info(this.getClass() + ": TagRepository.getName(): tag not found: " + t);
        }
        return foundTagOptional;
    }

    private void saveNewTag(Certificate certificate, Tag t) {
        Optional<Tag> tagOptional = tagRepository.save(t);
        tagOptional
                .ifPresent(tag -> {
                    t.setId(tagOptional.get().getId());
                    certificateRepository
                            .saveCertificateTag(certificate.getId(), tag.getId());
                });
    }

    private void saveExistingTag(Certificate certificate, Tag t, Optional<Tag> foundTagOptional) {
        Tag tag = foundTagOptional.get();
        if (isEqualsTags(t, tag)) {
            t.setId(tag.getId());
            certificateRepository.saveCertificateTag(certificate.getId(), tag.getId());
        }
    }

    private boolean isEqualsTags(Tag t, Tag tag) {
        if (t.getId() != null && t.getId() > 0 && t.getId().equals(tag.getId())) {
            if (t.getName() != null && t.getName().equals(tag.getName())) {
                return true;
            }
            if (t.getName() == null) {
                return true;
            }
        }
        if ((t.getId() == null || t.getId() == 0) && t.getName().equals(tag.getName())) {
            return true;
        }
        throw new InconsistencyIdException("Inconsistency of id and name. Get the tag name by id or remove id:" + t.getId());
    }


    private void addTagsToCertificate(CertificateDto certificateDto) {
        if (certificateDto != null) {
            List<TagDto> tagDtos = getTagsByCertificateId(certificateDto.getId());
            if (tagDtos != null && !tagDtos.isEmpty()) {
                tagDtos
                        .stream()
                        .filter(t -> certificateDto.getTags()
                                .stream()
                                .noneMatch(tag -> t.getName().equals(tag.getName())))
                        .forEach(t -> certificateDto.getTags().add(t));
            }
        }
    }

    private List<TagDto> updateTags(Certificate certificate) {
        tagRepository.removeTagsByCertificateId(certificate.getId());
        saveTags(certificate);
        certificate.getTags().clear();
        return getTagsByCertificateId(certificate.getId());
    }
}
