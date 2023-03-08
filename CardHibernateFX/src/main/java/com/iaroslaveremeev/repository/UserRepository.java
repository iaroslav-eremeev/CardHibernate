package com.iaroslaveremeev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Constants;
import com.iaroslaveremeev.util.DataFromURL;
import javafx.scene.control.Alert;

import javax.security.auth.login.FailedLoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserRepository {

    public UserRepository() {
    }

    public User register(User user) {
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/registration?" +
                "&login=" + URLEncoder.encode(user.getLogin(), StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(user.getPassword(), StandardCharsets.UTF_8) +
                "&name=" + URLEncoder.encode(user.getName(), StandardCharsets.UTF_8), "POST")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<User> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registration failed!");
            alert.show();
            return null;
        }
    }

    public User authorize(String login, String password){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/users?" +
                "&login=" + URLEncoder.encode(login, StandardCharsets.UTF_8) +
                "&password=" + URLEncoder.encode(password, StandardCharsets.UTF_8), "POST")) {
            ObjectMapper mapper = new ObjectMapper();
            if (inputStream == null){
                throw new FailedLoginException();
            }
            ResponseResult<User> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException | IllegalArgumentException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Server error. Check connection!");
            alert.show();
            return null;
        } catch (FailedLoginException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong login or password!");
            alert.show();
            return null;
        }
    }

    public User get(int userId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/users?" +
                "&id=" + userId, "GET")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<User> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData(); // check if retrieving is successful
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Server error. Check connection!");
            alert.show();
            return null;
        }
    }

}
