package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.DAO.DAO;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Unicode;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    // Get all the categories for selected user by their id
    // Get category by its id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String userId = req.getParameter("userId");
        // Get category by its id
        if (id != null){
            Category category = (Category) DAO.getObjectById(Integer.parseInt(id), Category.class);
            if (category != null){
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(category)));
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("No category with such id found!");
            }
        }
        // Get categories by user id
        else if (userId != null){
            List categories = DAO.getObjectsByParam("userId", Integer.parseInt(userId), Category.class);
            resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(categories)));
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id or user id input");
        }
    }

    // Add category to user with certain id
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String name = req.getParameter("name");
        String userId = req.getParameter("userId");
        if(name != null && userId != null) {
            // Check if user with such id exists
            User user = (User) DAO.getObjectById(Integer.parseInt(userId), User.class);
            DAO.closeOpenedSession();
            if (user != null) {
                Category category = new Category(name, user);
                DAO.addObject(category);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(category)));
            } else {
                resp.setStatus(400);
                resp.getWriter().println("There is no user with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect category name or user id input");
        }
    }

    // Update category by its id
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        String name = req.getParameter("name");
        String userId = req.getParameter("userId");
        if(id != null & name != null && userId != null) {
            Category category = (Category) DAO.getObjectById(Integer.parseInt(id), Category.class);
            DAO.closeOpenedSession();
            if (category != null){
                User user = (User) DAO.getObjectById(Integer.parseInt(userId), User.class);
                DAO.closeOpenedSession();
                if (user != null){
                    DAO.updateObject(category);
                    resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(category)));
                }
                else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no user with such user id!");
                }
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("There is no category to update with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect category id, name or user id input");
        }
    }

    // Delete category by its id
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        if (id != null) {
            Category categoryToDelete = (Category) DAO.getObjectById(Integer.parseInt(id), Category.class);
            DAO.closeOpenedSession();
            if (categoryToDelete != null) {
                DAO.deleteObject(categoryToDelete);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(categoryToDelete)));
            } else {
                resp.setStatus(400);
                resp.getWriter().println("There is no category with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input");
        }
    }
}
