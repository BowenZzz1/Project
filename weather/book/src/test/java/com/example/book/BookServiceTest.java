package com.example.library.service;

import com.example.library.domain.dto.BookDto;
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

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    public BookServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBook() {
        BookDto bookDto = new BookDto();
        bookDto.setTitle("Test Book");
        Set<Long> authorIds = new HashSet<>();
        authorIds.add(1L);
        bookDto.setAuthorIds(authorIds);

        Author author = new Author();
        author.setId(1L);
        author.setName("Test Author");

        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(i -> i.getArguments()[0]);

        BookDto createdBook = bookService.createBook(bookDto);
        assertEquals("Test Book", createdBook.getTitle());
        assertEquals(1, createdBook.getAuthorIds().size());
    }
}
