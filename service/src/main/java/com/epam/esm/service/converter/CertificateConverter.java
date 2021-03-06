package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CertificateConverter implements Converter<CertificateDto, Certificate> {

    private ModelMapper modelMapper;

    public CertificateConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Certificate toEntity(CertificateDto certificateDto) {
        if (certificateDto != null) {
            Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
            if (certificate != null && !certificateDto.getTagDtos().isEmpty()) {
                Set<TagDto> tagDtos = certificateDto.getTagDtos();
                tagDtos.forEach(tagDto -> {
                    Tag tag = modelMapper.map(tagDto, Tag.class);
                    if (tag != null) {
                        certificate.getTags().add(tag);
                    }
                });
            }
            return certificate;
        }
        return null;
    }

    @Override
    public CertificateDto toDto(Certificate certificate) {
        if (certificate != null) {
            CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
            if (certificateDto != null && !certificate.getTags().isEmpty()) {
                certificate.getTags().forEach(tag -> {
                    TagDto tagDto = modelMapper.map(tag, TagDto.class);
                    if (tagDto != null) {
                        certificateDto.getTagDtos().add(tagDto);
                    }
                });
            }
            return certificateDto;
        }
        return null;
    }
}
