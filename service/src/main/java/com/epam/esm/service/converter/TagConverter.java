package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class TagConverter implements Converter<TagDto, Tag> {
    private ModelMapper modelMapper;

    public TagConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Tag toEntity(TagDto tagDto) {
        if (tagDto != null) {
            Tag tag = modelMapper.map(tagDto, Tag.class);
            if (tag != null && !tagDto.getCertificateDtos().isEmpty()) {
                tagDto.getCertificateDtos().forEach(certificateDto -> {
                    Certificate certificate = modelMapper.map(certificateDto, Certificate.class);
                    if (certificate != null) {
                        tag.getCertificates().add(certificate);
                    }
                });
            }
            return tag;
        }
        return null;
    }


    @Override
    public TagDto toDto(Tag tag) {
        if (tag != null) {
            TagDto tagDto = modelMapper.map(tag, TagDto.class);
            if (tagDto != null && !tag.getCertificates().isEmpty()) {
                tag.getCertificates().forEach(certificate -> {
                    CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);
                    if (certificateDto != null) {
                        tagDto.getCertificateDtos().add(certificateDto);
                    }
                });
            }
            return tagDto;
        }
        return null;
    }
}
