package com.example.project2.model;

public class Reservation {
    private int reservationId;
    private int tableId;
    private String customerName;
    private String customerPhone;
    private String status;

    public Reservation(int reservationId, int tableId, String customerName,
                       String customerPhone, String status) {
        this.reservationId = reservationId;
        this.tableId = tableId;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.status = status;
    }

    public int getReservationId() { return reservationId; }
    public void setReservationId(int reservationId) { this.reservationId = reservationId; }

    public int getTableId() { return tableId; }
    public void setTableId(int tableId) { this.tableId = tableId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Reservation{id=" + reservationId + ", tableId=" + tableId + "}";
    }
}