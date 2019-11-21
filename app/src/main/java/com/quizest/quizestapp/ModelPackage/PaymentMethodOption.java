package com.quizest.quizestapp.ModelPackage;

public class PaymentMethodOption {

    private boolean isSelected;
    private String paymentName;
    private int id;

    public PaymentMethodOption(String paymentName, int id) {
        this.paymentName = paymentName;
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getId() {
        return id;
    }

    public String getPaymentName() {
        return paymentName;
    }
}
