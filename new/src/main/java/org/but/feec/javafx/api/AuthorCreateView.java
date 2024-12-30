package org.but.feec.javafx.api;

import java.time.LocalDate;

public class AuthorCreateView {

    private String firstName;
    private String lastName;
    private String country;
    private String mainLanguage;
    private LocalDate born;
    private LocalDate death;

    // Getters and Setters for the author attributes

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMainLanguage() {
        return mainLanguage;
    }

    public void setMainLanguage(String mainLanguage) {
        this.mainLanguage = mainLanguage;
    }

    public LocalDate getBorn() {
        return born;
    }

    public void setBorn(LocalDate born) {
        this.born = born;
    }

    public LocalDate getDeath() {
        return death;
    }

    public void setDeath(LocalDate death) {
        this.death = death;
    }

    @Override
    public String toString() {
        return "AuthorCreateView{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", mainLanguage='" + mainLanguage + '\'' +
                ", born=" + born +
                ", death=" + death +
                '}';
    }
}
