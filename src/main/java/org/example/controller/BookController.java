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


import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BookService bookService, PeopleService peopleService) {
        this.bookService = bookService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showPaginatedAndSort(@RequestParam(value = "page", required = false) Integer page,
                                @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                                @RequestParam(value = "sort_by_year", required = false) boolean sortByYear,
                                       Model model) {
        List<Book> books;
        if(page != null && booksPerPage != null){
            books = bookService.findAll(page, booksPerPage, sortByYear);
        } else {
            books = bookService.findAll(sortByYear);
        }

        model.addAttribute("books", books);
        return "/book/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "/book/new";
    }

    @GetMapping("/search")
    public String search (){
        return "/book/search";
    }

    @PostMapping("/search")
    public String findBook (@RequestParam ("title") String title, Model model){
       List<Book> books = bookService.findByNameStartingWith(title);
        model.addAttribute("books", books);
        return "/book/search";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/book/new";
        }
        bookService.save(book);
        return "redirect:/book";
    }

    @GetMapping("/{id}/edit")
    public String editPage(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findOne(id).get());
        return "/book/edit";
    }

    @GetMapping("/{id}")
    public String info(@ModelAttribute("person") Person person, Model model, @PathVariable("id") int id) {
        Book book = bookService.findOne(id).orElse(null);
        model.addAttribute("book", book);
        if (book.getPerson() != null) {
            model.addAttribute("personBook", book.getPerson());
        } else {
            List<Person> people = peopleService.findAll();
            System.out.println(people.size());
            model.addAttribute("people", people);
        }
        return "book/info";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/book";
    }

    @PatchMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "book/edit";
        }
        book.setId(id);
        bookService.update(book);
        return "redirect:/book";
    }

    @PatchMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        Book book = bookService.findOne(id).get();
        book.setPerson(null);
        book.setRentedDate(null);
        bookService.update(book);
        return "redirect:/book/{id}";
    }

    @PatchMapping("/{id}/addPerson")
    public String addPerson(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
        Book book = bookService.findOne(id).get();
        book.setPerson(person);
        book.setRentedDate(LocalDate.now());
        bookService.update(book);
        return "redirect:/book/{id}";
    }
}
