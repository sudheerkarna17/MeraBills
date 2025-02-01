package com.example.merabills.data;

public class Payment {
    private String type;  //Only 3 Types: Cash, Bank Transfer, Credit Card
    private double amount;
    private String provider;
    private String transactionRef;

    public Payment(String type, double amount, String provider, String transactionRef) {
        this.type = type;
        this.amount = amount;
        this.provider = provider;
        this.transactionRef = transactionRef;
    }

    // Getters
    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getProvider() {
        return provider;
    }

    public String getTransactionRef() {
        return transactionRef;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setTransactionRef(String transactionRef) {
        this.transactionRef = transactionRef;
    }
}
