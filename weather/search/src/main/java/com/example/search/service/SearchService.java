package com.example.search.service;

import com.example.common.domain.GeneralResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class SearchService {

    private final RestTemplate restTemplate;

    public SearchService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public GeneralResponse search(String query) throws ExecutionException, InterruptedException {
        CompletableFuture<Object> bookAuthorFuture = CompletableFuture.supplyAsync(() -> callBookAuthorService(query));
        CompletableFuture<Object> detailsFuture = CompletableFuture.supplyAsync(this::callDetailsService);

        CompletableFuture.allOf(bookAuthorFuture, detailsFuture).join();

        Object bookAuthorResponse = bookAuthorFuture.get();
        Object detailsResponse = detailsFuture.get();

        // Merge results
        Map<String, Object> mergedData = mergeResults(bookAuthorResponse, detailsResponse);

        return new GeneralResponse("200", System.currentTimeMillis(), mergedData);
    }

    @HystrixCommand(fallbackMethod = "fallbackCallBookAuthorService")
    private Object callBookAuthorService(String query) {
        // Call the book/author service and return the result
        return restTemplate.getForObject("http://book-author-service/api/bookAuthor?query=" + query, Object.class);
    }

    @HystrixCommand(fallbackMethod = "fallbackCallBookAuthorService")
    private Object callDetailsService() {
        // Call the details service and return the result
        return restTemplate.getForObject("http://details/port", Object.class);
    }

    private Map<String, Object> mergeResults(Object bookAuthorResponse, Object detailsResponse) {
        // Merge the results from both services
        Map<String, Object> mergedResult = new HashMap<>();
        mergedResult.put("bookAuthor", bookAuthorResponse);
        mergedResult.put("details", detailsResponse);
        return mergedResult;
    }

    // Fallback methods
    private Object fallbackCallBookAuthorService(String query) {
        // Provide a fallback response for book/author service
        return "Fallback response for book/author service";
    }

    private Object fallbackCallDetailsService() {
        // Provide a fallback response for details service
        return "Fallback response for details service";
    }
}
