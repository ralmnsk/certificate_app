package com.epam.esm.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;

@Data
@NoArgsConstructor
public class CustomPageDto<T> {
    private long page;
    private long size;
    private long totalPage;
    private long totalElements;
    private CollectionModel<T> elements;
}
