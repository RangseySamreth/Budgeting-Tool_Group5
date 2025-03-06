/*package com.budget.tool.Models;

import javafx.beans.property.*;

public abstract class Account {
    private final StringProperty owner;
    private final StringProperty accountNumber;
    private final DoubleProperty balance;

    // Constructor
    public Account(String owner, String accountNumber, double balance) {
        this.owner = new SimpleStringProperty(this, "Owner", owner);
        this.accountNumber = new SimpleStringProperty(this, "Account Number", accountNumber);
        this.balance = new SimpleDoubleProperty(this, "Balance", balance);
    }

    // JavaFX Properties
    public StringProperty ownerProperty() {
        return owner;
    }

    public StringProperty accountNumberProperty() {
        return accountNumber;
    }

    public DoubleProperty balanceProperty() {
        return balance;
    }

    // Getters
    public String getOwner() {
        return owner.get();
    }

    public String getAccountNumber() {
        return accountNumber.get();
    }

    public double getBalance() {
        return balance.get();
    }

    // Setter for balance (since owner and account number shouldn't change)
    public void setBalance(double newBalance) {
        this.balance.set(newBalance);
    }
}*/
