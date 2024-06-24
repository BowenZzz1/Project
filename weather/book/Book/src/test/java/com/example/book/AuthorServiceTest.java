package com.example.library.service;

import com.example.library.domain.dto.AuthorDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.Book;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorService authorService;

    public AuthorServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAuthor() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setName("Test Author");
        Set<Long> bookIds = new HashSet<>();
        bookIds.add(1L);
        authorDto.setBookIds(bookIds);

        Book book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(authorRepository.save(any(Author.class))).thenAnswer(i -> i.getArguments()[0]);

        AuthorDto createdAuthor = authorService.createAuthor(authorDto);
        assertEquals("Test Author", createdAuthor.getName());
        assertEquals(1, createdAuthor.getBookIds().size());
    }
}
