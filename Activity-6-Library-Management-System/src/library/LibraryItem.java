package library;

public abstract class LibraryItem {
    protected int itemId;
    protected String title;

    public LibraryItem(int itemId, String title) {
        this.itemId = itemId;
        this.title = title;
    }

    public abstract void displayDetails();
}
