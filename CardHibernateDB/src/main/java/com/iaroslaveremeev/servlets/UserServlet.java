package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.User;
import com.iaroslaveremeev.repository.UserRepository;
import com.iaroslaveremeev.util.Unicode;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {

    // Get user by their id
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String id = req.getParameter("id");
        try (UserRepository userRepository = new UserRepository()){
            if (id != null){
                User user = userRepository.get(Integer.parseInt(id));
                if (user != null) {
                    resp.getWriter().println(objectMapper.writeValueAsString(new ResponseResult<>(user)));
                }
                else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no user with such id!");
                }
            } else {
                resp.setStatus(400);
                resp.getWriter().println("Incorrect id input!");
            }
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
            try (UserRepository userRepository = new UserRepository()) {
                for (int i = 0; i < userRepository.getAll().size(); i++) {
                    User user = userRepository.getAll().get(i);
                    String userPassword = user.getPassword();
                    if (login.equals(user.getLogin()) && password.equals(userPassword)){
                        success = true;
                        resp.getWriter()
                                .println(objectMapper.writeValueAsString(new ResponseResult<>(user)));
                    }
                }
                if (!success) {
                    resp.setStatus(400);
                    resp.getWriter().println("Authorization failure. Wrong login or password");
                }
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
        try (UserRepository userRepository = new UserRepository()) {
            if(id != null){
                User userToDelete = userRepository.get(Integer.parseInt(id));
                if (userToDelete != null){
                    userRepository.delete(userToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(userToDelete)));
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
}
