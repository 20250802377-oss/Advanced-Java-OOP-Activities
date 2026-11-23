package library;

import java.util.ArrayList;

public class Library {
    private ArrayList<Book> books = new ArrayList<>();

    public void addBook(Book book) {
        books.add(book);
        System.out.println("Book added successfully!");
    }

    public void viewBooks() {
        if (books.isEmpty()) {
            System.out.println("No books available.");
            return;
        }

        for (Book book : books) {
            book.displayDetails();
        }
    }

    public void searchBook(int id) {
        for (Book book : books) {
            if (book.getBookId() == id) {
                book.displayDetails();
                return;
            }
        }
        System.out.println("Book not found.");
    }

    public void issueBook(int id) {
        for (Book book : books) {
            if (book.getBookId() == id && !book.isIssued()) {
                book.issueBook();
                System.out.println("Book issued successfully!");
                return;
            }
        }
        System.out.println("Book unavailable or not found.");
    }

    public void returnBook(int id) {
        for (Book book : books) {
            if (book.getBookId() == id && book.isIssued()) {
                book.returnBook();
                System.out.println("Book returned successfully!");
                return;
            }
        }
        System.out.println("Issued book not found.");
    }
}
