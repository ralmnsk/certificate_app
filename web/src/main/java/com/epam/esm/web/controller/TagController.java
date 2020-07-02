package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final int DEFAULT_PAGE_SIZE = 5;
    private final int DEFAULT_PAGE_NUMBER = 1;
    private final TagService<TagDto, Integer> tagService;

    public TagController(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TagDto> tags(@PageableDefault(page = DEFAULT_PAGE_NUMBER,
            size = DEFAULT_PAGE_SIZE) Pageable pageable) {
        return tagService.getAll(pageable);
    }

    @GetMapping("/{id}")
    public TagDto get(@PathVariable Integer id) {
        return tagService.get(id).get();
    }

    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) {
        return tagService.save(tagDto).get();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletResponse response) {
        if (tagService.delete(id)) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
