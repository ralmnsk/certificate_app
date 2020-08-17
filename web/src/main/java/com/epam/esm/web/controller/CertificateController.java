package com.epam.esm.web.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.exception.ValidationException;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.SaveException;
import com.epam.esm.repository.exception.UpdateException;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.TagService;
import com.epam.esm.web.assembler.CertificateAssembler;
import com.epam.esm.web.page.CertificatePageBuilder;
import com.epam.esm.web.page.TagPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

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
    private TagPageBuilder tagPageBuilder;
    private WebSecurity webSecurity;

    public CertificateController(CertificateService certificateService, TagService tagService, ObjectMapper objectMapper, CertificateAssembler certificateAssembler, CertificatePageBuilder certificatePageBuilder, TagPageBuilder tagPageBuilder, WebSecurity webSecurity) {
        this.certificateService = certificateService;
        this.tagService = tagService;
        this.objectMapper = objectMapper;
        this.certificateAssembler = certificateAssembler;
        this.certificatePageBuilder = certificatePageBuilder;
        this.tagPageBuilder = tagPageBuilder;
        this.webSecurity = webSecurity;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<CertificateDto> getAll(
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "page must be 0-10000000")
            @Max(value = 10000000, message = "page must be 0-10000000") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,

            @RequestParam(required = false) @Size(min = 0, max = 3) List<@Pattern(regexp = "[a-zA-Z.+-]{0,20}") String> sort
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
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto get(@PathVariable Long id, Authentication authentication) {
        CertificateDto certificateDto = certificateService.get(id).orElseThrow(() -> new NotFoundException(id));
        return certificateAssembler.assemble(id, certificateDto, authentication);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CertificateDto create(@RequestBody CertificateDto certificateDto, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        certificateDto = certificateService.save(certificateDto).orElseThrow(() -> new SaveException("Certificate save exception"));

        return certificateAssembler.assemble(certificateDto.getId(), certificateDto, authentication);
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@RequestBody CertificateDto certificateDto, @PathVariable Long id,
                                 Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        certificateDto.setId(id);
        certificateDto = certificateService.update(certificateDto).orElseThrow(() -> new NotFoundException(id));

        return certificateAssembler.assemble(id, certificateDto, authentication);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Principal principal) {
        webSecurity.checkOperationAccess(principal);
        certificateService.delete(id);
    }


    @PatchMapping(path = "/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto update(@PathVariable Long id, @RequestBody JsonPatch patch, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        CertificateDto certificateDto = certificateService.get(id).orElseThrow(() -> new NotFoundException(id));
        CertificateDto certificateDtoPatched = null;
        try {
            certificateDtoPatched = applyPatchToCertificate(patch, certificateDto);
        } catch (JsonPatchException e) {
            log.warn("Certificate patch exception:{}", e.getMessage());
            throw new UpdateException("Certificate patch processing exception:" + e.getMessage());
        } catch (JsonProcessingException e) {
            log.warn("Certificate patch processing exception:{}", e.getMessage());
            throw new UpdateException("Certificate patch processing exception:" + e.getMessage());
        }
        certificateDto = patchToDto(certificateDto, certificateDtoPatched);
        certificateDto = certificateService.update(certificateDto).orElseThrow(() -> new UpdateException(id));

        return certificateAssembler.assemble(id, certificateDto, authentication);
    }


    @PutMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CertificateDto addTagToCertificate(@PathVariable Long certificateId,
                                              @RequestBody Set<Long> tagIds, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        tagService.addTagToCertificate(certificateId, tagIds);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setCertificateId(certificateId);
        CertificateDto certificateDto = certificateService.get(certificateId).orElseThrow(() -> new NotFoundException(this.getClass() + ":certificate not found, id:" + certificateId));
        return certificateAssembler.assemble(certificateId, certificateDto, authentication);
    }

    @DeleteMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public CertificateDto deleteTagFromCertificate(@PathVariable Long certificateId,
                                                   @RequestBody Set<Long> tagIds, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        tagService.removeTagFromCertificate(certificateId, tagIds);

        CertificateFilterDto filterDto = new CertificateFilterDto();
        filterDto.setCertificateId(certificateId);
        CertificateDto certificateDto = certificateService.get(certificateId).orElseThrow(() -> new NotFoundException(this.getClass() + ":certificate not found, id:" + certificateId));
        return certificateAssembler.assemble(certificateId, certificateDto, authentication);
    }

    @GetMapping("/{certificateId}/tags")
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<TagDto> getAllTagsByCertificateId(
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,

            @RequestParam(value = "certificateName", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters") String certificateName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(value = 0, message = "page must be 0-10000000")
            @Max(value = 10000000, message = "page must be 0-10000000") int page,

            @RequestParam(value = "size", defaultValue = "5")
            @Min(value = 1, message = "size must be 1-100")
            @Max(value = 100, message = "size must be 1-100") int size,

            @RequestParam(required = false) @Size(min = 0, max = 3) List<@Pattern(regexp = "[a-zA-Z.+-]{0,20}") String> sort,
            @PathVariable Long certificateId
    ) {
        TagFilterDto filterDto = new TagFilterDto();
        filterDto.setCertificateName(certificateName);
        filterDto.setTagName(tagName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        if (sort == null) {
            List<String> list = new ArrayList<>();
            list.add("tagName+");
            filterDto.setSortParams(list);
        } else {
            filterDto.setSortParams(sort);
        }
        filterDto.setCertificateId(certificateId);

        return tagPageBuilder.build(filterDto);
    }

    private CertificateDto applyPatchToCertificate(JsonPatch patch, CertificateDto certificateDto) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(certificateDto, JsonNode.class));
        return objectMapper.treeToValue(patched, CertificateDto.class);
    }

    private CertificateDto patchToDto(CertificateDto dto, CertificateDto patched) {
        Map<String, String> errors = new HashMap<>();

        if (patched.getName() != null) {
            boolean matches = patched.getName().matches("([А-Яа-яa-zA-Z0-9 .!&?#,;$]){2,256}");
            if (matches) {
                dto.setName(patched.getName());
            } else {
                errors.put("name:", "Name must be between 2 and 256 characters.");
            }
        }

        if (patched.getDescription() != null) {
            boolean matches = patched.getDescription().matches("[А-Яа-яa-zA-Z0-9 .!&?#,;$]{0,999}");
            if (matches) {
                dto.setDescription(patched.getDescription());
            } else {
                errors.put("description:", "Description must be between 2 and 999 characters.");
            }
        }

        if (patched.getPrice() != null) {
            try {
                if (patched.getPrice().compareTo(BigDecimal.valueOf(0.00)) < 0) {
                    throw new NumberFormatException();
                }
                if (patched.getPrice().compareTo(BigDecimal.valueOf(1000000000000.00)) > 0) {
                    throw new NumberFormatException();
                }
                dto.setPrice(patched.getPrice());
            } catch (NumberFormatException nfe) {
                errors.put("price:", "Price should be 0.00 - 1000000000000.00.");
            }
        }

        if (patched.getDuration() != null) {
            try {
                if (patched.getDuration() < 0 || patched.getDuration() > 100000) {
                    throw new NumberFormatException();
                }
                dto.setDuration(patched.getDuration());
            } catch (NumberFormatException nfe) {
                errors.put("duration:", "Range should be 0 - 100000.");
            }
        }

        if (!errors.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            errors.forEach((k, v) ->
                    builder.append("Field ").append(k).append(v).append("  ")
            );
            log.error(builder.toString());
            ValidationException validationException = new ValidationException(builder.toString());
            validationException.getFieldsException().putAll(errors);
            errors.clear();
            throw validationException;
        }

        return dto;
    }
}
