package com.epam.esm.web.assembler;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.controller.CertificateController;
import com.epam.esm.web.controller.TagController;
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
public class TagAssembler implements Assembler<Long, TagDto> {
    private CertificateService<CertificateDto, Long> certificateService;

    public TagAssembler(CertificateService<CertificateDto, Long> certificateService) {
        this.certificateService = certificateService;
    }

    @Override
    public TagDto assemble(Long tagId, TagDto tagDto) {
        int id = tagId.intValue();
        Link linkSelfTag = linkTo(methodOn(TagController.class).get(id)).withSelfRel();
        Long certId = certificateService.getCertIdByTagId(id);
        tagDto.add(linkSelfTag);
        if (id > 0) {
            Link linkToAllCertTags = linkTo(methodOn(CertificateController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_CERTS)), certId)).withRel("tags");
            tagDto.add(linkToAllCertTags);
        }

        return tagDto;
    }

    @Override
    public CollectionModel<TagDto> toCollectionModel(Long certId, List<TagDto> tags, Pageable pageable) {
        if (!tags.isEmpty()) {
            tags.forEach(c -> {
                Link selfLink = linkTo(methodOn(TagController.class).get(c.getId())).withSelfRel();
                c.add(selfLink);
            });
        }

        if ((certId == null || certId.equals(PARAM_NOT_USED)) && !tags.isEmpty()) {
            Integer tagId = tags.get(0).getId();
            Long foundId = certificateService.getCertIdByTagId(tagId);
            if (foundId != null) {
                certId = foundId;
            }
        }

        Link link = linkTo(methodOn(CertificateController.class).getAll(pageable, certId)).withSelfRel();
        Link linkCert = linkTo(methodOn(CertificateController.class).get(certId)).withRel("certificate");

        return CollectionModel.of(tags, link, linkCert);
    }
}
