import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Book {
    private String title;
    private String author;
    private String genre;
    private int book_id;

    public Book(String author, String title, int book_id, String genre) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.book_id = book_id;
    }

    public void setBook_id(int book_id) {this.book_id = book_id;}

    public int getBook_id() {return book_id;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

//    public void setPrice(double price) {
//        this.price = price;
//    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

//    public boolean isCheckedOut() {
//        return isCheckedOut;
//    }

//    public String getDateCheckedOut() {
//        return dateCheckedOut;
//    }

//    public double getPrice() {
//        return price;
//    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }












}