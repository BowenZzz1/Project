package com.example.library.domain.dto;

import lombok.Data;
import java.util.Set;

@Data
public class BookDto {
    private Long id;
    private String title;
    private Set<Long> authorIds;
}
