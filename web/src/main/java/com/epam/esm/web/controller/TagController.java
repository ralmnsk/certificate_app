package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto, Integer> tagService;

    @Autowired
    public TagController(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> tags() {
        return tagService.getAll();
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
    public boolean delete(@PathVariable Integer id) {
        return tagService.delete(id);
    }
}
