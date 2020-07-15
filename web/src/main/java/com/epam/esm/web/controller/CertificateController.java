package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.filter.CertificateFilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.exception.UpdateException;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.CertificateAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Slf4j
@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private CertificateService certificateService;
    private TagService tagService;
    private ObjectMapper objectMapper;
    private CertificateAssembler certificateAssembler;
    private CertificatePageBuilder certificatePageBuilder;

    public CertificateController(CertificateService certificateService,
                                 TagService tagService,
                                 ObjectMapper objectMapper, CertificateAssembler certificateAssembler,
                                 CertificatePageBuilder certificatePageBuilder) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.certificateAssembler = certificateAssembler;
        this.certificatePageBuilder = certificatePageBuilder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> getAll(
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(1)
            @Max(100) int size,

            @RequestParam(required = false) List<String> sort
    ) {
        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setTagName(tagName);
        filterDto.setCertificateName(certificateName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        return certificatePageBuilder.build(filterDto);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        certificateService.delete(id);
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

    @PutMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto addTagToCertificate(@PathVariable Long certificateId, @Valid @RequestBody Set<Long> set) {
        tagService.addTagToCertificate(certificateId, set);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setCertificateId(certificateId);
        CertificateDto certificateDto = certificateService.get(certificateId).orElseThrow(() -> new NotFoundException(this.getClass() + ":certificate not found, id:" + certificateId));
        return certificateAssembler.assemble(certificateId, certificateDto);
    }

    @DeleteMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto deleteTagFromCertificate(@PathVariable Long certificateId, @Valid @RequestBody Set<Long> set) {
        tagService.deleteTagFromCertificate(certificateId, set);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setCertificateId(certificateId);
        CertificateDto certificateDto = certificateService.get(certificateId).orElseThrow(() -> new NotFoundException(this.getClass() + ":certificate not found, id:" + certificateId));
        return certificateAssembler.assemble(certificateId, certificateDto);
    }

    private CertificateDto applyPatchToCertificate(JsonPatch patch, CertificateDto certificateDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificateDto, JsonNode.class));
        return objectMapper.treeToValue(patched, CertificateDto.class);
    }
}
