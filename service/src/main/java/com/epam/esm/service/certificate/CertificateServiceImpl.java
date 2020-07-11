package com.epam.esm.service.certificate;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.repository.crud.CertificateCrudRepository;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@Transactional
@Getter
public class CertificateServiceImpl implements CertificateService<CertificateDto, Long> {
    private FilterDto filterDto;

    private CertificateCrudRepository certificateRepository;
    private ModelMapper mapper;

    public CertificateServiceImpl(CertificateCrudRepository certificateRepository, ModelMapper mapper) {
        this.certificateRepository = certificateRepository;
        this.mapper = mapper;
    }

    @Override //save without tags
    public Optional<CertificateDto> save(CertificateDto certificateDto) {
        certificateDto.getTags().clear();
        certificateDto.setId(null); //or detached entity passes to persistent and happens exception
        Certificate certificate = mapper.map(certificateDto, Certificate.class);
        certificate = certificateRepository.save(certificate).orElseThrow(() -> new SaveException("Certificate save exception"));
        return get(certificate.getId());
    }

    @Override
    public Optional<CertificateDto> get(Long id) {
        Optional<CertificateDto> certificateDtoOptional = Optional.empty();
        Certificate certificate = certificateRepository.get(id).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));
//        setCorrectTime(certificate);
        certificateDtoOptional = Optional.ofNullable(mapper.map(certificate, CertificateDto.class));

        return certificateDtoOptional;
    }

    @Override
    public Optional<CertificateDto> update(CertificateDto certificateDto) {
        long id = certificateDto.getId();
        Certificate found = certificateRepository.get(certificateDto.getId()).orElseThrow(() -> new NotFoundException("Certificate not found exception, id:" + id));

        found.setName(certificateDto.getName());
        found.setCreation(certificateDto.getCreation());
        found.setModification(certificateDto.getModification());
        found.setPrice(certificateDto.getPrice());
        found.setDescription(certificateDto.getDescription());
        found.setDuration(certificateDto.getDuration());

        Certificate certificate = certificateRepository.update(found).orElseThrow(() -> new SaveException("Certificate save exception"));
        CertificateDto dto = mapper.map(certificate, CertificateDto.class);

        return Optional.ofNullable(dto);
    }

    @Override
    public boolean delete(Long certId) {
        Certificate certificate = certificateRepository.get(certId).orElseThrow(() -> new NotFoundException("Certificate delete: not found exception, id:" + certId));
        certificate.setDeleted(true);
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Certificate update in delete operation exception"));
        return true;
    }

    @Override
    public List<CertificateDto> getAll(FilterDto filterDto) {
        Filter filter = mapper.map(filterDto, Filter.class);
        List<Certificate> certificates = certificateRepository.getAll(filter);
        List<CertificateDto> dtoList = certificates.stream().map(c -> mapper.map(c, CertificateDto.class)).collect(toList());
        filter = certificateRepository.getFilter();
        this.filterDto = mapper.map(filter, FilterDto.class);

        return dtoList;
    }

//    @Override
//    public Page<CertificateDto> getAllByOrderId(Long orderId, Pageable pageable) {
//        Page<Certificate> certificates = certificateRepository.getAllByOrderId(orderId, pageable);
//        if (certificates.getContent().isEmpty()) {
//            return new PageImpl<>(null, pageable, 0);
//        }
//
//        List<CertificateDto> dtoList = certificates.getContent()
//                .stream()
//                .map(o -> certificateConverter.toDto(o))
//                .collect(Collectors.toList());
//        return new PageImpl<CertificateDto>(dtoList, pageable, dtoList.size());
//    }

//    private void setCorrectTime(Certificate certificate) {
//        Instant created = certificateRepository.getCreationById(certificate.getId());
//        if (created != null) {
//            certificate.setCreation(created);
//            certificate.setModification(created);
//        }
//        Instant modified = certificateRepository.getModificationById(certificate.getId());
//        if (modified != null) {
//            certificate.setModification(modified);
//        }
//    }

}
