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
        return jdbcTemplate.query("SELECT * FROM book where id =?",new BookMapper(),id)
                .stream().findAny().orElse(null);
    }

    public List<Book> getAll(){
        return jdbcTemplate.query("Select * FROM book", new BookMapper());
    }

    public List<Book> getBooksByPersonID(int id){
        String sql = "SELECT * from book join person on book.person_id = person.id where person.id = ?";
        return jdbcTemplate.query(sql,new BookMapper(), id);
    }

    public void save (Book book){
        jdbcTemplate.update("INSERT INTO book(name, author, year) VALUES (?,?,?)", book.getName(), book.getAuthor(), book.getYear());
    }

    public void update(Book book){
        jdbcTemplate.update("UPDATE book set name=?, author=?, year=? where id=?", book.getName(), book.getAuthor(), book.getYear(), book.getId());
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE from book where id=?", id);
    }

}
