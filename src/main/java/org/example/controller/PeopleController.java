package org.example.controller;

import org.example.dao.BookDAO;
import org.example.dao.PersonDAO;
import org.example.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDAO personDAO;
    private final BookDAO bookDAO;

    @Autowired
    public PeopleController(PersonDAO personDAO, BookDAO bookDAO) {
        this.personDAO = personDAO;
        this.bookDAO = bookDAO;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("people", personDAO.getAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showPersonById(Model model, @PathVariable("id") int id) {
        Person person = personDAO.getById(id);
        person.setBooks(bookDAO.getBooksByPersonID(person.getId()));
        model.addAttribute("person", person);
        return "people/info";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDAO.getById(id));
        return "people/edit";
    }

    @PatchMapping("{id}")
    public String updatePerson(@ModelAttribute("person") Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        personDAO.update(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personDAO.delete(id);
        return "redirect:/people";
    }

    @PostMapping
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        personDAO.save(person);
        return "redirect:/people";
    }

}
