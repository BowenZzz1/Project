package com.example.universityapi.service;

import com.example.universityapi.model.University;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UniversityService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String BASE_URL = "http://universities.hipolabs.com/search";

    public List<University> searchUniversities(String query) {
        String url = BASE_URL + "?name=" + query;
        University[] universities = restTemplate.getForObject(url, University[].class);
        return filterUniversities(universities);
    }

    public List<University> searchUniversitiesByCountry(String country) {
        String url = BASE_URL + "?country=" + country.replace(" ", "+");
        University[] universities = restTemplate.getForObject(url, University[].class);
        return filterUniversities(universities);
    }

    private List<University> filterUniversities(University[] universities) {
        return Arrays.stream(universities)
                .map(university -> new University(university.getName(), university.getDomains(), university.getWebPages()))
                .collect(Collectors.toList());
    }
}
