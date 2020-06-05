package com.epam.esm.web.controller;

import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.dto.TagDto;
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
@RequestMapping
public class TagController {
    private final TagService<TagDto> tagService;

    @Autowired
    public TagController(TagService<TagDto> tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagDto>> tags() {
        List<TagDto> list = tagService.getAll();
        if (list.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/tag/{id}")
    public ResponseEntity<TagDto> getTag(@PathVariable Long id) {
        Optional<TagDto> tagDto = Optional.ofNullable(tagService.get(id));
        return tagDto
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/tag")
    public ResponseEntity<TagDto>
    createTag(@Valid @RequestBody TagDto tagDto) throws URISyntaxException {
        if (tagService.save(tagDto)) {
            TagDto result = tagService.getByName(tagDto.getName());
            if (result != null) {
                return ResponseEntity.created(new URI("/tag" + result.getId())).body(result);
            }
        }
        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @PutMapping("/tag/{id}")
    public ResponseEntity<TagDto>
    updateTag(@Valid @RequestBody TagDto tagDto) {
        boolean result = tagService.update(tagDto);
        if (result) {
            Optional<TagDto> found = Optional.ofNullable(tagService.getByName(tagDto.getName()));
            found
                    .map(response -> ResponseEntity.ok().body(response))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/tag/{id}")
    public ResponseEntity<TagDto> deleteTag(@PathVariable Long id) {
        boolean delete = tagService.delete(id);
        if (delete) {
            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
