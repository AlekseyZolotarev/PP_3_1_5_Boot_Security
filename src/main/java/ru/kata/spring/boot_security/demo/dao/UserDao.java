package ru.kata.spring.boot_security.demo.dao;


import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers(String sql);

    User getUser(int id);

    void saveUser(User user);

    void editUser(int id, User user);

    void deleteUser(int id);
}

