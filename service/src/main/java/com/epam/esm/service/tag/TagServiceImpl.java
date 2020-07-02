package com.epam.esm.service.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.dto.TagDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TagServiceImpl implements TagService<TagDto, Integer> {
    private TagRepository tagRepository;
    private ModelMapper mapper;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper mapper) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
    }


    @Override
    public Optional<TagDto> getByName(String name) {
        try {
            if (name != null) {
                Optional<Tag> optionalTag = Optional.ofNullable(tagRepository.getByName(name));
                if (optionalTag.isPresent()) {
                    return Optional.ofNullable(mapper.map(optionalTag.get(), TagDto.class));
                }
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("There is no such tag name = {}", name, e);
        }
        return Optional.empty();
    }

    @Override
    public Page<TagDto> getAll(Pageable pageable) {
        Page<Tag> users = tagRepository.findAll(pageable);
        List<TagDto> dtoList = users.getContent().stream().map(t -> mapper.map(t, TagDto.class)).collect(Collectors.toList());
        return new PageImpl<TagDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Optional<TagDto> save(TagDto tagDto) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        if (tagDto != null) {
            Tag tag = mapper.map(tagDto, Tag.class);
            tagRepository.saveAndFlush(tag);
            return get(tag.getId());
        }
        return tagDtoOptional;
    }

    @Override
    public Optional<TagDto> get(Integer id) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        Optional<Tag> tagOptional = tagRepository.findById(id);
        if (tagOptional.isPresent()) {
            TagDto tagDto = mapper.map(tagOptional.get(), TagDto.class);
            return Optional.ofNullable(tagDto);
        }

        return tagDtoOptional;
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer tagId) {
        tagRepository.deleteById(tagId);
        return true;
    }

    @Override
    public Page<TagDto> getAllByCertificateId(Long certificateId, Pageable pageable) {
        Page<Tag> certificates = tagRepository.getAllByCertificateId(certificateId, pageable);
        List<TagDto> dtoList = certificates.getContent()
                .stream()
                .map(t -> mapper.map(t, TagDto.class))
                .collect(Collectors.toList());
        return new PageImpl<TagDto>(dtoList, pageable, dtoList.size());
    }
}
