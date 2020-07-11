package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.CertificateAssembler;
import com.epam.esm.web.assembler.TagAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

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
    private CertificatePageBuilder certificatePageBuilder;

    public CertificateController(CertificateService<CertificateDto, Long> certificateService, TagService<TagDto, Integer> tagService, ObjectMapper objectMapper, CertificateAssembler certificateAssembler, TagAssembler tagAssembler, CertificatePageBuilder certificatePageBuilder) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.certificateAssembler = certificateAssembler;
        this.tagAssembler = tagAssembler;
        this.certificatePageBuilder = certificatePageBuilder;
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK) //ALL
//    public CollectionModel<CertificateDto> getAll(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
//            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
//        Page<CertificateDto> page = certificateService.getAll(pageable);
//        List<CertificateDto> certificates = page.getContent();
//
//
//        return certificateAssembler.toCollectionModel(PARAM_NOT_USED, certificates, pageable);
//    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> getAll(
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters") String name,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "1")
            @Min(1)
            @Max(100) int size,

            @RequestParam(required = false) List<String> sort
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setTagName(tagName);
        filterDto.setCertificateName(name);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        return certificatePageBuilder.build(filterDto);
    }
//
//    @PostMapping("/{certificateId}/tags")
//    @ResponseStatus(HttpStatus.OK)
//    public TagDto createTagInCertificate(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
//            size = DEFAULT_PAGE_SIZE, sort = DEFAULT_SORT_ORDERS) Pageable pageable, @PathVariable Long certificateId, @Valid @RequestBody TagDto tagDto) {
//        tagDto = tagService.createTagInOrder(certificateId, tagDto).orElseThrow(() -> new SaveException("Create Tag in Certificate Exception"));
//
//        return tagAssembler.assemble((long) tagDto.getId(), tagDto);
//    }

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

        return certificateAssembler.assemble(id, certificateDto);
    }

    private CertificateDto applyPatchToCertificate(JsonPatch patch, CertificateDto certificateDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificateDto, JsonNode.class));
        return objectMapper.treeToValue(patched, CertificateDto.class);
    }
}
