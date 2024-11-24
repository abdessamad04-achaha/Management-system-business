package com.example.finalproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Employee {
    private final StringProperty username;
    private final StringProperty poste;
    private final StringProperty phone;
    private final StringProperty email;

    public Employee(String username, String poste, String phone, String email) {
        this.username = new SimpleStringProperty(username);
        this.poste = new SimpleStringProperty(poste);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty posteProperty() {
        return poste;
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getUsername() {
        return username.get();
    }

    public String getPoste() {
        return poste.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }
}
