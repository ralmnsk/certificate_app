package com.epam.esm.web.controller;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.SaveException;
import com.epam.esm.service.TagService;
import com.epam.esm.web.assembler.TagAssembler;
import com.epam.esm.web.page.TagPageBuilder;
import com.epam.esm.web.security.config.WebSecurity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.security.Principal;
import java.util.List;

@Validated
@RestController
@RequestMapping("/tags")
public class TagController {
    private final TagService tagService;
    private TagAssembler tagAssembler;
    private TagPageBuilder pageBuilder;
    private WebSecurity webSecurity;

    public TagController(TagService tagService,
                         TagAssembler tagAssembler,
                         TagPageBuilder pageBuilder, WebSecurity webSecurity) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
        this.pageBuilder = pageBuilder;
        this.webSecurity = webSecurity;
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
    public TagDto get(@PathVariable Integer id, Authentication authentication) {
        TagDto tagDto = tagService.get(id).orElseThrow(() -> new NotFoundException(id));
        int idInt = id;
        return tagAssembler.assemble((long) idInt, tagDto, authentication);
    }

    @PostMapping
    public TagDto
    create(@Valid @RequestBody TagDto tagDto, Authentication authentication) {
        String login = authentication.getName();
        webSecurity.checkOperationAccess(login);
        tagDto = tagService.save(tagDto).orElseThrow(() -> new SaveException("Tag save exception"));
        int idInt = tagDto.getId();
        return tagAssembler.assemble((long) idInt, tagDto, authentication);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id, Principal principal) {
        webSecurity.checkOperationAccess(principal);
        tagService.delete(id);
    }

    @GetMapping("/find")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> findFrequentTag() {
        return tagService.findTopTag();
    }


}