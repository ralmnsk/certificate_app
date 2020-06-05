package com.epam.esm.web.controller;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping
public class CertificateController {
    private final CertificateService<CertificateDto> certificateService;

    @Autowired
    public CertificateController(CertificateService<CertificateDto> certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/certificates")
    public ResponseEntity<List<CertificateDto>> certificates() {
        List<CertificateDto> list = certificateService.getAll();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/certificate/{id}")
    public ResponseEntity<CertificateDto> getCertificate(@PathVariable Long id) {
        Optional<CertificateDto> certificateDto = Optional.ofNullable(certificateService.get(id));
        return certificateDto
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/certificate")
    public ResponseEntity<CertificateDto>
    createCertificate(@Valid @RequestBody CertificateDto certificateDto) throws URISyntaxException {
        if (certificateService.save(certificateDto)) {
            CertificateDto result = certificateService.getByName(certificateDto.getName());
            if (result != null) {
                return ResponseEntity.created(new URI("/certificate" + result.getId())).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/certificate/{id}")
    public ResponseEntity<CertificateDto>
    updateCertificate(@Valid @RequestBody CertificateDto certificateDto) {
        boolean result = certificateService.update(certificateDto);
        if (result) {
            Optional<CertificateDto> found = Optional.ofNullable(certificateService.getByName(certificateDto.getName()));
            found
                    .map(response -> ResponseEntity.ok().body(response))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/certificate/{id}")
    public ResponseEntity<CertificateDto> deleteCertificate(@PathVariable Long id) {
        boolean delete = certificateService.delete(id);
        if (delete) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
