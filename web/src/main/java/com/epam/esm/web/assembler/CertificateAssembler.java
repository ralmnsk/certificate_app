package com.epam.esm.web.assembler;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.filter.AbstractFilterDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashSet;
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
    public CertificateDto assemble(Long certId, CertificateDto certificateDto, Authentication authentication) {
        Link linkSelf = linkTo(methodOn(CertificateController.class).get(certId, authentication)).withSelfRel();
        certificateDto.add(linkSelf);
        if (isAuthenticationAdmin(authentication)) {
            Link linkCreate = linkTo(methodOn(CertificateController.class)
                    .create(certificateDto, authentication)).withRel("post_create_certificate");
            Link linkUpdate = linkTo(methodOn(CertificateController.class)
                    .update(certificateDto, certId, authentication)).withRel("put_update_certificate");
            Link linkPatch = linkTo(methodOn(CertificateController.class)
                    .update(certId, null, authentication)).withRel("patch_update_certificate");
            Link linkDelete = linkTo(CertificateController.class).slash(certificateDto.getId()).withRel("delete_certificate");
            Link linkAddTags = linkTo(methodOn(CertificateController.class)
                    .addTagToCertificate(certId, new HashSet<Long>(), authentication)).withRel("put_add_tags_to_certificate");
            Link linkDeleteTags = linkTo(methodOn(CertificateController.class)
                    .deleteTagFromCertificate(certId, new HashSet<Long>(), authentication)).withRel("put_remove_tags_from_certificate");
            certificateDto.add(linkCreate, linkUpdate, linkDelete, linkAddTags, linkDeleteTags);
        }

        return certificateDto;
    }


    @Override
    public CertificateDto assemble(Long number, CertificateDto dto) {
        return assemble(number, dto, null);
    }

    @Override
    public CollectionModel<CertificateDto> toCollectionModel(CertificateFilterDto filter) {
        ListWrapperDto<CertificateDto, CertificateFilterDto> wrapperDto = certificateService.getAll(filter);
        List<CertificateDto> certificates = wrapperDto.getList();
        filter = wrapperDto.getFilterDto();
        if (!certificates.isEmpty()) {
            certificates.forEach(c -> {
                Link selfLink = linkTo(methodOn(CertificateController.class).get(c.getId(), null)).withSelfRel();
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
            ).withRel("order_id_" + filter.getOrderId() + "_certificates");
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
                    )).withRel("order_id_" + filter.getOrderId() + "_certificates_previous_page");
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
                    )).withRel("order_id_" + filter.getOrderId() + "_certificates_next_page");
            collectionModel.add(link);
        }
    }

}
