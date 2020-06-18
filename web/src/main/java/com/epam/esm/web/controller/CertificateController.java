package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPage;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.page.PageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
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
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters")
                    String tagName,

            @RequestParam(value = "name", defaultValue = "")
            @Size(max = 16, message = "name should be 0-16 characters")
                    String name,

            @RequestParam(value = "page", defaultValue = "1")
            @Min(1)
            @Max(10000000)
                    int page,

            @RequestParam(value = "size", defaultValue = "10")
            @Min(1)
            @Max(100)
                    int size,

            @RequestParam(required = false)
                    List<String> sort
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
