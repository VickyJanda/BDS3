package org.but.feec.javafx.services;

import org.but.feec.javafx.api.AuthorBasicView;
import org.but.feec.javafx.api.AuthorCreateView;
import org.but.feec.javafx.api.AuthorEditView;
import org.but.feec.javafx.data.AuthorRepository;

import java.util.List;

/**
 * Class representing business logic on top of the Authors
 */
public class AuthorService {

    private AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorBasicView> getAuthorsBasicView() {
        return authorRepository.getAuthorsBasicView();
    }

    public void createAuthor(AuthorCreateView authorCreateView) {
        // Any additional business logic before saving the author can be added here
        authorRepository.createAuthor(authorCreateView);
    }

    public void editAuthor(AuthorEditView authorEditView) {
        authorRepository.editAuthor(authorEditView);
    }

    public void deleteAuthorById(Long id) {
        authorRepository.deleteAuthorById(id);
    }
}
