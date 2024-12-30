package org.but.feec.javafx.models;

import javafx.beans.property.*;

public class Person {

    private IntegerProperty id;
    private StringProperty email;
    private StringProperty givenName;
    private StringProperty familyName;
    private IntegerProperty age;
    private StringProperty phoneNumber;

    // Constructor
    public Person(int id, String email, String givenName, String familyName, int age, String phoneNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.email = new SimpleStringProperty(email);
        this.givenName = new SimpleStringProperty(givenName);
        this.familyName = new SimpleStringProperty(familyName);
        this.age = new SimpleIntegerProperty(age);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    // Getters and Setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getGivenName() {
        return givenName.get();
    }

    public void setGivenName(String givenName) {
        this.givenName.set(givenName);
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public String getFamilyName() {
        return familyName.get();
    }

    public void setFamilyName(String familyName) {
        this.familyName.set(familyName);
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public int getAge() {
        return age.get();
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public IntegerProperty ageProperty() {
        return age;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
}
