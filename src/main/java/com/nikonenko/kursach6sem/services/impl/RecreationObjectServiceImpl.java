package com.nikonenko.kursach6sem.services.impl;

import com.nikonenko.kursach6sem.dto.responses.PageDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import com.nikonenko.kursach6sem.exceptions.RecreationObjectNotFoundException;
import com.nikonenko.kursach6sem.models.Photo;
import com.nikonenko.kursach6sem.models.RecreationObject;
import com.nikonenko.kursach6sem.models.Review;
import com.nikonenko.kursach6sem.repositories.PhotoRepository;
import com.nikonenko.kursach6sem.repositories.RecreationObjectRepository;
import com.nikonenko.kursach6sem.services.RecreationObjectService;
import com.nikonenko.kursach6sem.utils.ImgurUploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecreationObjectServiceImpl implements RecreationObjectService {
    private final RecreationObjectRepository recreationObjectRepository;
    private final PhotoRepository photoRepository;
    private final ModelMapper modelMapper;
    private final ImgurUploader imgurUploader;

    @Override
    public List<RecreationObjectDto> getAllRecreationObjects() {
        return recreationObjectRepository.findAll().stream()
                .map(object -> modelMapper.map(object, RecreationObjectDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PageDto<RecreationObjectDto> getPageableRecreationObjects(Pageable pageable) {
        Page<RecreationObject> recreationObjectPage = recreationObjectRepository.findAll(pageable);
        List<RecreationObjectDto> recreationObjectList = recreationObjectPage.getContent().stream()
                .map(recreationObject -> {
                    RecreationObjectDto response =
                            modelMapper.map(recreationObject, RecreationObjectDto.class);
                    response.setRating(getRating(recreationObject));
                    return response;
                })
                .toList();
        return PageDto.<RecreationObjectDto>builder()
                .objectList(recreationObjectList)
                .totalElements(recreationObjectPage.getTotalElements())
                .totalPages(recreationObjectPage.getTotalPages())
                .currentPage(recreationObjectPage.getNumber())
                .build();
    }

    @Override
    public PageDto<RecreationObjectDto> getSearchRecreationObjects(String search, String sortField, Pageable pageable) {
        Page<RecreationObject> recreationObjectPage = recreationObjectRepository.findBySearch(search, pageable);
        List<RecreationObjectDto> recreationObjectList = new ArrayList<>(recreationObjectPage.getContent().stream()
                .map(recreationObject -> {
                    RecreationObjectDto response =
                            modelMapper.map(recreationObject, RecreationObjectDto.class);
                    response.setRating(getRating(recreationObject));
                    return response;
                })
                .toList());

        if (sortField.equals("cheap")) {
            recreationObjectList.sort(Comparator.comparing(RecreationObjectDto::getPricePerDay));
        } else if (sortField.equals("expensive")) {
            recreationObjectList.sort(Comparator.comparing(RecreationObjectDto::getPricePerDay).reversed());
        }

        return PageDto.<RecreationObjectDto>builder()
                .objectList(recreationObjectList)
                .totalElements(recreationObjectPage.getTotalElements())
                .totalPages(recreationObjectPage.getTotalPages())
                .currentPage(recreationObjectPage.getNumber())
                .build();
    }

    @Override
    public RecreationObjectDto getRecreationObjectById(Long objectId) {
        RecreationObjectDto recreationObjectDto =
                modelMapper.map(getOrThrow(objectId), RecreationObjectDto.class);
        recreationObjectDto.setRating(getRating(getOrThrow(objectId)));
        return recreationObjectDto;
    }

    @Override
    public void saveRecreationObject(RecreationObjectDto recreationObjectDto, MultipartFile multipartFile,
                                     MultipartFile[] additionalImages) {
        saveOrEditRecreationObject(recreationObjectDto, multipartFile, additionalImages, false);
    }

    @Override
    public void editRecreationObject(Long id, RecreationObjectDto recreationObjectDto, MultipartFile multipartFile,
                                     MultipartFile[] additionalImages) {
        saveOrEditRecreationObject(recreationObjectDto, multipartFile, additionalImages, true);
    }

    private void saveOrEditRecreationObject(RecreationObjectDto recreationObjectDto, MultipartFile multipartFile,
                                      MultipartFile[] additionalImages, boolean isEdit) {
        try {
            String imgurUrl = imgurUploader.uploadToImgur(multipartFile, "ae559801c138d8c");
            recreationObjectDto.setMainPhoto(imgurUrl);
            RecreationObject recreationObject = recreationObjectRepository
                    .save(modelMapper.map(recreationObjectDto, RecreationObject.class));

            Set<Photo> photos = new HashSet<>();
            for (MultipartFile additionalImage : additionalImages) {
                if (!additionalImage.isEmpty()) {
                    photos.add(savePhoto(recreationObject, additionalImage, true));
                }
            }
            recreationObject.setPhotos(photos);

            if (isEdit) {
                recreationObject.setId(recreationObjectDto.getId());
            }
            recreationObjectRepository.save(recreationObject);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Photo savePhoto(RecreationObject recreationObject, MultipartFile multipartFile, boolean isAdditional) throws IOException {
        String imgurUrl = imgurUploader.uploadToImgur(multipartFile, "ae559801c138d8c");
        return photoRepository.save(Photo.builder()
                .recreationObject(recreationObject)
                .photo(imgurUrl)
                .build());
    }

    @Override
    public void deleteRecreationObject(Long id) {
        recreationObjectRepository.delete(getOrThrow(id));
    }

    private RecreationObject getOrThrow(Long objectId) {
        return recreationObjectRepository.findById(objectId).orElseThrow(RecreationObjectNotFoundException::new);
    }

    public static Double getRating(RecreationObject recreationObject){
        return recreationObject.getReviews().stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }
}
