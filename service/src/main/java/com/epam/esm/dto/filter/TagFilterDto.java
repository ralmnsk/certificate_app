package com.epam.esm.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TagFilterDto extends AbstractFilterDto {
    private String certificateName;
    private String tagName;
    private Long certificateId;
}
