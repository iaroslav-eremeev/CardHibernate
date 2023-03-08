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

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {

    // Post method to register a new user (add new record to the users' database)
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Unicode.setUnicode(req, resp);
        ObjectMapper objectMapper = new ObjectMapper();
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        if(login != null && password != null && name != null) {
            try(UserRepository userRepository = new UserRepository()) {
                User user = new User(login, password, name);
                if (userRepository.add(user)) {
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(user)));
                }
                else {
                    resp.getWriter().println("Registration failed. Check if you used parameters correctly");
                    resp.setStatus(400);
                }
            } catch (Exception e) {
                resp.getWriter().println("Database loading failed. Check connection");
                resp.setStatus(400);
            }
        }
    }
}
