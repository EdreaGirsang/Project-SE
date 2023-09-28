package com.example.manakos;

public class ProcessPayment {

    public enum PaymentStatus {
        UNPAID,
        ONGOING,
        PAID
    }

    String title;
    String date;
    String rupiah;
    private PaymentStatus status;

    public ProcessPayment() {}

    public ProcessPayment(String title, String date, String rupiah, PaymentStatus status) {
        this.title = title;
        this.date = date;
        this.rupiah = rupiah;
        this.status = status;
    }
    public PaymentStatus getStatus() {
        return status;
    }

}

