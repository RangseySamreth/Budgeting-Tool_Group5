/*package com.budget.tool.Models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class SavingAccount extends Account {
    // The withdrawal limit for savings accounts
    private final DoubleProperty withdrawalLimit;

    // Constructor
    public SavingAccount(String owner, String accountNumber, double balance, double withdrawLimit) {
        super(owner, accountNumber, balance);
        this.withdrawalLimit = new SimpleDoubleProperty(this, "Withdrawal Limit", withdrawLimit);
    }

    // Property Getter for JavaFX Binding
    public DoubleProperty withdrawalLimitProperty() {
        return withdrawalLimit;
    }

    // Getter
    public double getWithdrawalLimit() {
        return withdrawalLimit.get();
    }

    // Setter
    public void setWithdrawalLimit(double newLimit) {
        this.withdrawalLimit.set(newLimit);
    }
}*/
