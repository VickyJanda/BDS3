package org.but.feec.javafx.api;

import ch.qos.logback.classic.Logger;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



public class FineBasicView {

    private LongProperty fineId = new SimpleLongProperty();
    private LongProperty userId = new SimpleLongProperty();
    private LongProperty rentId = new SimpleLongProperty();
    private ObjectProperty<LocalDateTime> fineDueDate = new SimpleObjectProperty<>();
    private DoubleProperty fineTotal = new SimpleDoubleProperty();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Getter and setter methods for fineId
    public Long getId() {
        return fineIdProperty().get();
    }

    public void setId(Long fineId) {
        this.fineIdProperty().set(fineId);
    }

    // Getter and setter methods for userId
    public Long getUserId() {
        return userIdProperty().get();
    }

    public void setUserId(Long userId) {
        this.userIdProperty().set(userId);
    }

    // Getter and setter methods for rentId
    public Long getRentId() {
        return rentIdProperty().get();
    }

    public void setRentId(Long rentId) {
        this.rentIdProperty().set(rentId);
    }

    // Getter method
    public LocalDateTime getFineDueDate() {
        return fineDueDate.get();
    }

    // Setter method
    public void setFineDueDate(LocalDateTime fineDueDate) {
        this.fineDueDate.set(fineDueDate);
    }


    // Getter and setter methods for fineTotal
    public double getFineTotal() {
        return fineTotalProperty().get();
    }

    public void setFineTotal(double fineTotal) {
        this.fineTotalProperty().set(fineTotal);
    }

    // Property accessors for each field
    public LongProperty fineIdProperty() {
        return fineId;
    }

    public LongProperty userIdProperty() {
        return userId;
    }

    public LongProperty rentIdProperty() {
        return rentId;
    }

    public ObjectProperty<LocalDateTime> fineDueDateProperty() {
        return fineDueDate;
    }

    public DoubleProperty fineTotalProperty() {
        return fineTotal;
    }

    @Override
    public String toString() {
        return "FineBasicView{" +
                "fineId=" + fineId.get() +
                ", userId=" + userId.get() +
                ", rentId=" + rentId.get() +
                ", fineDueDate=" + fineDueDate.get() +
                ", fineTotal=" + fineTotal.get() +
                '}';
    }
}
