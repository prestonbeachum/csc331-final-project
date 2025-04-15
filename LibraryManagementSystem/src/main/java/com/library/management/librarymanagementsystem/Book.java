import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Book {
    private String title;
    private String author;
    private String genre;
    private String bookId;

    public Book(String title, String author, String genre, String bookId) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.bookId = bookId
    }

    public void setCheckedOut(boolean checkedOut) {
        isCheckedOut = checkedOut;
        if (checkedOut) {
            dateCheckedOut = LocalDate.now().toString(); // or use a custom format
        } else {
            dateCheckedOut = null;
        }
    }

    public void updateOverdueStatus() {
        if (isCheckedOut && dateCheckedOut != null) {
            LocalDate checkoutDate = LocalDate.parse(dateCheckedOut);
            overdue = ChronoUnit.DAYS.between(checkoutDate, LocalDate.now()) >= 30;
        } else {
            overdue = false;
        }
    }

    public boolean isOverdue() {
        updateOverdueStatus(); // ensures it always reflects current status
        return overdue;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCheckedOut() {
        return isCheckedOut;
    }

    public String getDateCheckedOut() {
        return dateCheckedOut;
    }

    public double getPrice() {
        return price;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }
}