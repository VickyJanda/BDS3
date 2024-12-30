package org.but.feec.javafx.api;

public class FineEditView {

    private Long id;
    private Long userId;
    private Long rentId;
    private String fineDueDate;
    private Double fineTotal;

    // Getters and Setters for the fine attributes

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getFineDueDate() {
        return fineDueDate;
    }

    public void setFineDueDate(String fineDueDate) {
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
        return "FineEditView{" +
                "id=" + id +
                ", userId=" + userId +
                ", rentId=" + rentId +
                ", fineDueDate='" + fineDueDate + '\'' +
                ", fineTotal=" + fineTotal +
                '}';
    }
}
