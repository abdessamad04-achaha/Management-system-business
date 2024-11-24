package com.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminController {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "12345";

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button buttonadmin;

    // FXML fields for admin interface
    @FXML
    private Button buttoninfo;

    @FXML
    private Button buttondemandeconge;

    @FXML
    private TableView<Employee> table_employe;

    @FXML
    private TableColumn<Employee, String> usernameemp;

    @FXML
    private TableColumn<Employee, String> posteemp;

    @FXML
    private TableColumn<Employee, String> phone_emp;

    @FXML
    private TableColumn<Employee, String> email_emp;

    @FXML
    private TableView<LeaveRequest> tableconge;

    @FXML
    private TableColumn<LeaveRequest, String> usernameconge;

    @FXML
    private TableColumn<LeaveRequest, String> startconge;

    @FXML
    private TableColumn<LeaveRequest, String> endconge;
    @FXML
    private AnchorPane menu;

    @FXML
    private AnchorPane pane_info;

    @FXML
    private AnchorPane pane_conge;

    @FXML
    private AnchorPane pane_login;

    @FXML
    private AnchorPane admininterface;

    // Fields for editing employee
    @FXML
    private TextField iem;

    @FXML
    private TextField ipho;

    @FXML
    private TextField ipo;

    @FXML
    private TextField iuse;

    private ObservableList<Employee> employees = FXCollections.observableArrayList();
    private ObservableList<LeaveRequest> leaveRequests = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize Table Columns
        usernameemp.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        posteemp.setCellValueFactory(cellData -> cellData.getValue().posteProperty());
        phone_emp.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        email_emp.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        usernameconge.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        startconge.setCellValueFactory(cellData -> cellData.getValue().startDateProperty());
        endconge.setCellValueFactory(cellData -> cellData.getValue().endDateProperty());


        pane_login.setVisible(true);
        admininterface.setVisible(false);
        menu.setVisible(false);
        buttoninfo.setVisible(false);
        buttondemandeconge.setVisible(false);

        // Set button actions
        buttonadmin.setOnAction(event -> handleLogin());
        buttoninfo.setOnAction(event -> {
            try {
                showEmployeeTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        buttondemandeconge.setOnAction(event -> {
            try {
                showLeaveRequestTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    @FXML
    private void handleLogin() {
        String enteredUsername = username.getText();
        String enteredPassword = password.getText();

        if (ADMIN_USERNAME.equals(enteredUsername) && ADMIN_PASSWORD.equals(enteredPassword)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Hello Admin!");


            buttoninfo.setDisable(false);
            buttondemandeconge.setDisable(false);

            pane_login.setVisible(false);
            menu.setVisible(true);
            admininterface.setVisible(true);
            buttoninfo.setVisible(true);
            buttondemandeconge.setVisible(true);
            pane_info.setVisible(false);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Username or Password.");
        }
    }


    @FXML
    private void showEmployeeTable() throws SQLException {

        fetchEmployeeData();


        pane_info.setVisible(true);
        pane_conge.setVisible(false);
    }


    @FXML
    private void showLeaveRequestTable() throws SQLException {

        fetchLeaveRequestData();

        pane_info.setVisible(false);
        pane_conge.setVisible(true);
    }

    private void fetchEmployeeData() throws SQLException {
        employees.clear();
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT username, poste, phone, email FROM employe";

        try (PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                employees.add(new Employee(
                        rs.getString("username"),
                        rs.getString("poste"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
            table_employe.setItems(employees);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch employee data.");
        }
    }

    private void fetchLeaveRequestData() throws SQLException {
        leaveRequests.clear();
        Connection conn = DatabaseConnection.getConnection();
        String query = "SELECT username, start_date, end_date FROM demande_conge";

        try (PreparedStatement pst = conn.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                leaveRequests.add(new LeaveRequest(
                        rs.getString("username"),
                        rs.getString("start_date"),
                        rs.getString("end_date")
                ));
            }
            tableconge.setItems(leaveRequests);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to fetch leave request data.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void editEmployee() {
        String newUsername = iuse.getText();
        String newPoste = ipo.getText();
        String newPhone = ipho.getText();
        String newEmail = iem.getText();

        if (newUsername.isEmpty() || newPoste.isEmpty() || newPhone.isEmpty() || newEmail.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Invalid Input", "All fields must be filled.");
            return;
        }

        Employee selectedEmployee = table_employe.getSelectionModel().getSelectedItem();
        if (selectedEmployee == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to edit.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "UPDATE employe SET username = ?, poste = ?, phone = ?, email = ? WHERE username = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setString(1, newUsername);
                pst.setString(2, newPoste);
                pst.setString(3, newPhone);
                pst.setString(4, newEmail);
                pst.setString(5, selectedEmployee.getUsername()); // Original username for identification

                int rowsUpdated = pst.executeUpdate();
                if (rowsUpdated > 0) {
                    fetchEmployeeData();
                    showAlert(Alert.AlertType.INFORMATION, "Employee Updated", "Employee details updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update employee details.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update employee.");
        }
    }

    @FXML
    private void deleteEmployee() {
        Employee selectedEmployee = table_employe.getSelectionModel().getSelectedItem();
        if (selectedEmployee != null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "DELETE FROM employe WHERE username = ?";
                try (PreparedStatement pst = conn.prepareStatement(query)) {
                    pst.setString(1, selectedEmployee.getUsername());
                    pst.executeUpdate();
                    fetchEmployeeData(); // Refresh employee data
                    showAlert(Alert.AlertType.INFORMATION, "Employee Deleted", "Employee deleted successfully.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete employee.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an employee to delete.");
        }
    }
}
