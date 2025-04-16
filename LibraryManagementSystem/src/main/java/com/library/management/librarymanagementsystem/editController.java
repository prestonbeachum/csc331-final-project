package com.library.management.librarymanagementsystem;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class editController {

    @FXML
    private TextField EditAuthorTextField;

    @FXML
    private Button EditCancelTextField;

    @FXML
    private TextField EditCategoryTextField;

    @FXML
    private TextField EditISBNTextField;

    @FXML
    private Button EditSubmitTextField;

    @FXML
    private TextField EditTitleTextField;

    @FXML
    private TextField author;

    @FXML
    private TextField book; // Title field

    @FXML
    private TextField isbn;

    @FXML
    private TextField category;

    @FXML
    private TextField search;

    @FXML
    private ListView<String> booksList;

    private static final String CSV_FILE_PATH = "src/main/resources/inventory.csv";

    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Ensure the CSV file exists
            createCsvIfNotExists();
            loadBooks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        booksList.setFixedCellSize(50.0);
    }

    private void createCsvIfNotExists() throws IOException {
        File csvFile = new File(CSV_FILE_PATH);
        if (!csvFile.exists()) {
            File parentDir = csvFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            csvFile.createNewFile();
        }
    }

    @FXML
    protected void searchBook() throws IOException {
        String searchText = search.getText().strip().toLowerCase();
        loadBooks();

        if (searchText.length() >= 3) {
            ArrayList<String> results = new ArrayList<>();

            for (String book : booksList.getItems()) {
                if (book.toLowerCase().contains(searchText)) {
                    results.add(book);
                }
            }

            booksList.getItems().clear();
            if (!results.isEmpty()) {
                for (String foundBook : results) {
                    booksList.getItems().add(foundBook);
                }
                booksList.refresh();
            }
        }
    }

    @FXML
    protected void editBook() throws IOException {
        ObservableList<Integer> selectedIndices = booksList.getSelectionModel().getSelectedIndices();

        if (selectedIndices.size() == 1) {
            String bookToEdit = booksList.getItems().get(selectedIndices.get(0));
            Book selectedBook = Book.fromCsvString(bookToEdit);
            String oldIsbn = selectedBook.getBook_id();

            // Show edit dialog
            EditBook editDialog = EditBook.showDialog(selectedBook);
            String updatedBookInfo = editDialog.getResult();

            if (updatedBookInfo != null) {
                // Read all books from CSV
                List<String> allBooks = readAllBooksFromCsv();

                // Update the specific book
                List<String> updatedBooks = allBooks.stream()
                        .map(bookLine -> {
                            Book book = Book.fromCsvString(bookLine);
                            if (book != null && book.getBook_id().equals(oldIsbn)) {
                                return updatedBookInfo;
                            }
                            return bookLine;
                        })
                        .collect(Collectors.toList());

                // Write all books back to CSV
                writeBooksToCsv(updatedBooks);

                loadBooks();
                search.setText("");
            }
        }
    }

    @FXML
    protected void deleteBook() throws IOException {
        ObservableList<Integer> selectedIndices = booksList.getSelectionModel().getSelectedIndices();

        if (selectedIndices.size() == 1) {
            String bookToDelete = booksList.getItems().get(selectedIndices.get(0));
            Book selectedBook = Book.fromCsvString(bookToDelete);
            String isbnToDelete = selectedBook.getBook_id();

            // Read all books
            List<String> allBooks = readAllBooksFromCsv();

            // Filter out the book to delete
            List<String> updatedBooks = allBooks.stream()
                    .filter(bookLine -> {
                        Book book = Book.fromCsvString(bookLine);
                        return book == null || !book.getBook_id().equals(isbnToDelete);
                    })
                    .collect(Collectors.toList());

            // Write remaining books back to CSV
            writeBooksToCsv(updatedBooks);

            loadBooks();
            search.setText("");
        }
    }

    @FXML
    protected void addItem() throws IOException {
        String authorText = author.getText();
        String titleText = book.getText();
        String isbnText = isbn.getText();
        String categoryText = category.getText();

        if (authorText.isEmpty() || titleText.isEmpty() || isbnText.isEmpty() || categoryText.isEmpty()) {
            return; // Don't add incomplete entries
        }

        Book newBook = new Book(authorText, titleText, isbnText, categoryText);

        // Check if book with this ISBN already exists
        List<String> allBooks = readAllBooksFromCsv();
        boolean isbnExists = allBooks.stream()
                .map(Book::fromCsvString)
                .filter(Objects::nonNull)
                .anyMatch(b -> b.getBook_id().equals(isbnText));

        if (!isbnExists) {
            // Add the new book
            allBooks.add(newBook.toCsvString());
            writeBooksToCsv(allBooks);

            // Clear input fields
            author.setText("");
            book.setText("");
            isbn.setText("");
            category.setText("");

            loadBooks();
        } else {
            // Handle duplicate ISBN (could show an alert)
            System.out.println("Book with ISBN " + isbnText + " already exists!");
        }
    }

    private List<String> readAllBooksFromCsv() throws IOException {
        try {
            return Files.readAllLines(Paths.get(CSV_FILE_PATH));
        } catch (IOException e) {
            // If file doesn't exist or is empty
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

    public void loadBooks() throws IOException {
        List<String> books = readAllBooksFromCsv();
        booksList.getItems().clear();
        booksList.getItems().addAll(books);
        booksList.refresh();
    }

    public static void changeScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("library.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = HelloApplication.getPrimaryStage();
        stage.hide();
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }
}