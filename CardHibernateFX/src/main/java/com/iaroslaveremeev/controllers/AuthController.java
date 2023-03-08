package com.iaroslaveremeev.controllers;

import com.iaroslaveremeev.Program;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.repository.UserRepository;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

import java.io.IOException;

public class AuthController {
    public TextField authLogin;
    public PasswordField authPassword;
    public Preferences prefs;

    public void checkAuthorization(ActionEvent actionEvent) {
        try {
            UserRepository userRepository = new UserRepository();
            String login = this.authLogin.getText();
            String password = this.authPassword.getText();
            User user = userRepository.authorize(login, password);
            if (user != null) {
                prefs = Preferences.userRoot().node("userId");
                prefs.putInt("userId", user.getId());
                Stage mainStage = Program.openWindow("/mainForm.fxml", null);
                assert mainStage != null;
                mainStage.show();
                Stage authStage = (Stage) authLogin.getScene().getWindow();
                authStage.close();
            }
        } catch (IOException ignored) {}
    }

    public void openRegForm(ActionEvent actionEvent) throws IOException {
        Stage authStage = Program.openWindow("/registration.fxml", null);
        assert authStage != null;
        authStage.showAndWait();
    }

}
