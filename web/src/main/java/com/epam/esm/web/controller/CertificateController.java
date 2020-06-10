package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService<CertificateDto> certificateService;


    public CertificateController(CertificateService<CertificateDto> certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CertificateDto> getAll(
            String tagName,            // filter by tag name
            String name,               //search by certificate name
            String sortByName,         //sort by certificate name
            String sortByDate          //sort by certificate date
    ) {
        return certificateService.getAll(tagName, name, sortByName, sortByDate);
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
    public void delete(@PathVariable Long id) {
        certificateService.delete(id);
    }
}
