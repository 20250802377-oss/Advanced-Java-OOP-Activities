package app;

import model.Expense;
import model.Expense.InvalidExpenseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main extends JFrame {
    private final ExpenseManager manager = new ExpenseManager();
    private final JLabel totalLabel = new JLabel(ExpenseManager.formatCurrency(0), SwingConstants.RIGHT);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"ID", "Date", "Description", "Category", "Amount"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);

    private final JTextField amountField = new JTextField(12);
    private final JComboBox<String> categoryBox = new JComboBox<>(
            new String[]{"Select category", "Food", "Travel", "Shopping", "Education", "Entertainment", "Other"});
    private final JTextField dateField = new JTextField(LocalDate.now().toString(), 12);
    private final JTextField descriptionField = new JTextField(22);

    public Main() {
        super("Daily Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(new Color(238, 243, 238));

        add(createHeader(), BorderLayout.NORTH);
        add(createFormPanel(), BorderLayout.WEST);
        add(createHistoryPanel(), BorderLayout.CENTER);

        loadExpensesInBackground();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Daily Expense Tracker");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        title.setForeground(new Color(38, 55, 43));

        JPanel totalCard = new JPanel(new BorderLayout());
        totalCard.setBackground(new Color(226, 236, 224));
        totalCard.setBorder(BorderFactory.createEmptyBorder(10, 18, 10, 18));
        JLabel totalTitle = new JLabel("Total Expenses");
        totalTitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        totalLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        totalCard.add(totalTitle, BorderLayout.NORTH);
        totalCard.add(totalLabel, BorderLayout.SOUTH);

        header.add(title, BorderLayout.WEST);
        header.add(totalCard, BorderLayout.EAST);
        return header;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 220)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(6, 0, 6, 0);

        JLabel heading = new JLabel("Add Expense");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        c.gridy = 0;
        panel.add(heading, c);

        c.gridy = 1;
        panel.add(new JLabel("Amount"), c);
        c.gridy = 2;
        panel.add(amountField, c);

        c.gridy = 3;
        panel.add(new JLabel("Category"), c);
        c.gridy = 4;
        panel.add(categoryBox, c);

        c.gridy = 5;
        panel.add(new JLabel("Date (YYYY-MM-DD)"), c);
        c.gridy = 6;
        panel.add(dateField, c);

        c.gridy = 7;
        panel.add(new JLabel("Description"), c);
        c.gridy = 8;
        panel.add(descriptionField, c);

        JButton saveButton = createButton("Save Expense");
        saveButton.addActionListener(event -> saveExpense());
        c.gridy = 9;
        c.insets = new Insets(14, 0, 6, 0);
        panel.add(saveButton, c);
        return panel;
    }

    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 228, 220)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));

        JLabel heading = new JLabel("Expense History");
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 17));
        panel.add(heading, BorderLayout.NORTH);

        table.setRowHeight(26);
        table.getTableHeader().setReorderingAllowed(false);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton deleteButton = createButton("Delete Selected");
        deleteButton.addActionListener(event -> deleteExpense());
        JPanel actions = new JPanel();
        actions.setBackground(Color.WHITE);
        actions.add(deleteButton);
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(126, 150, 126));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        return button;
    }

    private void saveExpense() {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            manager.addExpense(
                    amount,
                    (String) categoryBox.getSelectedItem(),
                    dateField.getText(),
                    descriptionField.getText());
            refreshTable();
            clearForm();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Amount must be numeric.", "Invalid Expense", JOptionPane.ERROR_MESSAGE);
        } catch (InvalidExpenseException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(), "Invalid Expense", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Could not save expense: " + ex.getMessage(),
                    "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteExpense() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Please select an expense to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (Integer) tableModel.getValueAt(row, 0);
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete the selected expense?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try {
                manager.deleteExpense(id);
                refreshTable();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Could not delete expense: " + ex.getMessage(),
                        "File Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        amountField.setText("");
        categoryBox.setSelectedIndex(0);
        dateField.setText(LocalDate.now().toString());
        descriptionField.setText("");
        amountField.requestFocus();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Expense expense : manager.getExpenses()) {
            tableModel.addRow(new Object[]{
                    expense.getId(),
                    expense.getDate(),
                    expense.getDescription(),
                    expense.getCategory(),
                    ExpenseManager.formatCurrency(expense.getAmount())
            });
        }
        totalLabel.setText(ExpenseManager.formatCurrency(manager.calculateTotal()));
    }

    private void loadExpensesInBackground() {
        SwingWorker<ArrayList<Expense>, Void> worker =
                new SwingWorker<ArrayList<Expense>, Void>() {
                    @Override
                    protected ArrayList<Expense> doInBackground() throws IOException {
                        return ExpenseFile.loadExpenses();
                    }

                    @Override
                    protected void done() {
                        try {
                            manager.setExpenses(get());
                            refreshTable();
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(Main.this,
                                    "Could not load expenses: " + ex.getMessage(),
                                    "File Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
        worker.execute();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}
