package com.epam.esm.service.tag;

import com.epam.esm.model.Tag;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.certificate.CertificateServiceImpl;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The type Tag service implementation.
 * Tag service uses {@link TagRepository}
 * for database crud operations with models(entities),
 * {@link TagConverter} for converting
 * DTO into model and vice versa.
 */
@Service
@Transactional
public class TagServiceImpl implements TagService<TagDto, Integer> {
    private static Logger logger = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private TagRepository<Tag, Integer> tagRepository;
    private TagConverter tagConverter;

    /**
     * Instantiates a new Tag service.
     * Spring injects parameters into constructor automatically.
     *
     * @param tagRepository the tag repository
     * @param tagConverter  the tag converter
     */
    public TagServiceImpl(TagRepository<Tag, Integer> tagRepository,
                          TagConverter tagConverter) {
        this.tagRepository = tagRepository;
        this.tagConverter = tagConverter;
    }

    @Override
    public Optional<TagDto> getByName(String name) {
        try {
            if (name != null) {
                Optional<Tag> optionalTag = tagRepository.getByName(name);
                if (optionalTag.isPresent()) {
                    return Optional.ofNullable(tagConverter.toDto(optionalTag.get()));
                }
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no such tag name = {}", name, e);
        }
        return Optional.empty();
    }

    @Override
    public List<TagDto> getAll() {
        List<Tag> tags = tagRepository.getAll();
        if (tags != null && !tags.isEmpty()) {
            return tags.stream().map(tagConverter::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public Optional<TagDto> save(TagDto tagDto) {

        Tag tag = tagConverter.toEntity(tagDto);
        try {
            Optional<Tag> optionalTag = tagRepository.save(tag);
            if (optionalTag.isPresent() && tag.getName() != null) {
                return Optional.ofNullable(tagConverter.toDto(optionalTag.get()));
            }
        } catch (DuplicateKeyException e) {
            logger.info("This tag already exists: {} {}", tag.getName(), e);
            throw new SaveException("This tag already exists: " + tag.getName() + ". Change name.");
        }
        throw new SaveException("Tag save exception happened");
    }

    @Override
    public Optional<TagDto> get(Integer id) {
        try {
            Optional<Tag> tagOptional = tagRepository.get(id);
            if (tagOptional.isPresent()) {
                TagDto tagDto = tagConverter.toDto(tagOptional.get());
                return Optional.ofNullable(tagDto);
            }
        } catch (EmptyResultDataAccessException e) {
            logger.info("There is no tag id = {}", id, e);
            throw new NotFoundException(id);
        }
        return Optional.empty();
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer tagId) {
        return tagRepository.delete(tagId);
    }
}
