
package app;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import model.Expense;
import java.util.ArrayList;

public class Main {

    private static final ExpenseManager manager = new ExpenseManager();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createWindow());
    }

    private static void createWindow() {
        JFrame frame = new JFrame("Daily Expense Tracker");
        frame.setSize(500, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTextField amount = new JTextField();
        JTextField category = new JTextField();
        JTextArea output = new JTextArea(10, 30);
        output.setEditable(false);

        JButton add = new JButton("Add Expense");
        JButton view = new JButton("View Expenses");
        JButton total = new JButton("Calculate Total");
        JButton delete = new JButton("Delete Expense");
        JButton save = new JButton("Save Expenses");

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));

        panel.add(new JLabel("DAILY EXPENSE TRACKER"));
        panel.add(new JLabel("Amount:"));
        panel.add(amount);
        panel.add(new JLabel("Category:"));
        panel.add(category);
        panel.add(add);
        panel.add(view);
        panel.add(total);
        panel.add(delete);
        panel.add(save);

        add.addActionListener(e -> {
            try {
                double value = Double.parseDouble(amount.getText());

                if (value <= 0 || category.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException();
                }

                int id = manager.getExpenses().size() + 1;

                Expense expense = new Expense(
                    id, value, category.getText(),
                    LocalDate.now().toString(), ""
                );

                manager.addExpense(expense);

                JOptionPane.showMessageDialog(frame, "Expense added!");
                amount.setText("");
                category.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a valid amount.");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, "Enter a positive amount and category.");
            }
        });

        view.addActionListener(e -> {
            output.setText("");

            for (Expense expense : manager.getExpenses()) {
                output.append(expense.toString() + "\n");
            }

            JOptionPane.showMessageDialog(frame, new JScrollPane(output));
        });

        total.addActionListener(e -> {
            double sum = manager.calculateTotal();
            JOptionPane.showMessageDialog(frame, "Total Expenses: Rs. " + sum);
        });

        delete.addActionListener(e -> {
            try {
                int id = Integer.parseInt(
                    JOptionPane.showInputDialog(frame, "Enter Expense ID:")
                );

                if (manager.deleteExpense(id)) {
                    JOptionPane.showMessageDialog(frame, "Expense deleted!");
                } else {
                    JOptionPane.showMessageDialog(frame, "Expense ID not found.");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid ID.");
            }
        });

        save.addActionListener(e -> {
            ArrayList<Expense> copy =
                new ArrayList<>(manager.getExpenses());

            save.setEnabled(false);

            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                protected Void doInBackground() {
                    ExpenseFile.saveExpenses(copy);
                    return null;
                }

                protected void done() {
                    save.setEnabled(true);
                    JOptionPane.showMessageDialog(frame, "Save operation completed.");
                }
            };

            worker.execute();
        });

        frame.add(panel);
        frame.setVisible(true);
    }
}
