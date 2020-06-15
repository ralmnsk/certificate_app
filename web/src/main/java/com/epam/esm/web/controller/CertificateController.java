package com.epam.esm.web.controller;

import com.epam.esm.model.Filter;
import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
            @Valid String tagName,            // filter by tag name
            @Valid String name,               //search by certificate name
            @Valid String page,
            @Valid String size,
            @Valid String sort
    ) {
        //http://localhost:8082/certificates?name=cert&tagName=ta&page=1&size=5&sort=(name,creation)

        Filter filter = new Filter();
        filter.setTagName(tagName);
        filter.setName(name);
        filter.setPage(page);
        filter.setSize(size);
        filter.setSort(sort);
        return certificateService.getAll(filter);
    }

    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        return certificateService.get(id).get();             //Empty optional will never happen because the service throws
    }                                                        // NotFound, Save or Update exception.

    @PostMapping
    public CertificateDto //It doesn't work without @RequestBody
    create(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.save(certificateDto).get();
    }

    @PutMapping("/{id}") //It doesn't work without @RequestBody
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
