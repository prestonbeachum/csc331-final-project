package com.library.management.librarymanagementsystem;

public class Book {
    private String author;
    private String title;
    private String book_id; // Using String for ISBN/book ID
    private String genre;  // Using genre as the category field
    

    // Default constructor required by JavaFX
    public Book() {
        this.author = "";
        this.title = "";
        this.book_id = "";
        this.genre = "";
    }

    // Parameterized constructor
    public Book(String author, String title, String book_id, String genre) {
        this.author = author;
        this.title = title;
        this.book_id = book_id;
        this.genre = genre;
    }

    // Getter and setter methods
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    // Convert book to CSV format string
    public String toCsvString() {
        return author + ";" + title + ";" + book_id + ";" + genre;
    }

    // Create a Book object from CSV string
    public static Book fromCsvString(String csvLine) {
        if (csvLine == null || csvLine.trim().isEmpty()) {
            return null;
        }

        String[] parts = csvLine.split(";");
        if (parts.length >= 4) {
            return new Book(parts[0], parts[1], parts[2], parts[3]);
        }
        return null;
    }

    @Override
    public boolean equals(Object book) {
        if (book.getClass() == getClass()) {
            Book bookCompare = (Book) book;
            return this.author.equals(bookCompare.getAuthor())&&this.title.equals(bookCompare.getTitle())&&this.book_id.equals(bookCompare.getBook_id()) && this.genre.equals(bookCompare.getGenre());
        }
        return false;
    }
}