package org.but.feec.javafx.api;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookBasicView {

    private LongProperty id = new SimpleLongProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty isbn = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty();
    private IntegerProperty price = new SimpleIntegerProperty();
    private IntegerProperty quantity = new SimpleIntegerProperty();


    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }


    public String getName() {
        return nameProperty().get();
    }

    public void setName(String name) {
        this.nameProperty().setValue(name);
    }


    public String getIsbn() {
        return isbnProperty().get();
    }

    public void setIsbn(String isbn) {
        this.isbnProperty().setValue(isbn);
    }


    public String getStatus() {
        return statusProperty().get();
    }

    public void setStatus(String status) {
        this.statusProperty().setValue(status);
    }


    public int getPrice() {
        return priceProperty().get();
    }

    public void setPrice(int price) {
        this.priceProperty().setValue(price);
    }


    public int getQuantity() {
        return quantityProperty().get();
    }

    public void setQuantity(int quantity) {
        this.quantityProperty().setValue(quantity);
    }


    public LongProperty idProperty() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    public StringProperty statusProperty() {
        return status;
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }
}
