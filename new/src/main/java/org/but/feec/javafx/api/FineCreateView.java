package org.but.feec.javafx.api;

import java.time.LocalDate;

public class FineCreateView {

    private Long fineId;         // Added fineId field
    private Long userId;
    private Long rentId;
    private LocalDate fineDueDate;  // Change to LocalDate
    private Double fineTotal;

    // Getters and Setters for the fine attributes

    public Long getId() {
        return fineId;
    }

    public void setId(Long fineId) {
        this.fineId = fineId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRentId() {
        return rentId;
    }

    public void setRentId(Long rentId) {
        this.rentId = rentId;
    }

    public LocalDate getFineDueDate() {
        return fineDueDate;
    }

    public void setFineDueDate(LocalDate fineDueDate) {
        this.fineDueDate = fineDueDate;
    }

    public Double getFineTotal() {
        return fineTotal;
    }

    public void setFineTotal(Double fineTotal) {
        this.fineTotal = fineTotal;
    }

    @Override
    public String toString() {
        return "FineCreateView{" +
                "fineId=" + fineId +  // Include fineId in the toString method
                ", userId=" + userId +
                ", rentId=" + rentId +
                ", fineDueDate=" + fineDueDate +  // Display LocalDate
                ", fineTotal=" + fineTotal +
                '}';
    }
}
