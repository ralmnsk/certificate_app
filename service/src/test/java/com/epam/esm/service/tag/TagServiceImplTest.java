package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.filter.TagFilter;
import com.epam.esm.model.wrapper.TagListWrapper;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.filter.TagFilterDto;
import com.epam.esm.service.impl.TagServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ModelMapper mapper;
    @Mock
    private CertificateRepository certificateRepository;
    @InjectMocks
    private TagServiceImpl tagService;

    private Order order;
    private Certificate certificate;
    private CertificateDto certificateDto;
    private Tag tag;
    private TagDto tagDto;
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        order = new Order();
        order.setDeleted(false);
        order.setTotalCost(new BigDecimal(0));
        order.setDescription("description");
        order.setCreated(Timestamp.from(Instant.now()));
        order.setId(1L);

        certificate = new Certificate();
        certificate.setDeleted(false);
        certificate.setDuration(10);
        certificate.setDescription("certificate description");
        certificate.setPrice(new BigDecimal(50));
        certificate.setName("name");
        certificate.setId(2L);
        certificate.setCreation(order.getCreated());
        order.getCertificates().add(certificate);
        modelMapper = new ModelMapper();
        tag = new Tag();
        tag.setName("testTag");
        tag.setId(1);
        certificate.getTags().add(tag);

        certificateDto = modelMapper.map(certificate, CertificateDto.class);
        tagDto = modelMapper.map(tag, TagDto.class);
        when(tagRepository.save(any())).thenReturn(Optional.ofNullable(tag));
        when(tagRepository.get(any())).thenReturn(Optional.ofNullable(tag));
        when(tagRepository.update(any())).thenReturn(Optional.ofNullable(tag));

        when(mapper.map(tagDto, Tag.class)).thenReturn(tag);
        when(mapper.map(tag, TagDto.class)).thenReturn(tagDto);
        when(tagRepository.getByName(tagDto.getName())).thenReturn(Optional.ofNullable(tag));
        when(tagRepository.save(tag)).thenReturn(Optional.ofNullable(tag));

    }

    @Test
    void getByName() {
        when(tagRepository.getByName("testTag")).thenReturn(Optional.ofNullable(tag));
        tagService.getByName("name");
        Mockito.verify(tagRepository).getByName("name");
    }

    @Test
    void getAll() {
        TagFilter filter = new TagFilter();
        TagFilterDto filterDto = new TagFilterDto();
        when(mapper.map(filter, TagFilterDto.class)).thenReturn(filterDto);
        when(mapper.map(filterDto, TagFilter.class)).thenReturn(filter);
        TagListWrapper wrapper = new TagListWrapper();
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        wrapper.setFilter(filter);
        wrapper.setList(tags);

        when(tagRepository.getAll(filter)).thenReturn(wrapper);
        tagService.getAll(filterDto);
        verify(tagRepository).getAll(filter);
    }

    @Test
    void save() {
        tagService.save(tagDto);
        verify(tagRepository).getByName(tag.getName());
    }

    @Test
    void get() {
        tagService.get(1);
        verify(tagRepository).get(1);
    }

//    @Test
//    void delete() {
//        tagService.delete(1);
//        verify(tagRepository).delete(1);
//    }

    @Test
    void addTagToCertificate() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        when(certificateRepository.get(1L)).thenReturn(Optional.ofNullable(certificate));
        when(certificateRepository.update(certificate)).thenReturn(Optional.ofNullable(certificate));
        tagService.addTagToCertificate(1L, set);
        verify(certificateRepository).update(certificate);
    }

    @Test
    void deleteTagFromCertificate() {
        Set<Long> set = new HashSet<>();
        set.add(1L);
        when(certificateRepository.get(1L)).thenReturn(Optional.ofNullable(certificate));
        when(certificateRepository.update(certificate)).thenReturn(Optional.ofNullable(certificate));
        tagService.addTagToCertificate(1L, set);
        verify(certificateRepository).update(certificate);
    }

    @Test
    void findFrequentTag() {
        tagService.findTopTag();
        verify(tagRepository).findTopTag();
    }
}