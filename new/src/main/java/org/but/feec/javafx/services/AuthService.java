package org.but.feec.javafx.services;

import org.but.feec.javafx.api.PersonAuthView;
import org.but.feec.javafx.data.PersonRepository;
import org.but.feec.javafx.exceptions.ResourceNotFoundException;

import static org.but.feec.javafx.services.Argon2FactoryService.ARGON2;

public class AuthService {

    private PersonRepository personRepository;

    public AuthService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    private PersonAuthView findPersonByEmail(String email) {
        return personRepository.findPersonByEmail(email);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        PersonAuthView personAuthView = findPersonByEmail(username);
        if (personAuthView == null) {
            throw new ResourceNotFoundException("Provided username is not found.");
        }
        System.out.println(personAuthView.getPassword());
        System.out.println(password.toCharArray());
        return ARGON2.verify(personAuthView.getPassword(), password.toCharArray());
    }

}
