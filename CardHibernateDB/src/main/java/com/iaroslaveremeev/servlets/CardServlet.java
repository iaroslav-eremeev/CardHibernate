package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.DAO.DAO;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Card;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Unicode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
            // Check if category with such id exists
            Category category = (Category) DAO.getObjectById(Integer.parseInt(categoryId), Category.class);
            DAO.closeOpenedSession();
            if (category != null) {
                Card card = new Card(question, answer, category);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(card)));

            } else {
                resp.setStatus(400);
                resp.getWriter().println("There is no category with such id!");
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
       if (id != null){
           Card card = (Card) DAO.getObjectById(Integer.parseInt(id), Card.class);
           DAO.closeOpenedSession();
           if (card != null){
               resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(card)));
           }
           else {
               resp.setStatus(400);
               resp.getWriter().println("There is no card with such id!");
           }
       }
       else if (categoryId != null){
            Category category = (Category) DAO.getObjectById(Integer.parseInt(categoryId), Category.class);
            DAO.closeOpenedSession();
            if (category != null){
                List cards = DAO.getObjectsByParam("categoryId", categoryId, Card.class);
                DAO.closeOpenedSession();
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(cards)));
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("There is no category with such id!");
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
            Card card = (Card) DAO.getObjectById(Integer.parseInt(id), Card.class);
            DAO.closeOpenedSession();
            if (card != null){
                DAO.updateObject(card);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(card)));
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("There is no card or category with such id!");
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
            Card cardToDelete = (Card) DAO.getObjectById(Integer.parseInt(id), Card.class);
            DAO.closeOpenedSession();
            if (cardToDelete != null) {
                DAO.deleteObject(cardToDelete);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(cardToDelete)));
            } else {
                resp.setStatus(400);
                resp.getWriter().println("There is no card with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input");
        }
    }
}
