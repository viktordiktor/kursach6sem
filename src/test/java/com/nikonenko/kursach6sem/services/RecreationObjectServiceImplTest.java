package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.responses.PageDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.exceptions.RecreationObjectNotFoundException;
import com.nikonenko.kursach6sem.models.Photo;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.Review;
import com.nikonenko.kursach6sem.repositories.PhotoRepository;
import com.nikonenko.kursach6sem.repositories.RecreationObjectRepository;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import com.nikonenko.kursach6sem.services.impl.RecreationObjectServiceImpl;
import com.nikonenko.kursach6sem.utils.ImgurUploader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecreationObjectServiceImplTest {

    @Mock
    private RecreationObjectRepository recreationObjectRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ImgurUploader imgurUploader;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private RecreationObjectServiceImpl recreationObjectService;

    private RecreationObject recreationObject;

    private RecreationObjectDto recreationObjectDto;

    private MultipartFile multipartFile;

    private MultipartFile[] additionalImages;

    private Set<Photo> photos;

    private Page<RecreationObject> recreationObjectPage;

    @BeforeEach
    void setUp() {
        recreationObject = RecreationObject.builder()
                .id(1L)
                .name("Test Recreation Object")
                .description("Test Description")
                .mainPhoto("test.jpg")
                .pricePerDay(BigDecimal.ONE)
                .reviews(new HashSet<>())
                .build();

        recreationObjectDto = RecreationObjectDto.builder()
                .id(1L)
                .name("Test Recreation Object")
                .description("Test Description")
                .pricePerDay(BigDecimal.ONE)
                .mainPhoto("test.jpg")
                .photos(new HashSet<>())
                .build();

        multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "test".getBytes());

        additionalImages = new MultipartFile[]{multipartFile};

        photos = new HashSet<>();

        recreationObjectPage = new PageImpl<>(List.of(recreationObject));
    }

    @Test
    void getAllRecreationObjects() {
        List<RecreationObject> recreationObjects = List.of(recreationObject);
        doReturn(recreationObjectDto)
                .when(modelMapper)
                .map(any(RecreationObject.class), eq(RecreationObjectDto.class));
        when(recreationObjectRepository.findAll()).thenReturn(recreationObjects);

        List<RecreationObjectDto> recreationObjectDtos = recreationObjectService.getAllRecreationObjects();

        assertEquals(1, recreationObjectDtos.size());
        assertEquals(recreationObjectDto.getId(),
                recreationObjectDtos.get(0).getId());
        assertEquals(recreationObjectDto.getName(), recreationObjectDtos.get(0).getName());
        assertEquals(recreationObjectDto.getDescription(), recreationObjectDtos.get(0).getDescription());
        assertEquals(recreationObjectDto.getPricePerDay(), recreationObjectDtos.get(0).getPricePerDay());
        assertEquals(recreationObjectDto.getMainPhoto(), recreationObjectDtos.get(0).getMainPhoto());
        assertTrue(recreationObjectDtos.get(0).getPhotos().isEmpty());
    }

    @Test
    void getPageableRecreationObjects() {
        when(recreationObjectRepository.findAll(any(Pageable.class)))
                .thenReturn(recreationObjectPage);
        doReturn(recreationObjectDto).when(modelMapper).map(any(RecreationObject.class), eq(RecreationObjectDto.class));

        PageDto<RecreationObjectDto> pageDto = recreationObjectService.getPageableRecreationObjects(Pageable.unpaged());

        assertEquals(1, pageDto.getObjectList().size());
        assertEquals(recreationObjectDto.getId(), pageDto.getObjectList().get(0).getId());
        assertEquals(recreationObjectDto.getName(), pageDto.getObjectList().get(0).getName());
        assertEquals(recreationObjectDto.getDescription(), pageDto.getObjectList().get(0).getDescription());
        assertEquals(recreationObjectDto.getPricePerDay(), pageDto.getObjectList().get(0).getPricePerDay());
        assertEquals(recreationObjectDto.getMainPhoto(), pageDto.getObjectList().get(0).getMainPhoto());
        assertTrue(pageDto.getObjectList().get(0).getPhotos().isEmpty());
        assertEquals(1, pageDto.getTotalElements());
        assertEquals(1, pageDto.getTotalPages());
        assertEquals(0, pageDto.getCurrentPage());
    }

    @Test
    void getSearchRecreationObjects() {
        when(recreationObjectRepository.findBySearch(anyString(), any(Pageable.class)))
                .thenReturn(recreationObjectPage);
        doReturn(recreationObjectDto).when(modelMapper).map(any(RecreationObject.class), eq(RecreationObjectDto.class));

        PageDto<RecreationObjectDto> pageDto = recreationObjectService.getSearchRecreationObjects("test", "cheap", Pageable.unpaged());

        assertEquals(1, pageDto.getObjectList().size());
        assertEquals(recreationObjectDto.getId(), pageDto.getObjectList().get(0).getId());
        assertEquals(recreationObjectDto.getName(), pageDto.getObjectList().get(0).getName());
        assertEquals(recreationObjectDto.getDescription(), pageDto.getObjectList().get(0).getDescription());
        assertEquals(recreationObjectDto.getPricePerDay(), pageDto.getObjectList().get(0).getPricePerDay());
        assertEquals(recreationObjectDto.getMainPhoto(), pageDto.getObjectList().get(0).getMainPhoto());
        assertTrue(pageDto.getObjectList().get(0).getPhotos().isEmpty());
        assertEquals(1, pageDto.getTotalElements());
        assertEquals(1, pageDto.getTotalPages());
        assertEquals(0, pageDto.getCurrentPage());
    }

    @Test
    void getRecreationObjectById() {
        when(recreationObjectRepository.findById(anyLong()))
                .thenReturn(Optional.of(recreationObject));
        doReturn(recreationObjectDto).when(modelMapper).map(any(RecreationObject.class), eq(RecreationObjectDto.class));

        RecreationObjectDto recreationObjectDto = recreationObjectService.getRecreationObjectById(1L);

        assertEquals(recreationObjectDto.getId(), recreationObject.getId());
        assertEquals(recreationObjectDto.getName(), recreationObject.getName());
        assertEquals(recreationObjectDto.getDescription(), recreationObject.getDescription());
        assertEquals(recreationObjectDto.getPricePerDay(), recreationObject.getPricePerDay());
        assertEquals(recreationObjectDto.getMainPhoto(), recreationObject.getMainPhoto());
        assertTrue(recreationObjectDto.getPhotos().isEmpty());
    }

    @Test
    void saveRecreationObject() throws IOException {
        doReturn("test.jpg").when(imgurUploader).uploadToImgur(any(MultipartFile.class), anyString());
        when(modelMapper.map(any(RecreationObjectDto.class), eq(RecreationObject.class)))
                .thenReturn(recreationObject);
        when(recreationObjectRepository.save(any(RecreationObject.class)))
                .thenReturn(recreationObject);

        recreationObjectService.saveRecreationObject(recreationObjectDto, multipartFile, additionalImages);

        verify(imgurUploader, times(2)).uploadToImgur(multipartFile, "ae559801c138d8c");
        verify(modelMapper, times(1)).map(recreationObjectDto, RecreationObject.class);
        verify(recreationObjectRepository, times(2)).save(recreationObject);
        assertEquals(1, recreationObject.getPhotos().size());
        assertEquals("test.jpg", recreationObject.getMainPhoto());
    }

    @Test
    void editRecreationObject() throws IOException {
        doReturn("test.jpg").when(imgurUploader).uploadToImgur(any(MultipartFile.class), anyString());
        when(modelMapper.map(any(RecreationObjectDto.class), eq(RecreationObject.class)))
                .thenReturn(recreationObject);
        when(recreationObjectRepository.save(any(RecreationObject.class)))
                .thenReturn(recreationObject);

        recreationObjectService.editRecreationObject(1L, recreationObjectDto, multipartFile, additionalImages);

        verify(modelMapper, times(1)).map(recreationObjectDto, RecreationObject.class);
        verify(recreationObjectRepository, times(2)).save(recreationObject);
        assertEquals(1, recreationObject.getPhotos().size());
        assertEquals("test.jpg", recreationObject.getMainPhoto());
    }

    @Test
    void deleteRecreationObject() {
        doReturn(Optional.of(recreationObject))
                .when(recreationObjectRepository)
                        .findById(1L);
        recreationObjectService.deleteRecreationObject(1L);

        verify(recreationObjectRepository, times(1)).delete(recreationObject);
    }

    @Test
    void getRating() {
        Set<Review> reviews = Set.of(
                Review.builder()
                        .id(1L)
                        .rating(5)
                        .build(),
                Review.builder()
                        .id(2L)
                        .rating(3)
                        .build()
        );

        RecreationObject recreationObject = RecreationObject.builder()
                .id(1L)
                .name("Test Recreation Object")
                .description("Test Description")
                .pricePerDay(BigDecimal.ONE)
                .reviews(reviews)
                .build();

        double rating = RecreationObjectServiceImpl.getRating(recreationObject);

        assertEquals(4.0, rating);
    }
}