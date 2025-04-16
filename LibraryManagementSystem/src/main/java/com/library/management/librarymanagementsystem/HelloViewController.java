package com.library.management.librarymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloViewController implements Initializable {
    @FXML
    private TextField searchTextField;

    @FXML
    private TextField authorNameTextField;

    @FXML
    private TextField bookNameTextField;

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField categoryTextField;

    @FXML
    private Button sumbitButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ListView<String> bookListView;

    private static final String CSV_FILE_PATH = "src/main/resources/inventory.csv";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Create CSV file if it doesn't exist
            createCsvIfNotExists();

            // Load books from CSV file
            loadBooks();

            // Set up event handlers
            setupEventHandlers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createCsvIfNotExists() throws IOException {
        File csvFile = new File(CSV_FILE_PATH);
        if (!csvFile.exists()) {
            // Create directory if it doesn't exist
            File parentDir = csvFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            // Create empty CSV file
            csvFile.createNewFile();

        }
    }

    private void setupEventHandlers() {
        // Search button functionality is handled by method in FXML

        // Submit button for adding new books
        sumbitButton.setOnAction(event -> {
            try {
                addBook();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Edit button for editing selected books
        editButton.setOnAction(event -> {
            try {
                editBook();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Delete button for removing selected books
        deleteButton.setOnAction(event -> {
            try {
                deleteBook();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Search text field functionality
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                searchBook(newValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadBooks() throws IOException {
        bookListView.getItems().clear();

        List<String> books = readAllBooksFromCsv();
        bookListView.getItems().addAll(books);
    }

    private List<String> readAllBooksFromCsv() throws IOException {
        try {
            return Files.readAllLines(Paths.get(CSV_FILE_PATH));
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void writeBooksToCsv(List<String> books) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            for (String book : books) {
                writer.write(book);
                writer.newLine();
            }
        }
    }

    private void addBook() throws IOException {
        String author = authorNameTextField.getText().trim();
        String title = bookNameTextField.getText().trim();
        String isbn = isbnTextField.getText().trim();
        String genre = categoryTextField.getText().trim();

        if (author.isEmpty() || title.isEmpty() || isbn.isEmpty() || genre.isEmpty()) {
            return; // Don't add incomplete entries
        }

        Book newBook = new Book(author, title, isbn, genre);

        // Check if ISBN already exists
        List<String> allBooks = readAllBooksFromCsv();
        boolean isbnExists = allBooks.stream()
                .map(Book::fromCsvString)
                .anyMatch(b -> b != null && b.getBook_id().equals(isbn));

        if (!isbnExists) {
            // Add the new book
            allBooks.add(newBook.toCsvString());
            writeBooksToCsv(allBooks);

            // Clear input fields
            authorNameTextField.clear();
            bookNameTextField.clear();
            isbnTextField.clear();
            categoryTextField.clear();

            // Reload the book list
            loadBooks();
        } else {
            System.out.println("Book with ISBN " + isbn + " already exists!");
        }
    }

    private void editBook() throws IOException {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }

        Book bookToEdit = Book.fromCsvString(selectedBook);
        if (bookToEdit == null) {
            return;
        }

        // Set edit form fields
        authorNameTextField.setText(bookToEdit.getAuthor());
        bookNameTextField.setText(bookToEdit.getTitle());
        isbnTextField.setText(bookToEdit.getBook_id());
        categoryTextField.setText(bookToEdit.getGenre());

        // Delete the old book (will be replaced when user submits the edit)
        deleteBook();
    }

    private void deleteBook() throws IOException {
        String selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            return;
        }

        Book bookToDelete = Book.fromCsvString(selectedBook);
        if (bookToDelete == null) {
            return;
        }

        String isbnToDelete = bookToDelete.getBook_id();

        // Read all books from CSV
        List<String> allBooks = readAllBooksFromCsv();

        // Filter out the book to delete
        List<String> updatedBooks = new ArrayList<>();
        for (String book : allBooks) {
            Book b = Book.fromCsvString(book);
            if (b == null || !b.getBook_id().equals(isbnToDelete)) {
                updatedBooks.add(book);
            }
        }

        // Write updated list back to CSV
        writeBooksToCsv(updatedBooks);

        // Reload the book list
        loadBooks();
    }

    private void searchBook(String searchText) throws IOException {
        if (searchText == null || searchText.trim().length() < 3) {
            loadBooks(); // Show all books if search term is too short
            return;
        }

        searchText = searchText.toLowerCase().trim();

        // Load all books first
        List<String> allBooks = readAllBooksFromCsv();
        List<String> matchingBooks = new ArrayList<>();

        // Filter books based on search text
        for (String bookLine : allBooks) {
            if (bookLine.toLowerCase().contains(searchText)) {
                matchingBooks.add(bookLine);
            }
        }

        // Update the ListView
        bookListView.getItems().clear();
        bookListView.getItems().addAll(matchingBooks);
    }
}