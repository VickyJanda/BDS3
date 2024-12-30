package org.but.feec.javafx.services;

import org.but.feec.javafx.api.PersonBasicView;
import org.but.feec.javafx.api.PersonCreateView;
import org.but.feec.javafx.api.PersonDetailView;
import org.but.feec.javafx.api.PersonEditView;
import org.but.feec.javafx.data.PersonRepository;

import java.util.List;

import static org.but.feec.javafx.services.Argon2FactoryService.ARGON2;

/**
 * Class representing business logic on top of the Persons
 */
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDetailView getPersonDetailView(Long id) {
        return personRepository.findPersonDetailedView(id);
    }

    public List<PersonBasicView> getPersonsBasicView() {
        return personRepository.getPersonsBasicView();
    }

    public void createPerson(PersonCreateView personCreateView) {
        // the following three lines can be written in one code line (only for more clear explanation it is written in three lines
        char[] originalPassword = personCreateView.getPwd();
        char[] hashedPassword = hashPassword(originalPassword);
        personCreateView.setPwd(hashedPassword);

        personRepository.createPerson(personCreateView);
    }

    public void editPerson(PersonEditView personEditView) {
        personRepository.editPerson(personEditView);
    }

    /**
     * <p>
     * Note: For implementation details see: https://github.com/phxql/argon2-jvm
     * </p>
     *
     * @param password to be hashed
     * @return hashed password
     */
    public char[] hashPassword(char[] password) {
        return ARGON2.hash(10, 65536, 1, password).toCharArray();
    }

    public void deletePersonById(Long id) {
        personRepository.deletePersonById(id);
    }


}