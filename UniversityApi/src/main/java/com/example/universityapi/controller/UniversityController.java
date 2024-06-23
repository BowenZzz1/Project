package com.example.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/universities")
public class UniversityController {

    private final RestTemplate restTemplate;

    public UniversityController() {
        this.restTemplate = new RestTemplate();
    }

    @GetMapping
    public List<University> getAllUniversities() {
        String url = "http://universities.hipolabs.com/search";
        University[] universities = restTemplate.getForObject(url, University[].class);
        return filterUniversityInfo(Arrays.asList(universities));
    }

    @PostMapping
    public List<University> getUniversitiesByCountries(@RequestBody List<String> countries) throws ExecutionException, InterruptedException {
        List<CompletableFuture<List<University>>> futures = new ArrayList<>();
        for (String country : countries) {
            futures.add(getUniversitiesByCountry(country));
        }

        List<University> result = new ArrayList<>();
        for (CompletableFuture<List<University>> future : futures) {
            result.addAll(future.get());
        }

        return filterUniversityInfo(result);
    }

    private CompletableFuture<List<University>> getUniversitiesByCountry(String country) {
        return CompletableFuture.supplyAsync(() -> {
            String url = "http://universities.hipolabs.com/search?country=" + country;
            University[] universities = restTemplate.getForObject(url, University[].class);
            return Arrays.asList(universities);
        });
    }

    private List<University> filterUniversityInfo(List<University> universities) {
        List<University> filteredUniversities = new ArrayList<>();
        for (University university : universities) {
            filteredUniversities.add(new University(university.getName(), university.getDomains(), university.getWebPages()));
        }
        return filteredUniversities;
    }

    static class University {
        private String name;
        private String[] domains;
        private String[] webPages;

        public University() {
        }

        public University(String name, String[] domains, String[] webPages) {
            this.name = name;
            this.domains = domains;
            this.webPages = webPages;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getDomains() {
            return domains;
        }

        public void setDomains(String[] domains) {
            this.domains = domains;
        }

        public String[] getWebPages() {
            return webPages;
        }

        public void setWebPages(String[] webPages) {
            this.webPages = webPages;
        }
    }
}
