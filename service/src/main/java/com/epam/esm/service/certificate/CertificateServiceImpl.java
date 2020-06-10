package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.certificate.tag.CertificateTagRepository;
import com.epam.esm.repository.exception.UpdateCertificateException;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.CertificateAlreadyExistsException;
import com.epam.esm.service.exception.CertificateNotFoundException;
import com.epam.esm.service.exception.CertificateSaveException;
import com.epam.esm.service.exception.CertificateUpdateException;
import com.epam.esm.service.validator.CertificateValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CertificateServiceImpl implements CertificateService<CertificateDto> {

    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private CertificateRepository<Certificate, Long> certificateRepository;
    private CertificateTagRepository certificateTagRepository;
    private TagRepository<Tag, Long> tagRepository;
    private CertificateConverter certificateConverter;
    private TagConverter tagConverter;
    private CertificateValidator validator;

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
    public void setValidator(CertificateValidator validator) {
        this.validator = validator;
    }

    @Override
    public Optional<CertificateDto> getByName(String name) {
        if (name != null) {
            try {
                Optional<Certificate> optionalCertificate = certificateRepository.getByName(name);
                if (optionalCertificate.isPresent()) {
                    CertificateDto certificateDto = certificateConverter.toDto(optionalCertificate.get());
                    addCollection(certificateDto);
                    return Optional.ofNullable(certificateDto);
                }
            } catch (EmptyResultDataAccessException e) {
                logger.info("There is no such certificate name = {}", name, e);
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
    public List<CertificateDto> getAll(String tagName, String name, String sortByName, String sortByDate) {                                  //Lazy get
        List<Certificate> certificates = certificateRepository.getAll(tagName, name, sortByName, sortByDate);
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
            if (certificate != null && !validator.isExist(certificate)) {
                try {
                    Optional<Certificate> certificateOptional = certificateRepository.save(certificate);
                    if (certificateOptional.isPresent() && certificate.getName() != null) {
                        saveTags(certificateOptional.get());
                        return Optional.ofNullable(certificateConverter.toDto(certificateOptional.get()));
                    }
                } catch (DuplicateKeyException e) {
                    logger.info("This certificate already exists: {}", certificate.getName(), e);
                } catch (NullPointerException e) {
                    logger.info("Certificate save exception", e); //repository gets id with keyHolder
                    throw new CertificateSaveException(certificateDto);
                }
            }
        }
                    throw new CertificateAlreadyExistsException();
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
        try {
            Optional<Certificate> certificateOptional = certificateRepository.get(id);
            if (certificateOptional.isPresent()) {
                CertificateDto certificateDto = certificateConverter.toDto(certificateOptional.get());
                addCollection(certificateDto);
                return Optional.ofNullable(certificateDto);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no certificate id = {}", id, e);
            throw new CertificateNotFoundException(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = certificateConverter.toEntity(certificateDto);

            if (validator.isExist(certificate.getId())) {

                Optional<Certificate> certificateOptional = certificateRepository.update(certificate);

                if (certificateOptional.isPresent()) {
                    certificate = certificateOptional.get();
                    saveTags(certificate);
                    certificateDto = certificateConverter.toDto(certificate);

                    if (certificateDto != null) {
                        addCollection(certificateDto);
                    }
                    return Optional.ofNullable(certificateDto);
                }
            } else {

                CertificateUpdateException ex = new CertificateUpdateException("id == null or could not be found in database");

                logger.info(ex.getMessage());
                throw ex;
            }
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
