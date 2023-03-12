package org.example.dao;

import org.example.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Person getById(int id){
        return jdbcTemplate.query("SELECT * FROM person where id =?",new PersonMapper(),id)
                .stream().findAny().orElse(null);
    }

    public List<Person> getAll(){
        return jdbcTemplate.query("Select * FROM person", new PersonMapper());
    }

    public void save (Person person){
        jdbcTemplate.update("INSERT INTO person(name, age) VALUES (?,?)", person.getName(), person.getAge());
    }

    public void update(Person person){
        jdbcTemplate.update("UPDATE person set name=?, age=? where id=?", person.getName(), person.getAge(), person.getId());
    }

    public void delete(int id){
        jdbcTemplate.update("DELETE from person where id=?", id);
    }

}
