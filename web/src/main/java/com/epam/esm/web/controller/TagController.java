package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * The type Tag controller.
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto, Integer> tagService;

    /**
     * Instantiates a new Tag controller.
     *
     * @param tagService the tag service
     */
    @Autowired
    public TagController(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    /**
     * Tags list.
     *
     * @return the list
     */
    @JsonView(Profile.PublicView.class)
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> tags() {
        return tagService.getAll();
    }

    /**
     * Get tag dto.
     *
     * @param id the id
     * @return the tag dto
     */
    @JsonView(Profile.PublicView.class)
    @GetMapping("/{id}")
    public TagDto get(@PathVariable Integer id) {
        return tagService.get(id).get();
    }

    /**
     * Create tag dto.
     *
     * @param tagDto the tag dto
     * @return the tag dto
     */
    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) {
        return tagService.save(tagDto).get();
    }


    /**
     * Delete.
     *
     * @param id       the id
     * @param response the response
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, HttpServletResponse response) {
        if (tagService.delete(id)) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
}
