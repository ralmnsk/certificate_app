package com.epam.esm.model.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class TagFilter extends AbstractFilter {
    private String tagName;
    private Long certificateId;
}
