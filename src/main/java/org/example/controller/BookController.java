package org.example.controller;

import org.example.dao.BookDAO;
import org.example.dao.PersonDAO;
import org.example.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/book")
public class BookController {
    private BookDAO bookDAO;
    private PersonDAO personDAO;
    @Autowired
    public BookController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }

    @GetMapping
    public String show(Model model){
        model.addAttribute("books", bookDAO.getAll());
        return "/book/show";
    }
    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "/book/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "/book/new";
        }
        bookDAO.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id){
    model.addAttribute("book", bookDAO.getById(id));
    return "/book/edit";
    }

    @GetMapping("/{id}")
    public String info(Model model, @PathVariable("id") int id){
        Book book = bookDAO.getById(id);
        model.addAttribute("book", book);
        if(book.getPersonId() != null){
            model.addAttribute("person", personDAO.getById(book.getPersonId()));
        }else {
            model.addAttribute("people", personDAO.getAll());
        }
        return "book/info";
    }
}
