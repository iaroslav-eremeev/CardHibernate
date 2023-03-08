package com.iaroslaveremeev.controllers;

import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegController {

    public TextField regLogin;
    public TextField regName;
    public PasswordField regPassword;

    public void signUp(ActionEvent actionEvent) {
        try {
            UserRepository userRepository = new UserRepository();
            String login = this.regLogin.getText();
            String password = this.regPassword.getText();
            String name = this.regName.getText();
            User user = new User(login, password, name);
            userRepository.register(user);
            Stage stage = (Stage) this.regLogin.getScene().getWindow();
            stage.close();
        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Server error. Check connection!");
            alert.show();
        }

    }
}
