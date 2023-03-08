package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.DAO.DAO;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.util.Unicode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    // Get user by their id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        if (id != null){
            User user = (User) DAO.getObjectById(Integer.parseInt(id), User.class);
            DAO.closeOpenedSession();
            if (user != null) {
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(user)));
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("There is no user with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input!");
        }
    }

    // User authorization method
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        boolean success = false;
        if(login != null && password != null) {
            List users = DAO.getAllObjects(User.class);
            DAO.closeOpenedSession();
            for (int i = 0; i < users.size(); i++) {
                User user = (User) users.get(i);
                String userPassword = user.getPassword();
                if (login.equals(user.getLogin()) && password.equals(userPassword)){
                    success = true;
                    resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(user)));
                }
            }
            if (!success) {
                resp.setStatus(400);
                resp.getWriter().println("Authorization failure. Wrong login or password");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect login or password input");
        }
    }

    // Delete a user by their id
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        if(id != null){
            User userToDelete = (User) DAO.getObjectById(Integer.parseInt(id), User.class);
            DAO.closeOpenedSession();
            if (userToDelete != null){
                DAO.deleteObject(userToDelete);
                resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(userToDelete)));
            }
            else {
                resp.setStatus(400);
                resp.getWriter().println("There is no user with such id!");
            }
        }
        else {
            resp.setStatus(400);
            resp.getWriter().println("Incorrect id input!");
        }
    }
}
