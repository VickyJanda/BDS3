package org.but.feec.javafx.api;

import java.util.Arrays;

public class BookCreateView {

    private String name;
    private String isbn;
    private String status;
    private Double price;
    private Integer quantity;

    // Getters and Setters for the book attributes

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "BookCreateView{" +
                "name='" + name + '\'' +
                ", isbn='" + isbn + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}
