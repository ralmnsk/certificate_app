package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler implements Assembler<Long, CertificateDto> {
    public CertificateAssembler() {
    }

    @Override
    public CertificateDto assemble(Long certId, CertificateDto certificateDto) {
        Link linkSelfCert = linkTo(methodOn(CertificateController.class).get(certId)).withSelfRel();
        certificateDto.add(linkSelfCert);

        return certificateDto;
    }

    @Override
    public CollectionModel<CertificateDto> toCollectionModel(Long orderId, List<CertificateDto> certificates, Pageable pageable) {
        if (!certificates.isEmpty()) {
            certificates.forEach(c -> {
                Link selfLink = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
                c.add(selfLink);
            });
        }

        Link link = linkTo(methodOn(OrderController.class).getAll(pageable, orderId)).withSelfRel();
        CollectionModel<CertificateDto> collectionModel = CollectionModel.of(certificates, link);

        if (orderId != null && orderId > 0L) {
            Link linkOrder = linkTo(methodOn(OrderController.class).get(orderId)).withRel("order");
            collectionModel.add(linkOrder);
        }

        return collectionModel;
    }
}
