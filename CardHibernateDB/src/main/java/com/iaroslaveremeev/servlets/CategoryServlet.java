package com.iaroslaveremeev.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iaroslaveremeev.dto.ResponseResult;
import com.iaroslaveremeev.model.Category;
import com.iaroslaveremeev.repository.CategoryRepository;
import com.iaroslaveremeev.repository.UserRepository;
import com.iaroslaveremeev.util.Unicode;

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
        if (id != null || userId != null){
            try (CategoryRepository categoryRepository = new CategoryRepository()){
                // Get category by its id
                if (id != null){
                    Category category = categoryRepository.get(Integer.parseInt(id));
                    if (category != null){
                        resp.getWriter()
                                .println(objectMapper.writeValueAsString(new ResponseResult<>(category)));
                    }
                    else {
                        resp.setStatus(400);
                        resp.getWriter().println("No category with such id found!");
                    }
                }
                // get all the categories for selected user by their id
                else {
                    List<Category> categories = categoryRepository
                            .getCategoriesByUserId(Integer.parseInt(userId));
                    if (categories.size() > 0){
                        resp.getWriter()
                                .println(objectMapper.writeValueAsString(new ResponseResult<>(categories)));
                    }
                    else {
                        resp.setStatus(400);
                        resp.getWriter().println("The selected user has no categories!");
                    }
                }
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter().println("Database loading failed. Check connection");
            }
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
            try (CategoryRepository categoryRepository = new CategoryRepository();
                 UserRepository userRepository = new UserRepository()) {
                // Check if user with such id exists
                if (userRepository.get(Integer.parseInt(userId)) != null) {
                    Category category = new Category(name, Integer.parseInt(userId));
                    categoryRepository.addCategory(category);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(category)));

                } else {
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
            try (CategoryRepository categoryRepository = new CategoryRepository();
                    UserRepository userRepository = new UserRepository()){
                Category oldCategory = categoryRepository.get(Integer.parseInt(id));
                if (oldCategory != null){
                    if (userRepository.get(Integer.parseInt(userId)) != null){
                        Category newCategory =
                                new Category(Integer.parseInt(id), name, Integer.parseInt(userId));
                        categoryRepository.update(newCategory);
                        resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(newCategory)));
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
            } catch (Exception e) {
                resp.setStatus(400);
                resp.getWriter().println("Database loading failed. Check connection");
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
            try (CategoryRepository categoryRepository = new CategoryRepository()) {
                Category categoryToDelete = categoryRepository.get(Integer.parseInt(id));
                if (categoryToDelete != null) {
                    categoryRepository.delete(categoryToDelete);
                    resp.getWriter()
                            .println(objectMapper.writeValueAsString(new ResponseResult<>(categoryToDelete)));
                } else {
                    resp.setStatus(400);
                    resp.getWriter().println("There is no category with such id!");
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
