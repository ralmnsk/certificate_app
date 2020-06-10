package com.epam.esm.web.controller;

import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.TagAlreadyExistsException;
import com.epam.esm.service.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto> tagService;

    @Autowired
    public TagController(TagService<TagDto> tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> tags() {
        return tagService.getAll();
    }

    @GetMapping("/{id}")
    public TagDto get(@PathVariable Long id) {
        return tagService.get(id).get();
//                .orElseThrow(() -> new TagNotFoundException(id));
    }

    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) throws TagAlreadyExistsException {
        return tagService.save(tagDto).get();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
