package sonnh.opt.opt_plan.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import sonnh.opt.opt_plan.model.Book;
import sonnh.opt.opt_plan.service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Retrieve all books", description = "Fetches a list of all books in the system.")
    @GetMapping
    public List<Book> findAll() {
        return bookService.findAll();
    }

    @Operation(summary = "Find a book by ID", description = "Fetches a book by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    @GetMapping("/{id}")
    public Optional<Book> findById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    @Operation(summary = "Create a new book", description = "Adds a new book to the system.")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.save(book);
    }

    @Operation(summary = "Update an existing book", description = "Updates the details of an existing book.")
    @PutMapping
    public Book update(@RequestBody Book book) {
        return bookService.save(book);
    }

    @Operation(summary = "Delete a book", description = "Removes a book from the system by its ID.")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        bookService.deleteById(id);
    }

    @Operation(summary = "Find books by title", description = "Fetches a list of books that match the given title.")
    @GetMapping("/find/title/{title}")
    public List<Book> findByTitle(@PathVariable String title) {
        return bookService.findByTitle(title);
    }

    @Operation(summary = "Find books published after a certain date", description = "Fetches a list of books published after the specified date.")
    @GetMapping("/find/date-after/{date}")
    public List<Book> findByPublishedDateAfter(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return bookService.findByPublishedDateAfter(date);
    }
}
