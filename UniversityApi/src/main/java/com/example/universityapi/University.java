package com.example.universityapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class University {

    private String name;
    private List<String> domains;

    @JsonProperty("web_pages")
    private List<String> webPages;
}
