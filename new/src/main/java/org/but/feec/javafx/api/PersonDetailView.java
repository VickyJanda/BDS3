package org.but.feec.javafx.api;

import javafx.beans.property.*;

public class PersonDetailView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty age = new SimpleStringProperty();  // Changed to match schema
    private StringProperty phoneNumber = new SimpleStringProperty();
    private StringProperty role = new SimpleStringProperty();

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getEmail() {
        return emailProperty().get();
    }

    public void setEmail(String email) {
        this.emailProperty().setValue(email);
    }

    public String getGivenName() {
        return givenNameProperty().get();
    }

    public void setGivenName(String givenName) {
        this.givenNameProperty().setValue(givenName);
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public void setFamilyName(String familyName) {
        this.familyNameProperty().setValue(familyName);
    }

    public String getAge() {
        return ageProperty().get();
    }

    public void setAge(String age) {
        this.ageProperty().setValue(age);
    }

    public String getPhoneNumber() {
        return phoneNumberProperty().get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumberProperty().setValue(phoneNumber);
    }

    public String getRole() {
        return roleProperty().get();
    }

    public void setRole(String role) {
        this.roleProperty().setValue(role);
    }

    // Property accessors
    public LongProperty idProperty() {
        return id;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
    public StringProperty roleProperty() {
        return role;
    }
}
