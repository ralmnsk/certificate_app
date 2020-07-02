package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.UpdateException;
import com.epam.esm.service.tag.TagService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.DEFAULT_PAGE_NUMBER;
import static com.epam.esm.web.controller.ControllerConstants.DEFAULT_PAGE_SIZE;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private CertificateService<CertificateDto, Long> certificateService;
    private TagService<TagDto, Integer> tagService;
    private ObjectMapper objectMapper;

    public CertificateController(CertificateService<CertificateDto, Long> certificateService, TagService<TagDto, Integer> tagService, ObjectMapper objectMapper) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<CertificateDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        List<CertificateDto> certificates = certificateService.getAll(pageable).getContent();

        certificates.stream().forEach(c -> {
            Link linkSelf = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
            Link linkCerts = linkTo(methodOn(CertificateController.class).getAll(pageable, c.getId())).withRel("tags");
            c.add(linkSelf);
            c.add(linkCerts);
        });
        Link linkOrders = linkTo(methodOn(CertificateController.class).getAll(pageable)).withRel("certificates");

        return CollectionModel.of(certificates, linkOrders);
    }

    @GetMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<TagDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable Long certificateId) {
        List<TagDto> tags = tagService.getAllByCertificateId(certificateId, pageable).getContent();

        tags.stream().forEach(t -> {
            Link selfLink = linkTo(methodOn(TagController.class).get(t.getId())).withSelfRel();
            t.add(selfLink);
        });

        Link link = linkTo(methodOn(CertificateController.class).getAll(pageable, certificateId)).withSelfRel();

        return CollectionModel.of(tags, link);
    }

    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        return certificateService.get(id).get();
    }

    @PostMapping
    public CertificateDto create(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.save(certificateDto).get();
    }

    @PutMapping("/{id}")
    public CertificateDto update(@Valid @RequestBody CertificateDto certificateDto, @PathVariable Long id) {
        certificateDto.setId(id);
        return certificateService.update(certificateDto).get();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletResponse response) {

        if (certificateService.delete(id)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }

    //https://www.baeldung.com/spring-rest-json-patch
    @PatchMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public CertificateDto update(@PathVariable Long id, @RequestBody JsonPatch patch) {
        CertificateDto certificateDto = certificateService.get(id).orElseThrow(() -> new NotFoundException(id));
        CertificateDto certificateDtoPatched = null;
        try {
            certificateDtoPatched = applyPatchToCertificate(patch, certificateDto);
        } catch (JsonPatchException e) {
            log.warn("Certificate patch exception:", e);
        } catch (JsonProcessingException e) {
            log.warn("Certificate patch processing exception:", e);
        }
        certificateDto = certificateService.update(certificateDtoPatched).orElseThrow(() -> new UpdateException(id));

        Link linkSelf = linkTo(methodOn(CertificateController.class).get(certificateDto.getId())).withSelfRel();
        Link linkToAll = linkTo(methodOn(CertificateController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("certificates");
        certificateDto.add(linkSelf, linkToAll);

        return certificateDto;
    }

    private CertificateDto applyPatchToCertificate(JsonPatch patch, CertificateDto certificateDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificateDto, JsonNode.class));
        return objectMapper.treeToValue(patched, CertificateDto.class);
    }
}
