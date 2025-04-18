package com.library.management.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EditBook {
    @FXML
    private TextField editAuthorField;

    @FXML
    private TextField editTitleField;

    @FXML
    private TextField editIsbnField;

    @FXML
    private TextField editCategoryField;

    @FXML
    private Button editSubmitButton;

    @FXML
    private Button editCancelButton;

    private Stage dialogStage;
    private Book book;
    private boolean submitted = false;

    public void setBook(Book book) {
        this.book = book;

        editAuthorField.setText(book.getAuthor());
        editTitleField.setText(book.getTitle());
        editIsbnField.setText(book.getBook_id());
        editCategoryField.setText(book.getGenre());
    }

    @FXML
    private void handleSubmit() {
        book.setAuthor(editAuthorField.getText());
        book.setTitle(editTitleField.getText());
        book.setBook_id(editIsbnField.getText());
        book.setGenre(editCategoryField.getText());

        submitted = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isSubmitted() {
        return submitted;
    }

    public String getResult() {
        if (submitted) {
            return book.toCsvString();
        }
        return null;
    }

    public static EditBook showDialog(Book bookToEdit) throws IOException {
        FXMLLoader loader = new FXMLLoader(EditBook.class.getResource("edit.fxml"));
        Scene scene = new Scene(loader.load());

        EditBook controller = loader.getController();
        controller.setBook(bookToEdit);

        Stage stage = new Stage();
        stage.setTitle("Edit Book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);

        controller.setDialogStage(stage);
        stage.showAndWait();

        return controller;
    }
}