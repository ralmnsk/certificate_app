package com.epam.esm.service.page;

import com.epam.esm.service.certificate.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.CustomPage;
import com.epam.esm.service.dto.FilterDto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Page builder.
 */
@Component
public class PageBuilder {
    private CertificateService<CertificateDto, Long> service;

    /**
     * Instantiates a new Page builder.
     *
     * @param service the service
     */
    public PageBuilder(CertificateService<CertificateDto, Long> service) {
        this.service = service;
    }

    /**
     * Build custom page.
     *
     * @param filterDto the filter dto
     * @return the custom page
     */
    public CustomPage<CertificateDto, Integer> build(FilterDto filterDto) {

        List<CertificateDto> certificates = service.getAll(filterDto);
        CustomPage<CertificateDto, Integer> page = new CustomPage<>();
        if (certificates != null && !certificates.isEmpty()) {
            page.getList().addAll(certificates);
        }
        page.setNumber(filterDto.getPage());
        page.setSize(filterDto.getSize());
        filterDto.setCount(true);
        int count = service.getAllCount(filterDto).intValue();
        page.setTotalElements(count);
        int totalPages = count / page.getSize();
        if (count % page.getSize() != 0) {
            totalPages++;
        }
        if (totalPages == 0) {
            totalPages++;
        }
        page.setTotalPages(totalPages);

        return page;
    }
}
