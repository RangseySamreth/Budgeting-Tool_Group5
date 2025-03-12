/*package com.budget.tool.Models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Transaction {
    private final StringProperty category;  // Category of spending (e.g., "Groceries", "Rent")
    private final DoubleProperty amount;    // Amount spent
    private final ObjectProperty<LocalDate> date;  // Date of the transaction
    private final StringProperty note;  // Optional note (e.g., "Paid electricity bill")

    // Constructor
    public Transaction(String category, double amount, LocalDate date, String note) {
        this.category = new SimpleStringProperty(this, "Category", category);
        this.amount = new SimpleDoubleProperty(this, "Amount", amount);
        this.date = new SimpleObjectProperty<>(this, "Date", date);
        this.note = new SimpleStringProperty(this, "Note", note);
    }

    // Getters for JavaFX Properties
    public StringProperty categoryProperty() {
        return this.category;
    }

    public DoubleProperty amountProperty() {
        return this.amount;
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return this.date;
    }

    public StringProperty noteProperty() {
        return this.note;
    }

    // Getters for Raw Values
    public String getCategory() {
        return category.get();
    }

    public double getAmount() {
        return amount.get();
    }

    public LocalDate getDate() {
        return date.get();
    }

    public String getNote() {
        return note.get();
    }
}*/
