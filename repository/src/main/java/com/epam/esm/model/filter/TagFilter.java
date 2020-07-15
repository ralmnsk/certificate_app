package com.epam.esm.model.filter;

import lombok.Data;

@Data
public class TagFilter extends AbstractFilter {
    private String tagName;
}
