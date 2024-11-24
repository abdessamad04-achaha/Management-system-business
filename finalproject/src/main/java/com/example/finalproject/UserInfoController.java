package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserInfoController {

    @FXML
    private TableColumn<Employee, String> colusername;

    @FXML
    private TableColumn<Employee, String> coposte;

    @FXML
    private TableColumn<Employee, String> cophone;

    @FXML
    private TableColumn<Employee, String> coemail;

    @FXML
    private Button conge;

    @FXML
    private Button dashboard;

    @FXML
    private AnchorPane demandeconge;

    @FXML
    private DatePicker end;

    @FXML
    private AnchorPane information;

    @FXML
    private DatePicker start;

    @FXML
    private TableView<Employee> table;

    @FXML
    private TextField usernamef;

    private final ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colusername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        coposte.setCellValueFactory(cellData -> cellData.getValue().posteProperty());
        cophone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        coemail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        table.setItems(employeeData);

        showInformationPane();
    }

    public void setUserInfo(String username, String poste, String phone, String email) {
        employeeData.clear();
        employeeData.add(new Employee(username, poste, phone, email));
        table.setItems(employeeData);
    }

    @FXML
    private void showInformationPane() {
        information.setVisible(true);
        demandeconge.setVisible(false);
    }

    @FXML
    private void showDemandeCongePane() {
        information.setVisible(false);
        demandeconge.setVisible(true);
    }

    @FXML
    private void submitLeaveRequest() {
        if (start.getValue() == null || end.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select both start and end dates!");
            return;
        }

        String startDate = start.getValue().toString();
        String endDate = end.getValue().toString();
        String username = usernamef.getText();

        if (username.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username field cannot be empty!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO demande_conge (username, start_date, end_date) VALUES (?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, username);
                pst.setString(2, startDate);
                pst.setString(3, endDate);

                int rowsAffected = pst.executeUpdate();

                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Leave request submitted successfully!\nStart Date: " + startDate + "\nEnd Date: " + endDate);
                    clearFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit leave request.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while submitting the leave request.");
        }
    }

    private void clearFields() {
        start.setValue(null);
        end.setValue(null);
        usernamef.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
