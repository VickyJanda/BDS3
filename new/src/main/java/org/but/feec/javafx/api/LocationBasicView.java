package org.but.feec.javafx.api;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LocationBasicView {

    private LongProperty locationId = new SimpleLongProperty();
    private LongProperty libraryId = new SimpleLongProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty street = new SimpleStringProperty();
    private StringProperty houseNumber = new SimpleStringProperty();
    private StringProperty zipCode = new SimpleStringProperty();

    // Getter and setter methods for locationId
    public Long getLocationId() {
        return locationIdProperty().get();
    }

    public void setLocationId(Long locationId) {
        this.locationIdProperty().setValue(locationId);
    }

    // Getter and setter methods for libraryId
    public Long getLibraryId() {
        return libraryIdProperty().get();
    }

    public void setLibraryId(Long libraryId) {
        this.libraryIdProperty().setValue(libraryId);
    }

    // Getter and setter methods for city
    public String getCity() {
        return cityProperty().get();
    }

    public void setCity(String city) {
        this.cityProperty().setValue(city);
    }

    // Getter and setter methods for street
    public String getStreet() {
        return streetProperty().get();
    }

    public void setStreet(String street) {
        this.streetProperty().setValue(street);
    }

    // Getter and setter methods for houseNumber
    public String getHouseNumber() {
        return houseNumberProperty().get();
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumberProperty().setValue(houseNumber);
    }

    // Getter and setter methods for zipCode
    public String getZipCode() {
        return zipCodeProperty().get();
    }

    public void setZipCode(String zipCode) {
        this.zipCodeProperty().setValue(zipCode);
    }

    // Property accessors for each field
    public LongProperty locationIdProperty() {
        return locationId;
    }

    public LongProperty libraryIdProperty() {
        return libraryId;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty streetProperty() {
        return street;
    }

    public StringProperty houseNumberProperty() {
        return houseNumber;
    }

    public StringProperty zipCodeProperty() {
        return zipCode;
    }
}
