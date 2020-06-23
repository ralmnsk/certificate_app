package com.epam.esm.web.controller;

import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.tag.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * The type Tag controller.
 * The TagController takes care of mapping tag request data
 * to the defined request handler method. Once response body is generated
 * from the handler method, it converts it to JSON response.
 */
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService<TagDto, Integer> tagService;

    /**
     * Instantiates a new Tag controller.
     * Spring injects parameters into constructor automatically.
     *
     * @param tagService the tag service
     */

    public TagController(TagService<TagDto, Integer> tagService) {
        this.tagService = tagService;
    }

    /**
     * Tags list.
     *
     * @return the list
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TagDto> tags() {
        return tagService.getAll();
    }

    /**
     * Get tag dto.
     *
     * @param id the tag id
     * @return the tag dto from a database
     */
    @GetMapping("/{id}")
    public TagDto get(@PathVariable Integer id) {
        return tagService.get(id).get();
    }

    /**
     * Create and return tag dto .
     *
     * @param tagDto the tag dto to create
     * @return the created tag dto
     */
    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto) {
        return tagService.save(tagDto).get();
    }


    /**
     * Delete tag by the id.
     *
     * @param id       the tag id
     * @param response the response with status SC_OK(successful) or
     *                 SC_NO_CONTENT (content not found)
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
