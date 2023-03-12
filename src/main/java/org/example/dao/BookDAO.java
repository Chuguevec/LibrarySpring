package org.example.dao;

import org.example.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDAO {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book getById(int id){
        return jdbcTemplate.query("SELECT * FROM book where id =?",new BeanPropertyRowMapper<Book>(),id)
                .stream().findAny().orElse(null);
    }

    public List<Book> getAll(){
        return jdbcTemplate.query("Select * FROM book", new BeanPropertyRowMapper<Book>());
    }

    public void save (Book book){
        jdbcTemplate.update("INSERT INTO book(name, author_id, year) VALUES (name=?, author_id=?, year=?)", book.getName(), book.getAuthor().getId(), book.getYear());
    }

    public void update(Book book){
        jdbcTemplate.update("UPDATE book set name=?, author_id=?, year=? where id=?", book.getName(), book.getAuthor(), book.getYear(), book.getId());
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE from book where id=?", id);
    }

}
