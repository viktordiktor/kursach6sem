package com.nikonenko.kursach6sem.services;

import com.nikonenko.kursach6sem.dto.responses.PageDto;
import com.nikonenko.kursach6sem.dto.responses.RecreationObjectDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RecreationObjectService {
    List<RecreationObjectDto> getAllRecreationObjects();
    PageDto<RecreationObjectDto> getPageableRecreationObjects(Pageable pageable);
    PageDto<RecreationObjectDto> getSearchRecreationObjects(String search, String sortField, Pageable pageable);
    RecreationObjectDto getRecreationObjectById(Long objectId);
    void saveRecreationObject(RecreationObjectDto recreationObjectDto, MultipartFile multipartFile,
                              MultipartFile[] additionalImages);

    void deleteRecreationObject(Long id);

    void editRecreationObject(Long id, RecreationObjectDto recreationObjectDto, MultipartFile file,
                              MultipartFile[] additionalImages);
}
