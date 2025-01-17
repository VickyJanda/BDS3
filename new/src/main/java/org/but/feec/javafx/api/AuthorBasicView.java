package org.but.feec.javafx.api;

import javafx.beans.property.*;

import java.time.LocalDate;

public class AuthorBasicView {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty firstName = new SimpleStringProperty();
    private StringProperty lastName = new SimpleStringProperty();
    private StringProperty country = new SimpleStringProperty();
    private StringProperty mainLanguage = new SimpleStringProperty();
    private ObjectProperty<LocalDate> born = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> death = new SimpleObjectProperty<>();


    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().set(id);
    }


    public String getFirstName() {
        return firstNameProperty().get();
    }

    public void setFirstName(String firstName) {
        this.firstNameProperty().set(firstName);
    }


    public String getLastName() {
        return lastNameProperty().get();
    }

    public void setLastName(String lastName) {
        this.lastNameProperty().set(lastName);
    }


    public String getCountry() {
        return countryProperty().get();
    }

    public void setCountry(String country) {
        this.countryProperty().set(country);
    }


    public String getMainLanguage() {
        return mainLanguageProperty().get();
    }

    public void setMainLanguage(String mainLanguage) {
        this.mainLanguageProperty().set(mainLanguage);
    }


    public LocalDate getBorn() {
        return bornProperty().get();
    }

    public void setBorn(LocalDate born) {
        this.bornProperty().set(born);
    }


    public LocalDate getDeath() {
        return deathProperty().get();
    }

    public void setDeath(LocalDate death) {
        this.deathProperty().set(death);
    }


    public LongProperty idProperty() {
        return id;
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty countryProperty() {
        return country;
    }

    public StringProperty mainLanguageProperty() {
        return mainLanguage;
    }

    public ObjectProperty<LocalDate> bornProperty() {
        return born;
    }

    public ObjectProperty<LocalDate> deathProperty() {
        return death;
    }
}
