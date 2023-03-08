package com.iaroslaveremeev.controllers;

import com.iaroslaveremeev.model.Card;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.repository.CardRepository;
import com.iaroslaveremeev.repository.CategoryRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class AddCardController {
    public TextField question;
    public TextField answer;
    public ComboBox<Category> categoryComboBox;

    public void initialize(){
        CategoryRepository categoryRepository = new CategoryRepository();
        int userId = Preferences.userRoot().node("userId").getInt("userId", 0);
        this.categoryComboBox
                .setItems(FXCollections.observableList(categoryRepository.getUserCategories(userId)));
    }
    public void addNewCard(ActionEvent actionEvent) {
        CardRepository cardRepository = new CardRepository();
        Card newCard = new Card(this.question.getText(), this.answer.getText(),
                this.categoryComboBox.getSelectionModel().getSelectedItem().getId());
        cardRepository.addCard(newCard);
        Stage stage = (Stage) this.question.getScene().getWindow();
        stage.close();
    }
}
