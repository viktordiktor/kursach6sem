package com.nikonenko.kursach6sem.dto.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageDto<T> {
    private List<T> objectList;
    private long totalElements;
    private int totalPages;
    private int currentPage;
}
