package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPage;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.page.PageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService<CertificateDto, Long> certificateService;
    private final PageBuilder pageBuilder;

    public CertificateController(CertificateService<CertificateDto, Long> certificateService,
                                 PageBuilder pageBuilder) {
        this.certificateService = certificateService;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPage<CertificateDto, Integer> getAll(
            @RequestParam(value = "tagName", defaultValue = "") @Valid String tagName,
            @RequestParam(value = "name", defaultValue = "") @Valid String name,
            @RequestParam(value = "page", defaultValue = "1") @Valid int page,
            @RequestParam(value = "size", defaultValue = "10") @Valid int size,
            @RequestParam(required = false) @Valid List<String> sort
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setTagName(tagName);
        filterDto.setName(name);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSort(sort);
        return pageBuilder.build(filterDto);
    }

    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        return certificateService.get(id).get();
    }

    @PostMapping
    public CertificateDto
    create(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.save(certificateDto).get();
    }

    @PutMapping("/{id}")
    public CertificateDto
    update(@Valid @RequestBody CertificateDto certificateDto, @PathVariable Long id) {
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
}
