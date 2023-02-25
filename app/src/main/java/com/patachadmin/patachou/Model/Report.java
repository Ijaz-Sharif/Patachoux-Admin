package com.patachadmin.patachou.Model;

public class Report {
    String orderDate,orderPayment,orderTime;

    public Report(String orderDate, String orderPayment, String orderTime) {
        this.orderDate = orderDate;
        this.orderPayment = orderPayment;
        this.orderTime = orderTime;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getOrderPayment() {
        return orderPayment;
    }

    public String getOrderTime() {
        return orderTime;
    }
}
