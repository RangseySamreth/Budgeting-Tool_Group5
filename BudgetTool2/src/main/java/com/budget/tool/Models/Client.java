package com.budget.tool.Models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Client {
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty userAddress;  // Replaced "payeeAddress" with "userAddress"
    private final ObjectProperty<Account> checkingAccount;
    private final ObjectProperty<Account> savingsAccount;
    private final ObjectProperty<LocalDate> dateCreated;

    // Constructor
    public Client(String fName, String lName, String uAddress, Account cAccount, Account sAccount, LocalDate date) {
        this.firstName = new SimpleStringProperty(this, "FirstName", fName);
        this.lastName = new SimpleStringProperty(this, "LastName", lName);
        this.userAddress = new SimpleStringProperty(this, "UserAddress", uAddress);
        this.checkingAccount = new SimpleObjectProperty<>(this, "Checking Account", cAccount);
        this.savingsAccount = new SimpleObjectProperty<>(this, "Savings Account", sAccount);
        this.dateCreated = new SimpleObjectProperty<>(this, "Date Created", date);
    }

    // JavaFX Properties
    public StringProperty firstNameProperty() {
        return firstName;
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public StringProperty userAddressProperty() {
        return userAddress;
    }

    public ObjectProperty<Account> checkingAccountProperty() {
        return checkingAccount;
    }

    public ObjectProperty<Account> savingsAccountProperty() {
        return savingsAccount;
    }

    public ObjectProperty<LocalDate> dateCreatedProperty() {
        return dateCreated;
    }

    // Getters
    public String getFirstName() {
        return firstName.get();
    }

    public String getLastName() {
        return lastName.get();
    }

    public String getUserAddress() {
        return userAddress.get();
    }

    public Account getCheckingAccount() {
        return checkingAccount.get();
    }

    public Account getSavingsAccount() {
        return savingsAccount.get();
    }

    public LocalDate getDateCreated() {
        return dateCreated.get();
    }
}
