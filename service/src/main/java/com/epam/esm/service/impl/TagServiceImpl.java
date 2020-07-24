package com.epam.esm.service.impl;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.dto.wrapper.TagListWrapperDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.NotFoundException;
import com.epam.esm.repository.exception.UpdateException;
import com.epam.esm.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class TagServiceImpl implements TagService {

    private TagRepository tagRepository;
    private ModelMapper mapper;
    private CertificateRepository certificateRepository;


    public TagServiceImpl(TagRepository tagRepository, ModelMapper mapper,
                          CertificateRepository certificateRepository) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.certificateRepository = certificateRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<TagDto> getByName(String name) {
        try {
            if (name != null) {
                Optional<Tag> optionalTag = tagRepository.getByName(name);
                if (optionalTag.isPresent()) {
                    return Optional.ofNullable(mapper.map(optionalTag.get(), TagDto.class));
                }
            }
        } catch (EmptyResultDataAccessException e) {
            log.info("There is no such tag name = {}", name, e);
        }
        return Optional.empty();
    }

    @Transactional(readOnly = true)
    @Override
    public TagListWrapperDto getAll(TagFilterDto tagFilterDto) {
        TagFilter tagFilter = mapper.map(tagFilterDto, TagFilter.class);
        TagListWrapper wrapper = tagRepository.getAll(tagFilter);
        List<TagDto> tagDtoList = wrapper.getList().stream().map(t -> mapper.map(t, TagDto.class)).collect(toList());

        TagListWrapperDto wrapperDto = new TagListWrapperDto();
        wrapperDto.setList(tagDtoList);
        TagFilter filter = wrapper.getFilter();
        wrapperDto.setFilterDto(mapper.map(filter, TagFilterDto.class));

        return wrapperDto;
    }


    @Transactional
    @Override
    public Optional<TagDto> save(TagDto tagDto) {
        Tag tag = mapper.map(tagDto, Tag.class);
        try {
            Optional<Tag> byName = tagRepository.getByName(tagDto.getName());
            if (byName.isPresent()) {
                tagDto = mapper.map(byName.get(), TagDto.class);
                return Optional.ofNullable(tagDto);
            }
        } catch (IncorrectResultSizeDataAccessException e) {
            log.warn("Tag not found");
        }
        tag.setId(null);
        Optional<Tag> save = tagRepository.save(tag);
        if (save.isPresent()) {
            tagDto = mapper.map(save.get(), TagDto.class);
            return Optional.ofNullable(tagDto);
        }

        return Optional.empty();

    }


    @Transactional(readOnly = true)
    @Override
    public Optional<TagDto> get(Integer id) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        Tag tag = tagRepository.get(id).orElseThrow(() -> new NotFoundException("Tag not found exception"));
        TagDto tagDto = mapper.map(tag, TagDto.class);


        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean delete(Integer tagId) {
        Tag tag = tagRepository.get(tagId).orElseThrow(() -> new NotFoundException("Certificate delete: not found exception, id:" + tagId));
        tagRepository.delete(tagId);
        return true;
    }

    @Transactional
    @Override
    public void addTagToCertificate(Long certificateId, Set<Long> list) {
        Certificate certificate = certificateRepository.get(certificateId).orElseThrow(() -> new NotFoundException("Add Tag to Certificate: Certificate not found: id:" + certificateId));
        list
                .stream()
                .map(idDto -> tagRepository.get(idDto.intValue()).orElseThrow(() -> new NotFoundException("Add Tag to Certificate: Tag not found: id:" + idDto)))
                .forEach(tag -> certificate.getTags().add(tag));
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Add Tag to Certificate: Tag update exception"));
    }

    @Transactional
    @Override
    public void removeTagFromCertificate(Long certificateId, Set<Long> list) {
        Certificate certificate = certificateRepository.get(certificateId).orElseThrow(() -> new NotFoundException("Delete Tag from Certificate: Tag not found: id:" + certificateId));
        list
                .stream()
                .map(idDto -> tagRepository.get((int) (long) idDto).orElseThrow(() -> new NotFoundException("Delete Tag to Certificate: Tag not found: id:" + idDto)))
                .forEach(tag -> certificate.getTags().remove(tag));
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Delete Tag to Certificate: Tag update exception"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findTopTag() {
        List<String> list = tagRepository.findTopTag();
        return list;
    }
}
