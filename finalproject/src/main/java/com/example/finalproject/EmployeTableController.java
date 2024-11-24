package com.example.finalproject;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EmployeTableController {

    @FXML
    private TableView<Employee> employeTable;

    @FXML
    private TableColumn<Employee, String> columnUsername;

    @FXML
    private TableColumn<Employee, String> columnEmail;

    @FXML
    private TableColumn<Employee, String> columnPhone;

    @FXML
    private TableColumn<Employee, String> columnPoste;

    public void initializeTable(String username) {
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnPoste.setCellValueFactory(new PropertyValueFactory<>("poste"));

        try {
            Connection conn = DatabaseConnection.getConnection();
            String query = "SELECT * FROM employe WHERE username=?";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Employee employee = new Employee(
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("poste")
                );
                employeTable.getItems().add(employee);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
