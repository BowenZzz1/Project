package com.example.library.service;

import com.example.library.domain.dto.AuthorDto;
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
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public AuthorDto createAuthor(AuthorDto authorDto) {
        Author author = new Author();
        author.setName(authorDto.getName());
        Set<Book> books = new HashSet<>();
        for (Long bookId : authorDto.getBookIds()) {
            books.add(bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId)));
        }
        author.setBooks(books);
        authorRepository.save(author);
        authorDto.setId(author.getId());
        return authorDto;
    }

    public AuthorDto getAuthor(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setName(author.getName());
        Set<Long> bookIds = new HashSet<>();
        for (Book book : author.getBooks()) {
            bookIds.add(book.getId());
        }
        authorDto.setBookIds(bookIds);
        return authorDto;
    }

    public AuthorDto updateAuthor(Long id, AuthorDto authorDto) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        author.setName(authorDto.getName());
        Set<Book> books = new HashSet<>();
        for (Long bookId : authorDto.getBookIds()) {
            books.add(bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId)));
        }
        author.setBooks(books);
        authorRepository.save(author);
        return authorDto;
    }

    public void deleteAuthor(Long id) {
        authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        authorRepository.deleteById(id);
    }
}
