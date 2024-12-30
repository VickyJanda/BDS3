package org.but.feec.javafx.services;

import org.but.feec.javafx.api.BookBasicView;
import org.but.feec.javafx.api.BookCreateView;
import org.but.feec.javafx.api.BookEditView;
import org.but.feec.javafx.data.BookRepository;

import java.util.List;

/**
 * Class representing business logic on top of the Books
 */
public class BookService {

    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<BookBasicView> getBooksBasicView() {
        return bookRepository.getBooksBasicView();
    }

    public void createBook(BookCreateView bookCreateView) {
        // Any additional business logic before saving the book can be added here
        bookRepository.createBook(bookCreateView);
    }

    public void editBook(BookEditView bookEditView) {
        bookRepository.editBook(bookEditView);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteBookById(id);
    }
}