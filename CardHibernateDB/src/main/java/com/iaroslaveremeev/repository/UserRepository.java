package com.iaroslaveremeev.repository;

import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements AutoCloseable {
    private Connection conn;
    public UserRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL,
                    Constants.USERNAME, Constants.PASSWORD);
        } catch (SQLException | ClassNotFoundException ignored) {}
    }

    // Extracted method to create user from info obtained from SQL database response
    private User getUserFromSQL(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setLogin(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        user.setName(resultSet.getString(4));
        user.setRegDate(resultSet.getTimestamp(5));
        return user;
    }

    // Get user by their id
    public User get(int id) {
        String sql = "select * from users where users.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            return getUserFromSQL(resultSet);
        } catch (SQLException ignored) {}
        return null;
    }

    // Get the list of all existing users from the database
    public List<User> getAll()  {
        String sql = "select * from users";
        ArrayList<User> users = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                users.add(getUserFromSQL(resultSet));
            }
        } catch (SQLException ignored) {}
        return users;
    }

    // Add user to database
    public boolean add(User user){
        String sql = "insert into users(login, password, name, regDate) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            // Get user registration data using timestamp
            preparedStatement.setTimestamp(4, new Timestamp(user.getRegDate().getTime()));
            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    user.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    // Delete user from database by their id
    public boolean delete(User user) {
        String sql = "delete from users where users.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, user.getId());
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
