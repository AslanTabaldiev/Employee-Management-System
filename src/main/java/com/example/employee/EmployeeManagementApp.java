package com.example.employee;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class EmployeeManagementApp extends Application {
    private ObservableList<Employee> employeeList = FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        TableView<Employee> tableView = new TableView<>();
        tableView.setItems(employeeList);

        TableColumn<Employee, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));

        TableColumn<Employee, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));

        TableColumn<Employee, String> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.format("%.2f", data.getValue().calculateSalary())
        ));

        tableView.getColumns().addAll(nameColumn, typeColumn, salaryColumn);

        // Input fields
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Full-time", "Part-time", "Contractor");
        typeComboBox.setPromptText("Type");

        TextField salaryField = new TextField();
        salaryField.setPromptText("Annual Salary / Hourly Rate");
        salaryField.setDisable(true);

        TextField hoursField = new TextField();
        hoursField.setPromptText("Hours Worked / Max Hours");
        hoursField.setDisable(true);

        typeComboBox.setOnAction(e -> {
            String type = typeComboBox.getValue();
            if ("Full-time".equals(type)) {
                salaryField.setDisable(false);
                hoursField.setDisable(true);
            } else if ("Part-time".equals(type) || "Contractor".equals(type)) {
                salaryField.setDisable(false);
                hoursField.setDisable(false);
            }
        });

        Button addButton = new Button("Add Employee");
        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String type = typeComboBox.getValue();
            try {
                if (name.isEmpty() || type == null) {
                    throw new IllegalArgumentException("Name and type must be provided.");
                }

                if ("Full-time".equals(type)) {
                    double annualSalary = Double.parseDouble(salaryField.getText());
                    employeeList.add(new FullTimeEmployee(name, annualSalary));
                } else if ("Part-time".equals(type)) {
                    double hourlyRate = Double.parseDouble(salaryField.getText());
                    int hoursWorked = Integer.parseInt(hoursField.getText());
                    employeeList.add(new PartTimeEmployee(name, hourlyRate, hoursWorked));
                } else if ("Contractor".equals(type)) {
                    double hourlyRate = Double.parseDouble(salaryField.getText());
                    int maxHours = Integer.parseInt(hoursField.getText());
                    employeeList.add(new ContractorEmployee(name, hourlyRate, maxHours));
                }

                nameField.clear();
                typeComboBox.getSelectionModel().clearSelection();
                salaryField.clear();
                salaryField.setDisable(true);
                hoursField.clear();
                hoursField.setDisable(true);

            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Salary and hours must be numeric values.");
            } catch (IllegalArgumentException ex) {
                showAlert("Missing Information", ex.getMessage());
            }
        });

        Button removeButton = new Button("Remove Selected");
        removeButton.setOnAction(e -> {
            Employee selected = tableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                employeeList.remove(selected);
            } else {
                showAlert("No Selection", "Please select an employee to remove.");
            }
        });

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        inputGrid.add(new Label("Name:"), 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(new Label("Type:"), 0, 1);
        inputGrid.add(typeComboBox, 1, 1);
        inputGrid.add(new Label("Salary / Rate:"), 0, 2);
        inputGrid.add(salaryField, 1, 2);
        inputGrid.add(new Label("Hours:"), 0, 3);
        inputGrid.add(hoursField, 1, 3);
        inputGrid.add(addButton, 0, 4);
        inputGrid.add(removeButton, 1, 4);

        VBox root = new VBox(10, tableView, inputGrid);
        Scene scene = new Scene(root, 400, 500);

        primaryStage.setTitle("Employee Management");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
