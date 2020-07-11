package com.epam.esm.web.page;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.web.assembler.CertificateAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class CertificatePageBuilder extends AbstractPageBuilder<CertificateDto, CertificateService<CertificateDto, Long>, CertificateAssembler> {
    private final String EMPTY = "";

    public CertificatePageBuilder(CertificateService<CertificateDto, Long> service, CertificateAssembler certificateAssembler) {
        super(new HashSet<>(Arrays.asList("tag.name", "certificate.name", "creation", "modification", "duration", "price")), service, certificateAssembler);
    }
}