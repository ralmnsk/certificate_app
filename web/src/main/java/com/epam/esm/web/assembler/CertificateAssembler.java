package com.epam.esm.web.assembler;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;
import com.epam.esm.service.dto.filter.CertificateFilterDto;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CertificateAssembler implements Assembler<Long, CertificateDto, CertificateFilterDto> {
    private CertificateService certificateService;

    public CertificateAssembler(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public CertificateDto assemble(Long certId, CertificateDto certificateDto) {
        Link linkSelfCert = linkTo(methodOn(CertificateController.class).get(certId)).withSelfRel();
        certificateDto.add(linkSelfCert);

        return certificateDto;
    }

    @Override
    public CollectionModel<CertificateDto> toCollectionModel(CertificateFilterDto filter) {
        ListWrapperDto<CertificateDto, CertificateFilterDto> wrapperDto = certificateService.getAll(filter);
        List<CertificateDto> certificates = wrapperDto.getList();
        filter = wrapperDto.getFilterDto();
        if (!certificates.isEmpty()) {
            certificates.forEach(c -> {
                Link selfLink = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
                c.add(selfLink);
            });
        }

        CollectionModel<CertificateDto> collectionModel = CollectionModel.of(certificates);
        if (filter.getOrderId() != null && filter.getOrderId() > 0) {
            Link link = linkTo(methodOn(OrderController.class)
                    .getAllCertificatesByOrderId(
                            filter.getCertificateName(),
                            filter.getPage(),
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getOrderId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )
            ).withRel("order id:" + filter.getOrderId() + " certificates");
            collectionModel.add(link);
            addNextPrevious(collectionModel, filter);
        } else {
            Link link = linkTo(methodOn(CertificateController.class)
                    .getAll(
                            filter.getTagName(),
                            filter.getCertificateName(),
                            filter.getPage(),
                            filter.getSize(),
                            filter.getSortParams()
                    )).withRel("certificates");
            collectionModel.add(link);
        }

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<CertificateDto> collectionModel, AbstractFilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(OrderController.class)
                    .getAllCertificatesByOrderId(
                            filter.getCertificateName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getOrderId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("order id:" + filter.getOrderId() + " certificates previous page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(OrderController.class)
                    .getAllCertificatesByOrderId(
                            filter.getCertificateName(),
                            filter.getPage() + 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getOrderId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("order id:" + filter.getOrderId() + " certificates next page");
            collectionModel.add(link);
        }
    }

}
