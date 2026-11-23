package app;

import model.Expense;
import java.io.*;
import java.util.ArrayList;

public class ExpenseFile {

    private static final String FILE_NAME = "expenses.txt";

    public static void saveExpenses(ArrayList<Expense> expenses) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense e : expenses) {
                writer.println(e.getId() + "," + e.getAmount() + ","
                        + e.getCategory() + "," + e.getDate() + ","
                        + e.getDescription());
            }
            System.out.println("Expenses saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving expenses: " + e.getMessage());
        }
    }
}
