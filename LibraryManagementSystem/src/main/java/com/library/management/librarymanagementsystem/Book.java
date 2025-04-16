package com.library.management.librarymanagementsystem;

public class Book {
    private String author;
    private String title;
    private int book_id;
    private String genre;

    public Book(String author, String title, int book_id, String genre) {
        this.author = author;
        this.title = title;
        this.book_id = book_id;
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public int getBook_id() {
        return book_id;
    }

    public String getGenre() {
        return genre;
    }

    @Override
    public String toString() {
        return title + " by " + author + " [" + genre + "]";
    }
}
