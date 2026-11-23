package library;

public class Book extends LibraryItem implements Borrowable {
    private String author;
    private boolean issued;

    public Book(int bookId, String title, String author) {
        super(bookId, title);
        this.author = author;
        this.issued = false;
    }

    public int getBookId() {
        return itemId;
    }

    public boolean isIssued() {
        return issued;
    }

    @Override
    public void issueBook() {
        issued = true;
    }

    @Override
    public void returnBook() {
        issued = false;
    }

    @Override
    public void displayDetails() {
        System.out.println(itemId + " | " + title + " | "
                + author + " | "
                + (issued ? "Issued" : "Available"));
    }
}
