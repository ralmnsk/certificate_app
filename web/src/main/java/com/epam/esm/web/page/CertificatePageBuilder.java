package com.epam.esm.web.page;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.filter.CertificateFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.web.assembler.CertificateAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;

@Component
public class CertificatePageBuilder extends AbstractPageBuilder<CertificateDto, CertificateService, CertificateAssembler, CertificateFilterDto> {
    private final String EMPTY = "";

    public CertificatePageBuilder(CertificateService service, CertificateAssembler certificateAssembler) {
        super(new HashSet<>(Arrays.asList("tag.name", "certificate.name", "price")), service, certificateAssembler);
    }


    public CustomPageDto<CertificateDto> build(CertificateFilterDto filterDto) {
        CustomPageDto<CertificateDto> page = new CustomPageDto<>();
        filterDto = validateFilter(filterDto);

        filterDto = setSort(filterDto);

        ListWrapperDto<CertificateDto, CertificateFilterDto> listWrapperDto = getService().getAll(filterDto);
        CollectionModel<CertificateDto> collectionModel = getCollectionModel(filterDto);

        filterDto = listWrapperDto.getFilterDto();
        page.setSize(filterDto.getSize());
        page.setTotalElements(filterDto.getTotalElements());
        page.setPage(filterDto.getPage());
        page.setElements(collectionModel);
        page.setTotalPage(filterDto.getTotalPages());

        return page;
    }

    private CertificateFilterDto validateFilter(CertificateFilterDto filterDto){
        if (filterDto.getTagName() == null) {
            filterDto.setTagName(EMPTY);
        }

        if (filterDto.getCertificateName() == null) {
            filterDto.setCertificateName(EMPTY);
        }
        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }
        if (filterDto.getCreation() == null) {
            filterDto.setCreation("1970-01-01 00:00:00");
        }
        if (filterDto.getModification() == null) {
            filterDto.setModification("1970-01-01 00:00:00");
        }
        if (filterDto.getDescription() == null) {
            filterDto.setDescription(EMPTY);
        }
        if (filterDto.getPrice() == null) {
            filterDto.setPrice(new BigDecimal(0.00));
        }
        if (filterDto.getDuration() == null) {
            filterDto.setDuration(0);
        }
        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }
        if (filterDto.getUserName() == null) {
            filterDto.setUserName(EMPTY);
        }
        filterDto = validateAbstractFilter(filterDto);
        return filterDto;
    }
}