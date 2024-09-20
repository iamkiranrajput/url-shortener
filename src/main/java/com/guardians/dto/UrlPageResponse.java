package com.guardians.dto;


import java.util.List;

public record UrlPageResponse (
    List<UrlMappingDto> urlDtos,
    Integer pageNumber,
    Integer pageSize,
    long totalElements,
    int totalPages,
    boolean isLast) {
}
