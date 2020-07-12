//package com.epam.esm.service.certificate;
//
//import com.epam.esm.model.Certificate;
//import com.epam.esm.model.Tag;
//import com.epam.esm.service.exception.NotFoundException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.dao.EmptyResultDataAccessException;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.time.Duration;
//import java.time.Instant;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//
//class CertificateServiceImplTest {
//    @Mock
//    private CertificateRepository repository;
//    @Mock
//    private CertificateConverter certificateConverter;
//    @Mock
//    private TagRepository tagRepository;
//    @Mock
//    private TagConverter tagConverter;
//    @Mock
//    private ModelMapper modelMapper;
//
//    private Certificate one;
//    private Tag tagOne;
//    private Tag tagTwo;
//
//
//    @InjectMocks
//    private CertificateServiceImpl service;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.initMocks(this);
//
//        one = new Certificate();
//        one = new Certificate();
//        one.setName("name1");
//        one.setDescription("description1");
//        one.setPrice(new BigDecimal(119.194).setScale(2, RoundingMode.HALF_UP));
//        one.setCreation(Instant.now());
//        one.setModification(Instant.now().plus(Duration.ofDays(10)));
//        one.setDuration(100);
//        one.setId(1L);
//
//        tagOne = new Tag();
//        tagOne.setName("tagOne");
//        tagOne.setId(1);
//        tagTwo = new Tag();
//        tagTwo.setName("tagTwo");
//        tagTwo.setId(2);
//
//    }
//
//
////    @Test
////    void getAll() {
////        FilterDto filterDto = new FilterDto();
////        Filter filter = new Filter();
////        CertificateDto certificateDto = new CertificateDto();
////        List<Certificate> list = new ArrayList<>();
////        list.add(one);
////        Mockito.when(modelMapper.map(any(), any())).thenReturn(filter);
////        Mockito.when(repository.getAll(any())).thenReturn(list);
////        Mockito.when(certificateConverter.toDto(any())).thenReturn(certificateDto);
////
////        service.getAll(filterDto);
////        Mockito.verify(modelMapper).map(any(), any());
////        Mockito.verify(repository).getAll(any());
////    }
//
//    @Test
//    void save() {
//        Mockito.when(certificateConverter.toEntity(any())).thenReturn(one);
//        service.save(any());
//        Mockito.verify(repository).save(any());
//    }
//
////    @Test
////    void get() {
////        Certificate certificate = new Certificate();
////        CertificateDto certificateDto = new CertificateDto();
////        Mockito.when(repository.get(any())).thenReturn(Optional.of(certificate));
////        service.get(any());
////        Mockito.verify(repository).get(any());
////        Mockito.verify(certificateConverter).toDto(any());
////    }
//
//    @Test
//    void getThrowsException() {
//        Mockito.when(service.get(any())).thenThrow(EmptyResultDataAccessException.class);
//        Exception exception = assertThrows(NotFoundException.class,
//                () -> service.get(any()));
//    }
//
////    @Test
////    void update() {
////        CertificateDto certificateDto = new CertificateDto();
////        certificateDto.setId(1L);
////        TagDto tagDto = new TagDto();
////        tagDto.setName("name1");
////        tagDto.setId(1);
////        certificateDto.getTags().add(tagDto);
////        List<Integer> list = new ArrayList<>(Arrays.asList(1));
////        Mockito.when(certificateConverter.toEntity(any())).thenReturn(one);
////        Mockito.when(repository.update(any())).thenReturn(Optional.of(one));
////        Mockito.when(certificateConverter.toDto(any())).thenReturn(certificateDto);
////        Mockito.when(tagRepository.get(any())).thenReturn(Optional.of(tagOne));
////        Mockito.when(tagConverter.toDto(any())).thenReturn(tagDto);
////
////        service.update(certificateDto);
////        Mockito.verify(repository).update(any());
////        Mockito.verify(certificateConverter).toDto(any());
////
////    }
//
//
////    @Test
////    void updateThrowsException() {
////        CertificateDto certificateDto = new CertificateDto();
////        certificateDto.setId(1L);
////        Mockito.when(certificateConverter.toEntity(any())).thenReturn(one);
////        Mockito.when(repository.update(any())).thenThrow(EmptyResultDataAccessException.class);
////        Exception exception = assertThrows(UpdateException.class,
////                () -> service.update(certificateDto));
////    }
//
//    @Test
//    void delete() {
//        service.delete(any());
//        Mockito.verify(repository).delete(any());
//    }
//
////    @Test
////    void addTagsToCertificate() {
////        Certificate cert = new Certificate();
////        cert.setId(1L);
////        cert.setName("name");
////        Optional<Certificate> cOptional = Optional.of(cert);
////        Mockito.when(repository.get(any())).thenReturn(cOptional);
////
////        CertificateDto cDto = new CertificateDto();
////        cDto.setId(1L);
////        cDto.setName("name");
////
////        Mockito.when(certificateConverter.toDto(any())).thenReturn(cDto);
////
////        List<Tag> tags = new ArrayList<>();
////        Tag tag = new Tag();
////        tag.setId(1);
////        tag.setName("tag");
////        tags.add(tag);
////        Mockito.when(tagRepository.getTagsByCertificateId(any())).thenReturn(tags);
////
////        TagDto tagDto = new TagDto();
////        tagDto.setId(1);
////        tagDto.setName("tag");
////        Mockito.when(tagConverter.toDto(any())).thenReturn(tagDto);
////        cDto.getTags().add(tagDto);
////
////        service.get(any());
////
////        Mockito.verify(tagConverter).toDto(any());
////    }
//
////    @Test
////    void saveTagsNoTagInDb() {
////        one.getTags().add(tagTwo);
////
////        CertificateDto certificateDto = new CertificateDto();
////        certificateDto.setId(1L);
////
////        Mockito.when(certificateConverter.toEntity(any())).thenReturn(one);
////        Mockito.when(repository.save(any())).thenReturn(Optional.ofNullable(one));
////        Mockito.when(certificateConverter.toDto(any())).thenReturn(certificateDto);
////        Mockito.when(tagRepository.getByName(any())).thenReturn(Optional.empty());
////        Mockito.when(tagRepository.save(any())).thenReturn(Optional.ofNullable(tagOne));
////
////        Exception exception = assertThrows(NewTagHasIdInCertificateException.class, () -> service.save(certificateDto));
////        Mockito.verify(tagRepository).getByName(any());
////    }
////
////
////    @Test
////    void getTagsByCertificateId() {
////        List<Tag> list = new ArrayList<>();
////        Tag tag = new Tag();
////        list.add(tag);
////        Mockito.when(tagRepository.getTagsByCertificateId(any())).thenReturn(list);
////        Mockito.when(tagRepository.get(any())).thenReturn(Optional.of(tag));
////
////        service.getTagsByCertificateId(1L);
////
////        Mockito.verify(tagRepository).getTagsByCertificateId(any());
////        Mockito.verify(tagConverter).toDto(any());
////    }
////
////    @Test
////    void getAllCount() {
////        FilterDto filterDto = new FilterDto();
////        Filter filter = new Filter();
////        Mockito.when(modelMapper.map(any(), any())).thenReturn(filter);
////        service.getAllCount(filterDto);
////        Mockito.verify(repository).getAllCount(any());
////    }
//}