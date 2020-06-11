package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.repository.certificate.FilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.certificate.CertificateNotFoundException;
import com.epam.esm.service.exception.certificate.CertificateUpdateException;
import com.epam.esm.service.validator.FilterValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService<CertificateDto> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Long> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;

    public CertificateServiceImpl(CertificateRepository<Certificate, Long> certificateRepository,
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

    @Autowired
    private FilterValidator filterValidator;



    private void addCollection(CertificateDto certificateDto) {  //add list to the set of certificateDto
        if (certificateDto != null) {
            List<TagDto> tagDtos = getTagsByCertificateId(certificateDto.getId());
            if (tagDtos != null && !tagDtos.isEmpty()) {
                certificateDto.getTagDtos().addAll(tagDtos);
            }
        }
    }

    @Override
    public List<CertificateDto> getAll(FilterDto filter) {                                  //Lazy get
        FilterDto validFilter = filterValidator.validate(filter);
        List<String> params = validFilter.getParams();

        List<Certificate> certificates = certificateRepository
                .getAll(validFilter);
        if (certificates != null && !certificates.isEmpty()) {
            return certificates.stream().map(certificateConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null; //Optional ?
    }

    @Override
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            if (certificate != null) {
//                try {
                Optional<Certificate> certificateOptional = certificateRepository.save(certificate);
                if (certificateOptional.isPresent()) {
                    saveTags(certificate);
                    CertificateDto savedCertificateDto = certificateConverter.toDto(certificateOptional.get());
                    addCollection(savedCertificateDto);
                    return Optional.ofNullable(savedCertificateDto);
                }
//                } catch (DuplicateKeyException e) {
//                    logger.info("This certificate already exists: {}", certificate.getName(), e);
//                }
//                catch (NullPointerException e) {
//                    logger.info("Certificate save exception", e); //repository gets id with keyHolder
//                    throw new CertificateSaveException(certificateDto);
//                }
            }
        }
     return Optional.empty();
    }


    private boolean saveTags(Certificate certificate) {
        if (certificate != null && !certificate.getTags().isEmpty()) {
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
                        if (tagOptional.isPresent()) { //new tag save and connect to the certificate
                            certificateTagRepository.saveCertificateTag(certificate.getId(), tagOptional.get().getId());
                        }
                    } else { //existing tag connecting to the certificate
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
            throw new CertificateNotFoundException(id,e);
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
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);
            Set<Tag> tags = certificate.getTags();
            Optional<Certificate> certificateOptional = Optional.empty();
            try{
                certificateOptional = certificateRepository.update(certificate);
            } catch (EmptyResultDataAccessException e) {
                logger.info("There is no certificate with id = {}", certificateDto.getId(), e);
                throw new CertificateUpdateException(certificateDto.getId(),e);
            }

            if (certificateOptional.isPresent()) {
                certificate = certificateOptional.get();
                certificate.getTags().addAll(tags);
                saveTags(certificate);
                certificateDto = certificateConverter.toDto(certificate);

                if (certificateDto != null) {
                    addCollection(certificateDto);
                }
                return Optional.ofNullable(certificateDto);
            }

//            CertificateUpdateException ex = new CertificateUpdateException("id == null or could not be found in database");
//
//            logger.info(ex.getMessage());
//            throw ex;
        }
        return Optional.empty();
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
