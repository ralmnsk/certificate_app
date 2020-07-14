package com.epam.esm.web.controller;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.dto.filter.TagFilterDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import com.epam.esm.web.page.TagPageBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Validated
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto, Integer, TagFilterDto> tagService;
    private TagAssembler tagAssembler;
    private TagPageBuilder pageBuilder;

    public TagController(TagService<TagDto, Integer, TagFilterDto> tagService, TagAssembler tagAssembler, TagPageBuilder pageBuilder) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CustomPageDto<TagDto> getAll(
            @RequestParam(value = "tagName", defaultValue = "")
            @Size(max = 16, message = "tagName should be 0-16 characters") String tagName,

            @RequestParam(value = "page", defaultValue = "0")
            @Min(0)
            @Max(10000000) int page,

            @RequestParam(value = "size", defaultValue = "1")
            @Min(1)
            @Max(100) int size,

            @RequestParam(required = false) List<String> sort
    ) {
        TagFilterDto filterDto = new TagFilterDto();
        filterDto.setTagName(tagName);
        filterDto.setPage(page);
        filterDto.setSize(size);
        filterDto.setSortParams(sort);
        return pageBuilder.build(filterDto);
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
        tagDto = tagService.save(tagDto).orElseThrow(() -> new SaveException("Tag save exception"));
        int idInt = tagDto.getId();
        return tagAssembler.assemble((long) idInt, tagDto);
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<?> delete(@PathVariable Integer id, HttpServletResponse response) {
        if (tagService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }


}