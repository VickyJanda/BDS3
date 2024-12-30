package org.but.feec.javafx.api;

import ch.qos.logback.classic.Logger;
import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class FineBasicView {

    private LongProperty fineId = new SimpleLongProperty();
    private LongProperty userId = new SimpleLongProperty();
    private LongProperty rentId = new SimpleLongProperty();
    private ObjectProperty<LocalDateTime> fineDueDate = new SimpleObjectProperty<>();
    private DoubleProperty fineTotal = new SimpleDoubleProperty();

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    public Long getId() {
        return fineIdProperty().get();
    }

    public void setId(Long fineId) {
        this.fineIdProperty().set(fineId);
    }


    public Long getUserId() {
        return userIdProperty().get();
    }

    public void setUserId(Long userId) {
        this.userIdProperty().set(userId);
    }


    public Long getRentId() {
        return rentIdProperty().get();
    }

    public void setRentId(Long rentId) {
        this.rentIdProperty().set(rentId);
    }


    public LocalDateTime getFineDueDate() {
        return fineDueDate.get();
    }


    public void setFineDueDate(LocalDateTime fineDueDate) {
        this.fineDueDate.set(fineDueDate);
    }


    public double getFineTotal() {
        return fineTotalProperty().get();
    }

    public void setFineTotal(double fineTotal) {
        this.fineTotalProperty().set(fineTotal);
    }


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
