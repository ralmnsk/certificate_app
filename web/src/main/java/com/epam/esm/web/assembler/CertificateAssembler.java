package com.epam.esm.web.assembler;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.web.controller.CertificateController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler implements Assembler<Long, CertificateDto> {
    private CertificateService<CertificateDto, Long> certificateService;

    public CertificateAssembler(CertificateService<CertificateDto, Long> certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public CertificateDto assemble(Long certId, CertificateDto certificateDto) {
        Link linkSelfCert = linkTo(methodOn(CertificateController.class).get(certId)).withSelfRel();
        certificateDto.add(linkSelfCert);

        return certificateDto;
    }

    @Override
    public CollectionModel<CertificateDto> toCollectionModel(FilterDto filter) {
        List<CertificateDto> certificates = certificateService.getAll(filter);
        filter = certificateService.getFilterDto();
        if (!certificates.isEmpty()) {
            certificates.forEach(c -> {
                Link selfLink = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
                c.add(selfLink);
            });
        }

        Link link = linkTo(methodOn(CertificateController.class)
                .getAll(filter.getTagName(), filter.getCertificateName(), filter.getPage(), filter.getSize(),
                        filter.getSortParams())).withRel("certificates");
        CollectionModel<CertificateDto> collectionModel = CollectionModel.of(certificates, link);

//        if (filter.getOrderId() != null && filter.getOrderId() > 0L) {
//            Link linkOrder = linkTo(methodOn(OrderController.class).get(orderId)).withRel("order");
//            collectionModel.add(linkOrder);
//        }

        return collectionModel;
    }

}
