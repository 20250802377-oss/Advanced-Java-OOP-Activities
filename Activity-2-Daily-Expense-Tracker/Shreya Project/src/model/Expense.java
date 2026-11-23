package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

interface Validatable {
    void validate() throws Expense.InvalidExpenseException;
}

abstract class Transaction {
    private int id;
    private double amount;
    private String date;
    private String description;

    public Transaction() {
    }

    public Transaction(int id, double amount, String date, String description) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    public abstract String getRecordType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

abstract class FinancialRecord extends Transaction {
    private String category;

    public FinancialRecord() {
    }

    public FinancialRecord(int id, double amount, String category, String date, String description) {
        super(id, amount, date, description);
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

public class Expense extends FinancialRecord implements Validatable {
    public static class InvalidExpenseException extends Exception {
        public InvalidExpenseException(String message) {
            super(message);
        }
    }

    public Expense() {
    }

    public Expense(int id, double amount, String category, String date, String description) {
        super(id, amount, category, date, description);
    }

    @Override
    public String getRecordType() {
        return "EXPENSE";
    }

    @Override
    public void validate() throws InvalidExpenseException {
        if (getAmount() <= 0) {
            throw new InvalidExpenseException("Amount must be greater than zero.");
        }
        if (getCategory() == null || getCategory().trim().isEmpty()
                || "Select category".equals(getCategory())) {
            throw new InvalidExpenseException("Please select a category.");
        }
        if (getDate() == null || getDate().trim().isEmpty()) {
            throw new InvalidExpenseException("Date is required.");
        }
        if (getDescription() == null || getDescription().trim().isEmpty()) {
            throw new InvalidExpenseException("Description is required.");
        }
        try {
            LocalDate.parse(getDate().trim());
        } catch (DateTimeParseException ex) {
            throw new InvalidExpenseException("Date must use YYYY-MM-DD format.");
        }
    }
}
