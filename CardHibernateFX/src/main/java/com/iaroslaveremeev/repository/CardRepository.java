package com.iaroslaveremeev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Card;
import com.iaroslaveremeev.util.Constants;
import com.iaroslaveremeev.util.DataFromURL;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CardRepository {

    public CardRepository() {
    }

    public List<Card> getCategoryCards(int categoryId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/cards?" +
                "&categoryId=" + categoryId, "GET")) {
            ObjectMapper mapper = new ObjectMapper();
            // If category has no cards yet we should return empty ArrayList to avoid IllegalArgumentException
            if (inputStream == null) return new ArrayList<>();
            ResponseResult<List<Card>> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Category's cards not uploaded!");
            alert.show();
            return null;
        }
    }

    public Card updateCard(Card updatedCard){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/cards?" +
                "&id=" + updatedCard.getId() +
                "&question=" + URLEncoder.encode(updatedCard.getQuestion(), StandardCharsets.UTF_8) +
                "&answer=" + URLEncoder.encode(updatedCard.getAnswer(), StandardCharsets.UTF_8) +
                "&categoryId=" + updatedCard.getCategoryId(), "PUT")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<Card> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Card not updated!");
            alert.show();
            return null;
        }
    }

    public Card deleteCard(int cardId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/cards?" +
                "&id=" + cardId, "DELETE")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<Card> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Card not deleted! Check connection!");
            alert.show();
            return null;
        }
    }

    public Card addCard(Card newCard){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/cards?" +
                "&question=" + URLEncoder.encode(newCard.getQuestion(), StandardCharsets.UTF_8) +
                "&answer=" + URLEncoder.encode(newCard.getAnswer(), StandardCharsets.UTF_8) +
                "&categoryId=" + newCard.getCategoryId(), "POST")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<Card> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Card not added! Check connection!");
            alert.show();
            return null;
        }
    }
}
