package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.FilterDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService<CertificateDto, Long> certificateService;


    public CertificateController(CertificateService<CertificateDto, Long> certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CertificateDto> getAll(
            @Valid String tagName,
            @Valid String name,
            @Valid String page,
            @Valid String size,
            @Valid String sort
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setTagName(tagName);
        filterDto.setName(name);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSort(sort);
        return certificateService.getAll(filterDto);
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
    public boolean delete(@PathVariable Long id) {
        return certificateService.delete(id);
    }
}
