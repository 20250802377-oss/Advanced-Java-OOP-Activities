package app;

import model.Expense;
import java.util.ArrayList;

public class ExpenseManager {
    private final ArrayList<Expense> expenses = new ArrayList<>();

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public ArrayList<Expense> getExpenses() {
        return expenses;
    }

    public double calculateTotal() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        return total;
    }

    public boolean deleteExpense(int id) {
        return expenses.removeIf(expense -> expense.getId() == id);
    }
}
