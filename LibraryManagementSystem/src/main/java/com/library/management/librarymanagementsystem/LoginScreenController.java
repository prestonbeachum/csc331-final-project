package com.library.management.librarymanagementsystem;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class LoginScreenController{

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;

    private static final String CSV_FILE_PATH = "src/main/resources/users.csv";

     /**
     * Sets up scene on application startup
     * @param url
     * @param resourceBundle
     */
    @FXML
    public void initialize() {
        try {
            createCsvIfNotExists();
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
    
    @FXML
    private void setupEventHandlers() {
        loginButton.setOnAction(event -> {
            try {
                login();
            } 
            catch (Exception e) {
                e.printStackTrace();
                showAlert(AlertType.ERROR, "Error", "An error occurred: " + e.getMessage());
            }
    });
    }

    /**
     * Generates a list of user objects from csv file
     * @return list of user objects
     */
    public ArrayList<User> getUsers() {
        String file = CSV_FILE_PATH;
        BufferedReader reader;
        String line;
        ArrayList<User> users = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(file));
            while((line = reader.readLine()) != null) {
                users.add(User.fromCSVString(line));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Invalid User Data", "There was a problem reading user data");
        }
        return null;
    }
 
    public void login() {
        try {
            ArrayList<User> users = getUsers();
            for(int i = 0; i < users.size(); i++) {
                if(usernameField.getText().toLowerCase().equals(users.get(i).getUsername()) && passwordField.getText().equals(users.get(i).getPassword())) {
                    User currentUser = users.get(i);
                    if (currentUser.getAdmin()) {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
                        Parent root = loader.load();
                        Scene adminScene = new Scene(root);
                        Stage stage = HelloApplication.getPrimaryStage();
                        stage.setScene(adminScene);
                        stage.setTitle("Library Management System");
                        stage.show();
                    }
                    else {
                        // Load the edit view FXML
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-screen.fxml"));
                        Parent root = loader.load();

                        // Get the controller and set the book data
                        UserScreenController userScreenController = loader.getController();
                        userScreenController.setUserVariables(currentUser, users);

                        // Set up the new scene
                        Scene userScene = new Scene(root);
                        Stage stage = HelloApplication.getPrimaryStage();
                        stage.setScene(userScene);
                        stage.setTitle("Library");
                        stage.show();
                }
            }
        }
    }
        catch(Exception e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Trouble Logging In", e.getMessage());
        }
    }
    

    /**
     * Method displays an alert
     * @param alertType defines what type of alert the user will see
     * @param title title of the alert
     * @param message message displayed to user
     */
    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
