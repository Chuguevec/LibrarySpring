package org.example.controller;

import jakarta.validation.Valid;
import org.example.entity.Book;
import org.example.entity.Person;
import org.example.service.BookService;
import org.example.service.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final BookService bookService;

    @Autowired
    public PeopleController(PeopleService peopleService, BookService bookService) {
        this.peopleService = peopleService;
        this.bookService = bookService;
    }

    @GetMapping
    public String showAll(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "people/show";
    }

    @GetMapping("/{id}")
    public String showPersonById(Model model, @PathVariable("id") int id) {
        Person person = peopleService.findOne(id);
        List<Book> books = bookService.findAllByPerson(person);
        person.setBooks(new ArrayList<>(books));
        model.addAttribute("person", person);
        return "people/info";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if(bindingResult.hasErrors()){
            return "people/edit";
        }
        peopleService.update(id,person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        peopleService.delete(id);
        return "redirect:/people";
    }

    @PostMapping
    public String createNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/new";
        }
        peopleService.save(person);
        return "redirect:/people";
    }

}
