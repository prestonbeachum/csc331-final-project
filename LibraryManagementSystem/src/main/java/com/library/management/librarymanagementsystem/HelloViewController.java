package com.library.management.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.*;

public class HelloViewController {

    @FXML private TextField authorNameTextField;
    @FXML private ListView<Book> bookListView;
    @FXML private TextField bookNameTextField;
    @FXML private TextField categoryTextField;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private TextField isbnTextField;
    @FXML private TextField searchTextField;
    @FXML private Button sumbitButton;

    private static ObservableList<Book> books = FXCollections.observableArrayList();
    private static final String CSV_FILE = "inventory.csv";

    public static void populateInventory() {
        String[][] bookData = {
                {"To Kill a Mockingbird", "Harper Lee", "Fiction", "115"},
                {"1984", "George Orwell", "Dystopian", "116"},
                {"Pride and Prejudice", "Jane Austen", "Romance", "117"},
                // Add more...
        };

        for (String[] data : bookData) {
            String title = data[0];
            String author = data[1];
            String genre = data[2];
            int book_id = Integer.parseInt(data[3]);

            books.add(new Book(author, title, book_id, genre));
        }

        updateInventory();
    }

    private static boolean updateInventory() {
        try (FileWriter writer = new FileWriter(CSV_FILE)) {
            writer.write("Author,Title,ISBN,Genre\n");
            for (Book b : books) {
                writer.write(String.format("\"%s\",\"%s\",\"%d\",\"%s\"\n",
                        b.getAuthor(), b.getTitle(), b.getBook_id(), b.getGenre()));
            }
            System.out.println("Inventory written to " + CSV_FILE);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            return false;
        }
    }

    public static boolean addBook(String author, String title, int book_id, String genre) {
        books.add(new Book(author, title, book_id, genre));
        return updateInventory();
    }

    public static boolean loadBooksFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            books.clear();
            reader.readLine(); // Skip header

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String author = parts[0].replace("\"", "");
                    String title = parts[1].replace("\"", "");
                    int bookId = Integer.parseInt(parts[2].replace("\"", ""));
                    String genre = parts[3].replace("\"", "");

                    books.add(new Book(author, title, bookId, genre));
                }
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error reading from file: " + e.getMessage());
            return false;
        }
    }

    public static boolean removeBook(int book_id) {
        for (Book book : books) {
            if (book.getBook_id() == book_id) {
                books.remove(book);
                return updateInventory();
            }
        }
        return false;
    }

    public static List<Book> searchInventory(String query) {
        List<Book> results = new ArrayList<>();
        query = query.toLowerCase();

        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(query) ||
                    book.getTitle().toLowerCase().contains(query) ||
                    String.valueOf(book.getBook_id()).contains(query) ||
                    book.getGenre().toLowerCase().contains(query)) {
                results.add(book);
            }
        }

        return results;
    }

    public static ObservableList<Book> getBooks() {
        return books;
    }
}
