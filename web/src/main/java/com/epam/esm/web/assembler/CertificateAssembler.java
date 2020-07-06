package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler implements Assembler<Long, CertificateDto> {
    private OrderService<OrderDto, Long> orderService;

    public CertificateAssembler(OrderService<OrderDto, Long> orderService) {
        this.orderService = orderService;
    }

    @Override
    public CertificateDto assemble(Long certId, CertificateDto certificateDto) {
        Link linkSelfCert = linkTo(methodOn(CertificateController.class).get(certId)).withSelfRel();
        Link linkToTags = linkTo(methodOn(CertificateController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_TAGS)), certId)).withRel("tags");
//        Long orderId = orderService.getOrderByCertificateId(certId);
        certificateDto.add(linkSelfCert, linkToTags);
//        if (orderId > 0L) {
//            Link linkToAllUserCerts = linkTo(methodOn(OrderController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_CERTS)), orderId)).withRel("certificates");
//            certificateDto.add(linkToAllUserCerts);
//        }

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

        if ((orderId == null || orderId.equals(PARAM_NOT_USED)) && !certificates.isEmpty()) {
            Long certId = certificates.get(0).getId();
            Long foundId = orderService.getOrderByCertificateId(certId);
            if (foundId != null) {
                orderId = foundId;
            }
        }

        Link link = linkTo(methodOn(OrderController.class).getAll(pageable, orderId)).withSelfRel();
        Link linkOrder = linkTo(methodOn(OrderController.class).get(orderId)).withRel("order");

        return CollectionModel.of(certificates, link, linkOrder);
    }
}
