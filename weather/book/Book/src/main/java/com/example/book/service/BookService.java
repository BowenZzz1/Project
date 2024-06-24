package com.example.library.service;

import com.example.library.domain.dto.BookDto;
import com.example.library.domain.entity.Author;
import com.example.library.domain.entity.Book;
import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.AuthorRepository;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public BookDto createBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        Set<Author> authors = new HashSet<>();
        for (Long authorId : bookDto.getAuthorIds()) {
            authors.add(authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId)));
        }
        book.setAuthors(authors);
        bookRepository.save(book);
        bookDto.setId(book.getId());
        return bookDto;
    }

    public BookDto getBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        Set<Long> authorIds = new HashSet<>();
        for (Author author : book.getAuthors()) {
            authorIds.add(author.getId());
        }
        bookDto.setAuthorIds(authorIds);
        return bookDto;
    }

    public BookDto updateBook(Long id, BookDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        book.setTitle(bookDto.getTitle());
        Set<Author> authors = new HashSet<>();
        for (Long authorId : bookDto.getAuthorIds()) {
            authors.add(authorRepository.findById(authorId).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + authorId)));
        }
        book.setAuthors(authors);
        bookRepository.save(book);
        return bookDto;
    }

    public void deleteBook(Long id) {
        bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.deleteById(id);
    }
}
