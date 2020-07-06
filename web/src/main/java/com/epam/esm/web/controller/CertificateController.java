package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.CertificateAssembler;
import com.epam.esm.web.assembler.TagAssembler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private CertificateService<CertificateDto, Long> certificateService;
    private TagService<TagDto, Integer> tagService;
    private ObjectMapper objectMapper;
    private CertificateAssembler certificateAssembler;
    private TagAssembler tagAssembler;

    public CertificateController(CertificateService<CertificateDto, Long> certificateService, TagService<TagDto, Integer> tagService, ObjectMapper objectMapper, CertificateAssembler certificateAssembler, TagAssembler tagAssembler) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.certificateAssembler = certificateAssembler;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK) //ALL
    public CollectionModel<CertificateDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        List<CertificateDto> certificates = certificateService.getAll(pageable).getContent();

//        certificates.forEach(c -> {
//            Link linkSelf = linkTo(methodOn(CertificateController.class).get(c.getId())).withSelfRel();
//            Link linkCerts = linkTo(methodOn(CertificateController.class).getAll(pageable, c.getId())).withRel("tags");
//            c.add(linkSelf);
//            c.add(linkCerts);
//        });
//        Link linkOrders = linkTo(methodOn(CertificateController.class).getAll(pageable)).withRel("certificates");
//
//        return CollectionModel.of(certificates, linkOrders);
        return certificateAssembler.toCollectionModel(PARAM_NOT_USED, certificates, pageable);
    }

    @GetMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<TagDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable, @PathVariable Long certificateId) {
        List<TagDto> tags = tagService.getAllByCertificateId(certificateId, pageable).getContent();

//        tags.stream().forEach(t -> {
//            Link selfLink = linkTo(methodOn(TagController.class).get(t.getId())).withSelfRel();
//            t.add(selfLink);
//        });
//
//        Link link = linkTo(methodOn(CertificateController.class).getAll(pageable, certificateId)).withSelfRel();
//
//        return CollectionModel.of(tags, link);
        return tagAssembler.toCollectionModel(certificateId, tags, pageable);
    }

    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        CertificateDto certificateDto = certificateService.get(id).orElseThrow(() -> new NotFoundException(id));
        return certificateAssembler.assemble(id, certificateDto);
    }

    @PostMapping
    public CertificateDto create(@Valid @RequestBody CertificateDto certificateDto) {
        certificateDto = certificateService.save(certificateDto).orElseThrow(() -> new SaveException("Certificate save exception"));
        return certificateAssembler.assemble(certificateDto.getId(), certificateDto);
    }


    @PutMapping("/{id}")
    public CertificateDto update(@Valid @RequestBody CertificateDto certificateDto, @PathVariable Long id) {
        certificateDto.setId(id);
        certificateDto = certificateService.update(certificateDto).orElseThrow(() -> new NotFoundException(id));
        return certificateAssembler.assemble(id, certificateDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, HttpServletResponse response) {
        certificateService.delete(id);
        return ResponseEntity.noContent().build();
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
            throw new UpdateException(id);
        } catch (JsonProcessingException e) {
            log.warn("Certificate patch processing exception:", e);
            throw new UpdateException(id);
        }
        certificateDto = certificateService.update(certificateDtoPatched).orElseThrow(() -> new UpdateException(id));

//        Link linkSelf = linkTo(methodOn(CertificateController.class).get(certificateDto.getId())).withSelfRel();
//        Link linkToAll = linkTo(methodOn(CertificateController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("certificates");
//        certificateDto.add(linkSelf, linkToAll);
//
//        return certificateDto;
        return certificateAssembler.assemble(id, certificateDto);
    }

    private CertificateDto applyPatchToCertificate(JsonPatch patch, CertificateDto certificateDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificateDto, JsonNode.class));
        return objectMapper.treeToValue(patched, CertificateDto.class);
    }
}
