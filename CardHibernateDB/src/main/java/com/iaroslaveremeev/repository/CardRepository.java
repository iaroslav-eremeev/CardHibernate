package com.iaroslaveremeev.repository;

import com.iaroslaveremeev.model.Card;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.util.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardRepository implements AutoCloseable {

    private Connection conn;

    public CardRepository() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.conn = DriverManager.getConnection(Constants.DB_URL,
                    Constants.USERNAME, Constants.PASSWORD);
        } catch (ClassNotFoundException | SQLException ignored) {}
    }

    // Add card to a category
    public boolean addCard(Card card){
        String sql = "insert into cards(question, answer, categoryId, creationDate) values (?,?,?,?)";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, card.getQuestion());
            preparedStatement.setString(2, card.getAnswer());
            preparedStatement.setInt(3, card.getCategoryId());
            preparedStatement.setTimestamp(4, new Timestamp(card.getCreationDate().getTime()));
            int row = preparedStatement.executeUpdate();
            if (row <= 0)
                return false;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next())
                    card.setId(generatedKeys.getInt(1));
            }
            return true;
        } catch (SQLException ignored) {}
        return false;
    }

    // Get card by its id
    public Card get(int id) {
        String sql = "select * from cards where cards.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next())
                return null;
            Card card = newCardForList(resultSet);
            return card;
        } catch (SQLException ignored) {
        }
        return null;
    }

    // Create new card from SQL response ResultSet
    private Card newCardForList(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setId(resultSet.getInt(1));
        card.setQuestion(resultSet.getString(2));
        card.setAnswer(resultSet.getString(3));
        card.setCategoryId(resultSet.getInt(4));
        card.setCreationDate(resultSet.getTimestamp(5));
        return card;
    }

    // Get ArrayList of cards by User of Category id
    private ArrayList<Card> getCards(int id, String sql) {
        ArrayList<Card> cards = new ArrayList<>();
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                cards.add(newCardForList(resultSet));
            }
        } catch (SQLException ignored) {}
        return cards;
    }

    // Get ArrayList of cards by Category id
    public ArrayList<Card> getCardsByCatId(int categoryId) {
        String sql = "select * from cards where cards.categoryId=?";
        return getCards(categoryId, sql);
    }

    // Get ArrayList of cards by User id
    public ArrayList<Card> getCardsByUserId(int userId) {
        String sql = "select * from cards\n" +
                "    join categories c on cards.categoryId = c.id\n" +
                "    join users u on u.id = c.userId\n" +
                "    where userId=?";
        return getCards(userId, sql);
    }

    // Update card by its id
    public boolean update(Card card) throws SQLException {
        String sql = "update cards set cards.question=?, cards.answer=?, cards.categoryId=?, " +
                "cards.creationDate=? where cards.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setString(1, card.getQuestion());
            preparedStatement.setString(2, card.getAnswer());
            preparedStatement.setInt(3, card.getCategoryId());
            preparedStatement.setTimestamp(4, (Timestamp) card.getCreationDate());
            preparedStatement.setInt(5, card.getId());
            return preparedStatement.executeUpdate() > 0;
        } catch (SQLException ignored) {}
        return false;
    }

    // Delete card by its id
    public boolean delete(Card card) {
        String sql = "delete from cards where cards.id=?";
        try (PreparedStatement preparedStatement = this.conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, card.getId());
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
