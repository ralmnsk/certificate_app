package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.crud.CertificateCrudRepository;
import com.epam.esm.repository.crud.TagCrudRepository;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.IdDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.UpdateException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Getter
@Service
@Transactional
public class TagServiceImpl implements TagService<TagDto, Integer> {
    private FilterDto filterDto;

    private TagCrudRepository tagRepository;
    private ModelMapper mapper;
    private CertificateCrudRepository certificateRepository;


    public TagServiceImpl(TagCrudRepository tagRepository, ModelMapper mapper, CertificateCrudRepository certificateRepository) {
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.certificateRepository = certificateRepository;
    }

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

    @Override
    public List<TagDto> getAll(FilterDto filterDto) {
        Filter filter = mapper.map(filterDto, Filter.class);
        List<Tag> all = tagRepository.getAll(filter);
        List<TagDto> collect = all.stream().map(t -> mapper.map(t, TagDto.class)).collect(toList());
        if (tagRepository.getFilter() != null) {
            filterDto = mapper.map(tagRepository.getFilter(), FilterDto.class);
            this.filterDto = filterDto;
        }

        return collect;
    }


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

    @Override
    public Optional<TagDto> get(Integer id) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        Tag tag = tagRepository.get(id).orElseThrow(() -> new NotFoundException(id));
        TagDto tagDto = mapper.map(tag, TagDto.class);


        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer tagId) {
        Tag tag = tagRepository.get(tagId).orElseThrow(() -> new NotFoundException("Certificate delete: not found exception, id:" + tagId));
        tag.setDeleted(true);
        tagRepository.update(tag).orElseThrow(() -> new UpdateException("Certificate update in delete operation exception"));
        return true;
    }

    @Override
    public void addTagToCertificate(Long certificateId, List<IdDto> list) {
        Certificate certificate = certificateRepository.get(certificateId).orElseThrow(() -> new NotFoundException("Add Tag to Certificate: Certificate not found: id:" + certificateId));
        list
                .stream()
                .map(idDto -> tagRepository.get((int) (long) idDto.getId()).orElseThrow(() -> new NotFoundException("Add Tag to Certificate: Tag not found: id:" + idDto.getId())))
                .forEach(tag -> certificate.getTags().add(tag));
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Add Tag to Certificate: Tag update exception"));
    }

    @Override
    public void deleteTagFromCertificate(Long certificateId, List<IdDto> list) {
        Certificate certificate = certificateRepository.get(certificateId).orElseThrow(() -> new NotFoundException("Delete Tag from Certificate: Tag not found: id:" + certificateId));
        list
                .stream()
                .map(idDto -> tagRepository.get((int) (long) idDto.getId()).orElseThrow(() -> new NotFoundException("Delete Tag to Certificate: Tag not found: id:" + idDto.getId())))
                .forEach(tag -> certificate.getTags().remove(tag));
        certificateRepository.update(certificate).orElseThrow(() -> new UpdateException("Delete Tag to Certificate: Tag update exception"));
    }

}
