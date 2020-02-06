package com.projekt.service;


import com.projekt.dao.UserDao;
import com.projekt.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
   private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void save(User user) {


        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDao.save(user);
    }



    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public List<User> findAllStudents() {
        return userDao.findAllStudents();
    }

    @Override
    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }

    public List<User> findAllOnCourse(int course_id){return userDao.findAllOnCourse(course_id);}

    @Override
    public void deleteById(int userId) {
        userDao.deleteById(userId);
    }
}