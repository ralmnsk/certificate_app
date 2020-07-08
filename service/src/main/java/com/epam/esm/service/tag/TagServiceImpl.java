package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.jpa.CertificateRepository;
import com.epam.esm.repository.jpa.TagRepository;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
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
    private CertificateRepository certificateRepository;

    public TagServiceImpl(TagRepository tagRepository, ModelMapper mapper, CertificateRepository certificateRepository) {
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
    public Page<TagDto> getAll(Pageable pageable) {
        Page<Tag> page = tagRepository.findAll(pageable);
        List<TagDto> dtoList = page.getContent().stream().map(t -> mapper.map(t, TagDto.class)).collect(Collectors.toList());
        return new PageImpl<TagDto>(dtoList, pageable, dtoList.size());
    }

    @Override
    public Optional<TagDto> save(TagDto tagDto) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        Tag tag = null;
        if (tagDto == null) {
            return Optional.empty();
        }
        if (tagDto.getId() != null && tagDto.getId() > 0) {
            Optional<Tag> byId = tagRepository.findById(tagDto.getId());
            if (byId.isPresent()) {
                tag = byId.get();
                return Optional.ofNullable(mapper.map(tag, TagDto.class));
            }
        }
        if (tagDto.getName() != null) {
            Optional<TagDto> byName = getByName(tagDto.getName());
            if (byName.isPresent()) {
                return byName;
            }
            tag = mapper.map(tagDto, Tag.class);
            tag = tagRepository.saveAndFlush(tag);
            return Optional.ofNullable(mapper.map(tag, TagDto.class));

        }
        return tagDtoOptional;

    }

    @Override
    public Optional<TagDto> get(Integer id) {
        Optional<TagDto> tagDtoOptional = Optional.empty();
        Tag tag = tagRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
        TagDto tagDto = mapper.map(tag, TagDto.class);


        return Optional.ofNullable(tagDto);
    }

    @Override
    public Optional<TagDto> update(TagDto tagDto) {
        return Optional.empty();
    }

    @Override
    public boolean delete(Integer tagId) {
        tagRepository.getOne(tagId);
        tagRepository.removeFromRelationByTagId(tagId);
        tagRepository.deleteById(tagId);
        tagRepository.flush();
        return true;
    }

    @Override
    public Optional<TagDto> createTagInOrder(Long certificateId, TagDto tagDto) {
        tagDto = save(tagDto).orElseThrow(() -> new SaveException("Tag save exception"));
        Certificate certificate = certificateRepository.findById(certificateId).orElseThrow(() -> new NotFoundException(certificateId));
        Tag tag = mapper.map(tagDto, Tag.class);
        certificate.getTags().add(tag);
        certificateRepository.save(certificate);

        return Optional.of(tagDto);
    }

    //    @Override
//    public Page<TagDto> getAllByCertificateId(Long certificateId, Pageable pageable) {
//        Page<Tag> certificates = tagRepository.getAllByCertificateId(certificateId, pageable);
//        List<TagDto> dtoList = certificates.getContent()
//                .stream()
//                .map(t -> mapper.map(t, TagDto.class))
//                .collect(Collectors.toList());
//        return new PageImpl<TagDto>(dtoList, pageable, dtoList.size());
//    }
}
