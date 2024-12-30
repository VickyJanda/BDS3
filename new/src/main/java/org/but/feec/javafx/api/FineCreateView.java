package org.but.feec.javafx.api;

import java.time.LocalDate;

public class FineCreateView {

    private Long fineId;
    private Long userId;
    private Long rentId;
    private LocalDate fineDueDate;
    private Double fineTotal;


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
                "fineId=" + fineId +
                ", userId=" + userId +
                ", rentId=" + rentId +
                ", fineDueDate=" + fineDueDate +
                ", fineTotal=" + fineTotal +
                '}';
    }
}
