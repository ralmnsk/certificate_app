package com.epam.esm.service.converter;

import com.epam.esm.model.Certificate;
import com.epam.esm.service.dto.CertificateDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * The type Certificate converter.
 * The CertificateConverter converts {@link CertificateDto} into
 * {@link Certificate} and vice versa.
 */
@Component
public class CertificateConverter implements Converter<CertificateDto, Certificate> {

    private ModelMapper modelMapper;

    /**
     * Instantiates a new Certificate converter.
     *
     * @param modelMapper the model mapper is attached automatically with Spring
     */
    public CertificateConverter(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public Certificate toEntity(CertificateDto certificateDto) {
        if (certificateDto != null) {
            return modelMapper.map(certificateDto, Certificate.class);
        }
        return null;
    }

    @Override
    public CertificateDto toDto(Certificate certificate) {
        if (certificate != null) {
            return modelMapper.map(certificate, CertificateDto.class);
        }
        return null;
    }
}
