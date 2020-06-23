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

/**
 * The type Certificate controller.
 * The CertificateController takes care of mapping certificate request data
 * to the defined request handler method. Once response body is generated
 * from the handler method, it converts it to JSON response.
 */
@Validated
@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private final CertificateService<CertificateDto, Long> certificateService;
    private final PageBuilder pageBuilder;

    /**
     * Instantiates a new Certificate controller.
     * Spring injects parameters into constructor automatically.
     *
     * @param certificateService the certificate service
     * @param pageBuilder        the page builder
     */
    public CertificateController(CertificateService<CertificateDto, Long> certificateService,
                                 PageBuilder pageBuilder) {
        this.certificateService = certificateService;
        this.pageBuilder = pageBuilder;
    }

    /**
     * Gets all.
     *
     * @param tagName    the tag name for the filtering
     * @param name       the certificate name for the filtering
     * @param page       the page number for certificates
     * @param size       the count of certificates on the page
     * @param sortParams the sorting parameters (name, creation)
     * @return CustomPage with parameters and a certificate list
     */
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
                    List<String> sortParams
    ) {
        FilterDto filterDto = new FilterDto();
        filterDto.setTagName(tagName);
        filterDto.setName(name);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sortParams);
        return pageBuilder.build(filterDto);
    }

    /**
     * Get certificate dto by the certificate id.
     *
     * @param id the certificate id
     * @return the certificate dto from the database
     */
    @GetMapping("/{id}")
    public CertificateDto get(@PathVariable Long id) {
        return certificateService.get(id).get();
    }

    /**
     * Create and return certificate dto.
     *
     * @param certificateDto the certificate dto to save
     * @return the saved certificate dto
     */
    @PostMapping
    public CertificateDto
    create(@Valid @RequestBody CertificateDto certificateDto) {
        return certificateService.save(certificateDto).get();
    }

    /**
     * Update existing certificate dto by the certificate id.
     *
     * @param certificateDto the certificate dto to update
     * @param id             the certificate id
     * @return the updated certificate dto
     */
    @PutMapping("/{id}")
    public CertificateDto
    update(@Valid @RequestBody CertificateDto certificateDto, @PathVariable Long id) {
        certificateDto.setId(id);
        return certificateService.update(certificateDto).get();
    }


    /**
     * Delete existing certificate by the certificate id.
     *
     * @param id       the certificate id
     * @param response the response with status SC_OK(successful) or
     *                 SC_NO_CONTENT (content not found)
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpServletResponse response) {

        if (certificateService.delete(id)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
