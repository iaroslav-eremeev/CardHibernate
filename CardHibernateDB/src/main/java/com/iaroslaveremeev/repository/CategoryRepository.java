package com.iaroslaveremeev.repository;

import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository implements AutoCloseable {

    private Connection conn;
    public CategoryRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL,
                    Constants.USERNAME, Constants.PASSWORD);
        } catch (ClassNotFoundException | SQLException ignored) {}
    }

    // Add user category
    public boolean addCategory(Category category){
        String sql = "insert into categories(name, userId) values (?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getUserId());
            int row = preparedStatement.executeUpdate();
            if (row <= 0) return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    category.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    // Create new category from SQL response ResultSet
    private Category newCategory(ResultSet resultSet) throws SQLException {
        Category category = new Category();
        category.setId(resultSet.getInt(1));
        category.setName(resultSet.getString(2));
        category.setUserId(resultSet.getInt(3));
        return category;
    }

    // Get category by its id
    public Category get(int id) {
        String sql = "select * from categories where categories.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            return newCategory(resultSet);
        } catch (SQLException ignored) {}
        return null;
    }

    // Get list of categories by user id
    public List<Category> getCategoriesByUserId(int userId) throws SQLException {
        String sql = "select * from categories where categories.userId=?";
        ArrayList<Category> categories = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                categories.add(newCategory(resultSet));
            }
        } catch (SQLException ignored) {}
        return categories;
    }

    // Update category by its id
    public boolean update(Category category) {
        String sql = "update categories set categories.name=?, categories.userId=? where categories.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getUserId());
            preparedStatement.setInt(3, category.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    // Delete category by its id
    public boolean delete(Category category) {
        String sql = "delete from categories where categories.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, category.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    // Close connection
    public void close() {
        try {
            if (this.conn != null)
                this.conn.close();
        } catch (Exception ignored) {}
    }
}
