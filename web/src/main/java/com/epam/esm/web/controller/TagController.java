package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto, Integer> tagService;
    private TagAssembler tagAssembler;

    public TagController(TagService<TagDto, Integer> tagService, TagAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<TagDto> tags(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        List<TagDto> tags = tagService.getAll(pageable).getContent();
        return tagAssembler.toCollectionModel(PARAM_NOT_USED, tags, pageable);
    }

    @GetMapping("/{id}")
    public TagDto get(@PathVariable Integer id) {
        TagDto tagDto = tagService.get(id).orElseThrow(() -> new NotFoundException(id));
        int idInt = id;
        return tagAssembler.assemble((long) idInt, tagDto);
    }

    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) {
        tagDto = tagService.save(tagDto).orElseThrow(() -> new SaveException("Certificate save exception"));
        int idInt = tagDto.getId();
        return tagAssembler.assemble((long) idInt, tagDto);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletResponse response) {
        tagService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
