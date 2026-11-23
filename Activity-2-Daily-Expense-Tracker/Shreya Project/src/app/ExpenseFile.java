package app;

import model.Expense;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ExpenseFile {
    private static final String FILE_PATH = "data" + File.separator + "expenses.csv";

    public static ArrayList<Expense> loadExpenses() throws IOException {
        ArrayList<Expense> expenses = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return expenses;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] fields = parseCsvLine(line);
                if (fields.length == 5) {
                    try {
                        expenses.add(new Expense(
                                Integer.parseInt(fields[0].trim()),
                                Double.parseDouble(fields[4].trim()),
                                fields[3].trim(),
                                fields[1].trim(),
                                fields[2].trim()));
                    } catch (NumberFormatException ignored) {
                        // Skip malformed records and keep the rest available.
                    }
                }
            }
        }
        return expenses;
    }

    public static void saveExpenses(ArrayList<Expense> expenses) throws IOException {
        File file = new File(FILE_PATH);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            for (Expense expense : expenses) {
                writer.write(String.join(",",
                        escapeCsv(String.valueOf(expense.getId())),
                        escapeCsv(expense.getDate()),
                        escapeCsv(expense.getDescription()),
                        escapeCsv(expense.getCategory()),
                        escapeCsv(String.valueOf(expense.getAmount()))));
                writer.newLine();
            }
        }
    }

    private static String escapeCsv(String value) {
        String text = value == null ? "" : value;
        if (text.contains(",") || text.contains("\"") || text.contains("\n")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }

    private static String[] parseCsvLine(String line) {
        ArrayList<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;

        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == '"' && quoted && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                field.append('"');
                i++;
            } else if (ch == '"') {
                quoted = !quoted;
            } else if (ch == ',' && !quoted) {
                fields.add(field.toString());
                field.setLength(0);
            } else {
                field.append(ch);
            }
        }
        fields.add(field.toString());
        return fields.toArray(new String[0]);
    }
}
