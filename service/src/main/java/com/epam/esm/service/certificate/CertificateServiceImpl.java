package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Dto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.UpdateException;
import com.epam.esm.service.validator.FilterValidator;
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

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Integer> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;
    private FilterValidator filterValidator;
    private ModelMapper modelMapper;

    public CertificateServiceImpl(CertificateRepository<Certificate, Long> certificateRepository,
                                  CertificateTagRepository certificateTagRepository,
                                  TagRepository<Tag, Integer> tagRepository,
                                  CertificateConverter certificateConverter,
                                  TagConverter tagConverter,
                                  FilterValidator filterValidator,
                                  ModelMapper modelMapper) {
        this.certificateRepository = certificateRepository;
        this.certificateTagRepository = certificateTagRepository;
        this.tagRepository = tagRepository;
        this.certificateConverter = certificateConverter;
        this.tagConverter = tagConverter;
        this.filterValidator = filterValidator;
        this.modelMapper = modelMapper;
    }

    private void addCollection(CertificateDto certificateDto) {
        if (certificateDto != null) {
            List<TagDto> tagDtos = getTagsByCertificateId(certificateDto.getId());
            if (tagDtos != null && !tagDtos.isEmpty()) {
                certificateDto.getTagDtos().addAll(tagDtos);
            }
        }
    }

    @Override
    public List<CertificateDto> getAll(FilterDto filterDto) {
        FilterDto validFilterDto = FilterValidator.validate(filterDto);
        Filter validFilter = modelMapper.map(validFilterDto, Filter.class);
        List<Certificate> certificates = certificateRepository
                .getAll(validFilter);
        if (certificates != null && !certificates.isEmpty()) {
            return certificates.stream().map(certificateConverter::toDto)
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
            saveTags(certificate);
            CertificateDto savedCertificateDto = certificateConverter.toDto(certificateOptional.get());
            addCollection(savedCertificateDto);
            return Optional.ofNullable(savedCertificateDto);
        }
        return Optional.empty();
    }


    private boolean saveTags(Certificate certificate) {
        if (!certificate.getTags().isEmpty()) {
            certificate.getTags().forEach(t -> {
                if (t != null && t.getName() != null) {
                    Optional<Tag> foundTagOptional = Optional.empty();
                    try {
                        foundTagOptional = tagRepository.getByName(t.getName());
                    } catch (EmptyResultDataAccessException e) {
                        logger.info(this.getClass() + ": TagRepository.getName(): tag not found: " + t);
                    }
                    if (!foundTagOptional.isPresent()) {
                        Optional<Tag> tagOptional = tagRepository.save(t);
                        tagOptional
                                .ifPresent(tag -> {
                                    t.setId(tagOptional.get().getId());
                                    certificateTagRepository
                                            .saveCertificateTag(certificate.getId(), tag.getId());
                                });
                    } else {
                        Tag tag = foundTagOptional.get();
                        t.setId(tag.getId());
                        certificateTagRepository.saveCertificateTag(certificate.getId(), tag.getId());
                    }

                }
            });
            return true;
        }
        return false;
    }

    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<Certificate> certificateOptional = Optional.empty();
        try {
            certificateOptional = certificateRepository.get(id);
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate id = {}", id, e);
            throw new NotFoundException(id, e);
        }
        Optional<CertificateDto> certificateOptionalDto = Optional.empty();
        if (certificateOptional.isPresent()) {
            CertificateDto certificateDto = certificateConverter.toDto(certificateOptional.get());
            addCollection(certificateDto);
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
            throw new UpdateException(certificateDto.getId(), e);
        }

        if (certificateOptional.isPresent()) {
            certificate = certificateOptional.get();
            certificate.getTags().addAll(tags);
            updateTags(certificate);
            certificateDto = certificateConverter.toDto(certificate);

            if (certificateDto != null) {
                addCollection(certificateDto);
            }
            return Optional.ofNullable(certificateDto);
        }

        return Optional.empty();
    }

    private void updateTags(Certificate certificate) {
        List<TagDto> tagsByCertificateId = getTagsByCertificateId(certificate.getId());
        if (tagsByCertificateId != null) {
            tagsByCertificateId
                    .stream()
                    .map(Dto::getId)
                    .map(tagRepository::get)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .filter(t -> !certificate.getTags().contains(t))
                    .forEach(t -> {
                        certificateTagRepository.deleteCertificateTag(certificate.getId(), t.getId());
                    });
        }
        saveTags(certificate);
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
        List<Integer> listTagIds = certificateTagRepository.getTagIdsByCertificateId(id);
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
