package com.example.finalproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LeaveRequest {
    private StringProperty username;
    private StringProperty startDate;
    private StringProperty endDate;

    public LeaveRequest(String username, String startDate, String endDate) {
        this.username = new SimpleStringProperty(username);
        this.startDate = new SimpleStringProperty(startDate);
        this.endDate = new SimpleStringProperty(endDate);
    }

    public StringProperty usernameProperty() { return username; }
    public StringProperty startDateProperty() { return startDate; }
    public StringProperty endDateProperty() { return endDate; }
}
