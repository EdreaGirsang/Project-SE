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

    public ProcessPayment(){}

    public ProcessPayment(String title, String date, String rupiah, PaymentStatus status) {
        this.title = title;
        this.date = date;
        this.rupiah = rupiah;
        this.status = status;
    }
    public PaymentStatus getStatus() {
        return status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRupiah() {
        return rupiah;
    }

    public void setRupiah(String rupiah) {
        this.rupiah = rupiah;
    }
}

