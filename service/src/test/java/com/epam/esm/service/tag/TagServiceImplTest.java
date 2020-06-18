package com.epam.esm.service.tag;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.certificate.CertificateRepository;
import com.epam.esm.repository.tag.TagRepository;
import com.epam.esm.service.converter.CertificateConverter;
import com.epam.esm.service.converter.TagConverter;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.exception.SaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

class TagServiceImplTest {
    @Mock
    private CertificateRepository<Certificate, Long> certificateRepository;
    @Mock
    private TagRepository<Tag, Integer> tagRepository;
    @Mock
    private CertificateConverter certificateConverter;
    @Mock
    private TagConverter tagConverter;

    @InjectMocks
    private TagServiceImpl service;

    private Tag tagOne;
    private Certificate one;
    private CertificateDto certificateDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tagOne = new Tag();
        tagOne.setName("tagOne");
        tagOne.setId(1);

        one = new Certificate();
        one.setName("name1");
        one.setDescription("description1");
        one.setPrice(new BigDecimal(119.194).setScale(2, RoundingMode.HALF_UP));
        one.setCreation(Instant.now());
        one.setModification(Instant.now().plus(Duration.ofDays(10)));
        one.setDuration(100);

        CertificateConverter cc = new CertificateConverter(new ModelMapper());

        cc.toDto(one);
    }

    @Test
    void getByName() {
        TagDto tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("tagOne");
        List<Long> list = new ArrayList<>();
        list.add(1L);
        Certificate certificate = one;

        Mockito.when(tagRepository.getByName(any())).thenReturn(Optional.ofNullable(tagOne));
        Mockito.when(tagConverter.toDto(any())).thenReturn(tagDto);
        Mockito.when(certificateRepository.getCertificateIdsByTagId(any())).thenReturn(list);
        Mockito.when(certificateRepository.get(any())).thenReturn(Optional.of(certificate));
        Mockito.when(certificateConverter.toDto(any())).thenReturn(certificateDto);

        service.getByName(tagOne.getName());
        Mockito.verify(tagRepository).getByName(any());
        Mockito.verify(certificateRepository).get(any());
        Mockito.verify(certificateConverter).toDto(any());
    }

    @Test
    void getAll() {
        List<Tag> list = new ArrayList<Tag>();
        list.add(new Tag());
        Mockito.when(tagRepository.getAll()).thenReturn(list);
        service.getAll();
        Mockito.verify(tagRepository).getAll();
        Mockito.verify(tagConverter).toDto(any());

    }

    @Test
    void save() {
        Mockito.when(tagConverter.toEntity(any())).thenReturn(tagOne);
        Mockito.when(tagRepository.save(any())).thenReturn(Optional.ofNullable(tagOne));
        TagDto tagDto = new TagDto();
        tagDto.setName("tagOne");
        tagDto.setId(1);
        service.save(tagDto);
        Mockito.verify(tagRepository).save(any());
    }

    @Test
    void saveThrowsException() {
        Mockito.when(tagConverter.toEntity(any())).thenReturn(tagOne);
        Mockito.when(tagRepository.save(any())).thenThrow(DuplicateKeyException.class);
        TagDto tagDto = new TagDto();
        tagDto.setName("tagOne");
        tagDto.setId(1);

        Exception exception = assertThrows(SaveException.class, () -> service.save(tagDto));
    }

    @Test
    void update() {
        service.update(new TagDto());
        Mockito.verify(tagRepository, Mockito.times(0)).update(any());
    }

    @Test
    void delete() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        Mockito.when(certificateRepository.getCertificateIdsByTagId(any())).thenReturn(list);
        service.delete(any());
        Mockito.verify(tagRepository).delete(any());
    }

    @Test
    void get() {
        TagDto tagDto = new TagDto();
        tagDto.setId(1);
        tagDto.setName("tagOne");
        Mockito.when(tagRepository.get(any())).thenReturn(Optional.of(tagOne));
        Mockito.when(tagConverter.toDto(any())).thenReturn(tagDto);
        service.get(any());
        Mockito.verify(tagConverter).toDto(any());
        Mockito.verify(tagRepository).get(any());
    }

    @Test
    void getThrowsException() {
        Mockito.when(tagRepository.get(any())).thenThrow(EmptyResultDataAccessException.class);

        Exception exception = assertThrows(NotFoundException.class, () -> service.get(any()));
    }

}