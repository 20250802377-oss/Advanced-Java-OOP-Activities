package app;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import model.Expense;

public class Main {
    private static final ArrayList<Expense> expenses = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Daily Expense Tracker");
            frame.setSize(450, 350);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JLabel title = new JLabel("Daily Expense Tracker", JLabel.CENTER);
            JTextField amount = new JTextField();
            JTextField category = new JTextField();
            JTextArea output = new JTextArea();

            JButton add = new JButton("Add Expense");
            JButton view = new JButton("View Expenses");

            JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
            panel.add(title);
            panel.add(new JLabel("Amount:"));
            panel.add(amount);
            panel.add(new JLabel("Category:"));
            panel.add(category);
            panel.add(add);
            panel.add(view);

            add.addActionListener(e -> {
                try {
                    double value = Double.parseDouble(amount.getText());
                    if (value <= 0) {
                        throw new IllegalArgumentException("Amount must be positive.");
                    }
                    expenses.add(new Expense(expenses.size() + 1, value,
                            category.getText(), "2026-11-23", ""));
                    JOptionPane.showMessageDialog(frame, "Expense added!");
                    amount.setText("");
                    category.setText("");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid details.");
                }
            });

            view.addActionListener(e -> {
                output.setText("");
                for (Expense expense : expenses) {
                    output.append(expense.toString() + "\n");
                }
                JOptionPane.showMessageDialog(frame, new JScrollPane(output));
            });

            frame.add(panel);
            frame.setVisible(true);
        });
    }
}
