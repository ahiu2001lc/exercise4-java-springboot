package com.example.exercise4.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse <T>{
    List<T> content;
    int page;
    int size;
    Long totalElements;
    int totalPages;
}
