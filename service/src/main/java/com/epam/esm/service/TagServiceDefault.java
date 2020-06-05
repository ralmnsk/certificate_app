package com.epam.esm.service;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.TagDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceDefault implements TagService<TagDto> {
    private static Logger logger = LoggerFactory.getLogger(CertificateServiceDefault.class);
    private TagRepository<Tag> tagRepository;
    private TagConverter tagConverter;

    public TagServiceDefault(TagRepository<Tag> tagRepository, TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public TagDto getByName(String name) {
        if (name != null){
            Tag tag = tagRepository.getByName(name);
            if (tag != null){
                return tagConverter.toDto(tag);
            }
        }
        return null;
    }

    @Override
    public List<TagDto> getAll() {
        List<Tag> tags = tagRepository.getAll();
        if (tags != null && !tags.isEmpty()){
            return tags.stream()
                    .map(tagConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public boolean save(TagDto tagDto) {
        if (tagDto != null){
            Tag tag = tagConverter.toEntity(tagDto);
            if (tag != null){
                return tagRepository.save(tag);
            }
        }
        return false;
    }

    @Override
    public TagDto get(Long id) {
        Tag tag = tagRepository.get(id);
        if (tag != null){
            return tagConverter.toDto(tag);
        }
        return null;
    }

    @Override
    public boolean update(TagDto tagDto) {
        if (tagDto != null){
            Tag tag = tagConverter.toEntity(tagDto);
            if (tag != null){
                return tagRepository.update(tag);
            }
        }
        return false;
    }

    @Override
    public boolean delete(Long id) {
        return tagRepository.delete(id);
    }
}
