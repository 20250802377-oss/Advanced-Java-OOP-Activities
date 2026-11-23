# Daily Expense Tracker

A simple Java Swing desktop application for recording and reviewing daily expenses.

## Requirements

- JDK 17 or newer

## Run

From the project directory:

```powershell
javac -d out src\model\Expense.java src\app\ExpenseFile.java src\app\ExpenseManager.java src\app\Main.java
java -cp "out" app.Main
```

Expense records are stored in `data\expenses.csv`. The application creates the file and `data` directory if either is missing.
