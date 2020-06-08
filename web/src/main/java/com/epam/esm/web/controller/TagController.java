package com.epam.esm.web.controller;

import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.web.exception.TagAlreadyExistsException;
import com.epam.esm.web.exception.TagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
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
        return tagService.get(id)
                .orElseThrow(() -> new TagNotFoundException(id));
    }

    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) throws TagAlreadyExistsException {
        Optional<TagDto> tagDtoOptional = tagService.save(tagDto);
        if (tagDtoOptional.isPresent()){
            return tagDtoOptional.get();
        }

        throw new TagAlreadyExistsException();
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        tagService.delete(id);
    }
}
