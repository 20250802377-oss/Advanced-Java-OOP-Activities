package app;

import model.Expense;
import model.Expense.InvalidExpenseException;

import java.io.IOException;
import java.util.ArrayList;

public class ExpenseManager {
    public static final String CURRENCY = "\u20B9";

    private final ArrayList<Expense> expenses = new ArrayList<>();

    public void setExpenses(ArrayList<Expense> loadedExpenses) {
        expenses.clear();
        expenses.addAll(loadedExpenses);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public Expense addExpense(double amount, String category, String date, String description)
            throws InvalidExpenseException, IOException {
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            throw new InvalidExpenseException("Amount must be numeric.");
        }

        Expense expense = new Expense(nextId(), amount, category, date.trim(), description.trim());
        expense.validate();
        expenses.add(expense);
        ExpenseFile.saveExpenses(expenses);
        return expense;
    }

    public boolean deleteExpense(int id) throws IOException {
        for (int i = 0; i < expenses.size(); i++) {
            if (expenses.get(i).getId() == id) {
                expenses.remove(i);
                ExpenseFile.saveExpenses(expenses);
                return true;
            }
        }
        return false;
    }

    public double calculateTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public double calculateTotal(String category) {
        double total = 0;
        for (Expense expense : expenses) {
            if (expense.getCategory().equalsIgnoreCase(category)) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    public double calculateTotal(String startDate, String endDate) {
        double total = 0;
        for (Expense expense : expenses) {
            String date = expense.getDate();
            if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                total += expense.getAmount();
            }
        }
        return total;
    }

    public static String formatCurrency(double amount) {
        return CURRENCY + String.format("%.2f", amount);
    }

    private int nextId() {
        int maxId = 0;
        for (Expense expense : expenses) {
            maxId = Math.max(maxId, expense.getId());
        }
        return maxId + 1;
    }
}
