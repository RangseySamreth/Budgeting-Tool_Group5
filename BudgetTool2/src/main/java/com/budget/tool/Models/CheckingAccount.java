package com.budget.tool.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class CheckingAccount extends Account {
    // The number of transactions a client is allowed per day.
    private final IntegerProperty transactionLimit;

    // Constructor
    public CheckingAccount(String owner, String accountNumber, double balance, int tLimit) {
        super(owner, accountNumber, balance);
        this.transactionLimit = new SimpleIntegerProperty(this, "Transaction Limit", tLimit);
    }

    // Property Getter for JavaFX Binding
    public IntegerProperty transactionLimitProperty() {
        return transactionLimit;
    }

    // Getter
    public int getTransactionLimit() {
        return transactionLimit.get();
    }

    // Setter
    public void setTransactionLimit(int newLimit) {
        this.transactionLimit.set(newLimit);
    }
}
