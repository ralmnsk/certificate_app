package com.epam.esm.web.page;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class TagPageBuilder extends AbstractPageBuilder<TagDto, TagService<TagDto, Integer>, TagAssembler> {
    private final String EMPTY = "";

    public TagPageBuilder(TagService<TagDto, Integer> service, TagAssembler certificateAssembler) {
        super(new HashSet<>(Arrays.asList("tagName")), service, certificateAssembler);
    }

}