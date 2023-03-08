package com.iaroslaveremeev.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Constants;
import com.iaroslaveremeev.util.DataFromURL;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public CategoryRepository() {
    }

    public List<Category> getUserCategories(int userId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/categories?" +
                "&userId=" + userId, "GET")) {
            ObjectMapper mapper = new ObjectMapper();
            // If user has no categories yet we should return empty ArrayList to avoid IllegalArgumentException
            if (inputStream == null) return new ArrayList<>();
            ResponseResult<List<Category>> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User categories not uploaded!");
            alert.show();
            return null;
        }
    }

    public Category getCategoryById(int catId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/categories?" +
                "&id=" + catId, "GET")) {
            ObjectMapper mapper = new ObjectMapper();
            // If user has no categories yet we should return empty ArrayList to avoid IllegalArgumentException
            ResponseResult<Category> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Category not uploaded!");
            alert.show();
            return null;
        }
    }

    public Category addCategory(String name, int userId){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/categories?" +
                "&name=" + name + "&userId=" + userId, "POST")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<Category> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User category not added!");
            alert.show();
            return null;
        }
    }

    public Category deleteCategory(int id){
        try (InputStream inputStream = DataFromURL.getData(Constants.SERVER_URL + "/categories?" +
                "&id=" + id, "DELETE")) {
            ObjectMapper mapper = new ObjectMapper();
            ResponseResult<Category> result = mapper.readValue(inputStream, new TypeReference<>() {});
            return result.getData();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "User category not deleted!");
            alert.show();
            return null;
        }
    }




}
