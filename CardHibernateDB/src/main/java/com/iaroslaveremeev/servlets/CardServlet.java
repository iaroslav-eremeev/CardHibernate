package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Card;
import com.iaroslaveremeev.repository.CardRepository;
import com.iaroslaveremeev.repository.CategoryRepository;
import com.iaroslaveremeev.repository.UserRepository;
import com.iaroslaveremeev.util.Unicode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@WebServlet("/cards")
public class CardServlet extends HttpServlet {

    // Add card to the category with certain id
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String question = req.getParameter("question");
        String answer = req.getParameter("answer");
        String categoryId = req.getParameter("categoryId");
        if(question != null && answer != null && categoryId != null) {
            try (CardRepository cardRepository = new CardRepository();
                 CategoryRepository categoryRepository = new CategoryRepository()) {
                // Check if category with such id exists
                if (categoryRepository.get(Integer.parseInt(categoryId)) != null) {
                    Card card = new Card(question, answer, Integer.parseInt(categoryId));
                    cardRepository.addCard(card);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(card)));

                } else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no category with such id!");
                }
            } catch (Exception e){
                resp.setStatus(400);
                resp.getWriter().println("Database loading failed. Check connection");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect card question, answer or category id input");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
       Unicode.setUnicode(req, resp);
       ObjectMapper objectMapper = new ObjectMapper();
       String id = req.getParameter("id");
       String categoryId = req.getParameter("categoryId");
       String userId = req.getParameter("userId");
       if (id != null){
           try (CardRepository cardRepository = new CardRepository()){
               Card card = cardRepository.get(Integer.parseInt(id));
               if (card != null){
                   resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(card)));
               }
               else {
                   resp.setStatus(400);
                   resp.getWriter().println("There is no card with such id!");
               }
           } catch (Exception e){
               resp.setStatus(400);
               resp.getWriter().println("Database loading failed. Check connection");
           }
       }
       else if (categoryId != null){
           try (CardRepository cardRepository = new CardRepository();
                CategoryRepository categoryRepository = new CategoryRepository()){
                if (categoryRepository.get(Integer.parseInt(categoryId)) != null){
                    ArrayList<Card> cards = cardRepository.getCardsByCatId(Integer.parseInt(categoryId));
                    resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(cards)));
                }
                else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no category with such id!");
                }
           } catch (Exception e){
               resp.setStatus(400);
               resp.getWriter().println("Database loading failed. Check connection");
           }
       }
       else if (userId != null){
           try (CardRepository cardRepository = new CardRepository();
                UserRepository userRepository = new UserRepository()){
               if (userRepository.get(Integer.parseInt(userId)) != null){
                   ArrayList<Card> cards = cardRepository.getCardsByUserId(Integer.parseInt(userId));
                   resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(cards)));
               }
               else {
                   resp.setStatus(400);
                   resp.getWriter().println("There is no user with such id!");
               }
           } catch (Exception e){
               resp.setStatus(400);
               resp.getWriter().println("Database loading failed. Check connection");
           }
       }
       else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input");
       }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String question = req.getParameter("question");
        String answer = req.getParameter("answer");
        String categoryId = req.getParameter("categoryId");
        if (id != null && question != null && answer != null && categoryId != null){
            try (CardRepository cardRepository = new CardRepository();
                 CategoryRepository categoryRepository = new CategoryRepository()){
                Card oldCard = cardRepository.get(Integer.parseInt(id));
                if (oldCard != null && categoryRepository.get(Integer.parseInt(categoryId)) != null){
                    Card newCard = new Card(oldCard.getId(), question, answer,
                            Integer.parseInt(categoryId), oldCard.getCreationDate());
                    cardRepository.update(newCard);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(newCard)));
                }
                else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no card or category with such id!");
                }
            }
            catch (Exception e){
                resp.setStatus(400);
                resp.getWriter().println("Database loading failed. Check connection");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect parameters input");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        if (id != null) {
            try (CardRepository cardRepository = new CardRepository()) {
                Card cardToDelete = cardRepository.get(Integer.parseInt(id));
                if (cardToDelete != null) {
                    cardRepository.delete(cardToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(cardToDelete)));
                } else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no card with such id!");
                }
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter().println("Database loading failed. Check connection");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input");
        }
    }
}
