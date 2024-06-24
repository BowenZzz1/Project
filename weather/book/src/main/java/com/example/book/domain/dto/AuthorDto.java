package com.example.library.domain.dto;

import lombok.Data;
import java.util.Set;

@Data
public class AuthorDto {
    private Long id;
    private String name;
    private Set<Long> bookIds;
}
