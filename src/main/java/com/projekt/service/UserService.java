package com.projekt.service;

import com.projekt.model.User;

import java.util.List;

public interface UserService {
    void save(User user);

    User findByUsername(String username);

    User findById(int id);

    List<User> findAllStudents();

    List<User> findAllUsers();

    List<User> findAllOnCourse(int course_id);

    void deleteById(int userId);
}
