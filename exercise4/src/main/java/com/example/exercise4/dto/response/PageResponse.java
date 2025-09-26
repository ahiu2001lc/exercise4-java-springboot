package com.example.exercise4.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse <T>{
    private List<T> content;
    private int page;
    private int size;
    private Long totalElements;
    private int totalPages;
}
