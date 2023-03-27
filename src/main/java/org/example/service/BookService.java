package org.example.service;

import org.example.entity.Book;
import org.example.entity.Person;
import org.example.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll(boolean sort) {
        if (sort) {
            return bookRepository.findAll(Sort.by("year"));
        }
        return bookRepository.findAll();
    }

    public List<Book> findAll(int page, int itemsPerPage, boolean sort) {
        if (sort) {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year"))).getContent();
        } else {
            return bookRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        }

    }

    public Optional<Book> findOne(int id) {
        return bookRepository.findById(id);
    }

    public List<Book> findByNameStartingWith(String name){
        return bookRepository.findBooksByNameStartingWith(name);
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void update(Book updatedBook) {
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        bookRepository.deleteById(id);
    }

    public List<Book> findAllByPerson(Person person) {
        List<Book> books = bookRepository.findBooksByPerson(person);
        for (Book book : books) {
            book.setExpired(book.getRentedDate().plusDays(10).isBefore(LocalDate.now()));
        }
        return books;
    }

}
