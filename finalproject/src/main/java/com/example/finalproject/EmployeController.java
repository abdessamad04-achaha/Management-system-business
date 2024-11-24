package com.example.finalproject;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EmployeController implements Initializable {
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement pst = null;
    @FXML
    private Button button_back;

    @FXML
    private Button button_login;

    @FXML
    private Button button_sign;

    @FXML
    private TextField email_sign;

    @FXML
    private Hyperlink link_signup;

    @FXML
    private AnchorPane pane_login;

    @FXML
    private AnchorPane pane_signup;

    @FXML
    private PasswordField password_login;

    @FXML
    private PasswordField password_sign;

    @FXML
    private TextField phone_sign;

    @FXML
    private TextField poste_sign;

    @FXML
    private TextField username_login;

    @FXML
    private TextField username_sign;

    public void LoginPaneShow() {
        pane_login.setVisible(true);
        pane_signup.setVisible(false);
    }

    public void SignupPaneShow() {
        pane_signup.setVisible(true);
        pane_login.setVisible(false);
    }

    public void regBtn() throws SQLException {
        // Check if any field is empty
        if (username_sign.getText().isEmpty() || password_sign.getText().isEmpty() || phone_sign.getText().isEmpty() || poste_sign.getText().isEmpty() || email_sign.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Message");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else {
            String sql = "INSERT INTO employe(username, poste, phone, password, email) VALUES(?, ?, ?, ?, ?)";
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pst = null;

            try {
                pst = conn.prepareStatement(sql);
                pst.setString(1, username_sign.getText());
                pst.setString(2, poste_sign.getText());
                pst.setString(3, phone_sign.getText());
                pst.setString(4, password_sign.getText());
                pst.setString(5, email_sign.getText());
                pst.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Registered!");
                alert.showAndWait();

                username_sign.clear();
                password_sign.clear();
                phone_sign.clear();
                poste_sign.clear();
                email_sign.clear();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (pst != null) {
                    try {
                        pst.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    @FXML
    private void Login(javafx.event.ActionEvent actionEvent) throws Exception {
        conn = DatabaseConnection.getConnection();
        String query = "SELECT * FROM employe WHERE username=? AND password=?";

        try {
            pst = conn.prepareStatement(query);
            pst.setString(1, username_login.getText());
            pst.setString(2, password_login.getText());
            rs = pst.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                String poste = rs.getString("poste");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                // Open the new interface with user data
                showUserInfo(username, poste, phone, email);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Failed");
                alert.setHeaderText(null);
                alert.setContentText("Invalid username or password");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pst != null) pst.close();
            if (conn != null) conn.close();
        }
    }

    private void showUserInfo(String username, String poste, String phone, String email) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user_info.fxml"));
            AnchorPane root = loader.load();

            UserInfoController controller = loader.getController();
            controller.setUserInfo(username, poste, phone, email);

            // Show the user info interface
            Stage stage = new Stage();
            stage.setTitle("User Information");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


}
