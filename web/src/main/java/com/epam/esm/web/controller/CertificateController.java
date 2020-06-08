package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.web.exception.CertificateAlreadyExistsException;
import com.epam.esm.web.exception.CertificateNotFoundException;
import com.epam.esm.web.exception.CertificateUpdateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService<CertificateDto> certificateService;

    @Autowired
    public CertificateController(CertificateService<CertificateDto> certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CertificateDto> getAll() {
        return certificateService.getAll();
    }

    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        return certificateService.get(id)
                .orElseThrow(() -> new CertificateNotFoundException(id));
    }

    @PostMapping
    public CertificateDto
    create(@Valid @RequestBody CertificateDto certificateDto)
            throws CertificateAlreadyExistsException {
        Optional<CertificateDto> certificateDtoOptional = certificateService.save(certificateDto);
        if (certificateDtoOptional.isPresent()){
            return certificateDtoOptional.get();
        }
        throw new CertificateAlreadyExistsException();
    }

    @PutMapping("/{id}")
    public CertificateDto
    update(@Valid @RequestBody CertificateDto certificateDto, @PathVariable Long id) throws CertificateUpdateException{
        certificateDto.setId(id);
        Optional<CertificateDto> certificateDtoOptional = certificateService.update(certificateDto);
        if(certificateDtoOptional.isPresent()){
            return certificateDtoOptional.get();
        }
        throw new CertificateUpdateException(certificateDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        certificateService.delete(id);
    }
}
