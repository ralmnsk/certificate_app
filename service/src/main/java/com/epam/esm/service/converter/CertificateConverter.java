package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.service.dto.CertificateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CertificateConverter implements Converter<CertificateDto, Certificate> {
    private ModelMapper mapper;

    public CertificateConverter(ModelMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Certificate toEntity(CertificateDto certificateDto) {
//        Set<Tag> tags = null;
//        Set<TagDto> tagDtos = certificateDto.getTags();
//        if (!tagDtos.isEmpty()) {
//            certificateDto.setTags(null);
//            tags = tagDtos.stream().map(t -> mapper.map(t, Tag.class)).collect(Collectors.toSet());
//        }
        Certificate certificate = mapper.map(certificateDto, Certificate.class);
//        if (tags != null) {
//            certificate.setTags(tags);
//        }
        return certificate;
    }

    @Override
    public CertificateDto toDto(Certificate certificate) {
        CertificateDto certificateDto = mapper.map(certificate, CertificateDto.class);
        return certificateDto;
    }
}
