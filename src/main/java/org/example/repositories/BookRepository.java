package org.example.repositories;

import org.example.entity.Book;
import org.example.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findBooksByPerson(Person person);
    List <Book> findBooksByNameStartingWith(String name);
}
