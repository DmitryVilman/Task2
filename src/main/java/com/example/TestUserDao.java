package com.example;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.entity.User;
import java.time.LocalDateTime;

public class TestUserDao {
    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        User user1 = new User("John", "john@example.com", 30, LocalDateTime.now());
        User user2 = new User("Jane", "john@example.com", 25, LocalDateTime.now());
        userDao.createUser(user1);
        userDao.createUser(user2); // ТУТ должна быть ошибка!Второй пользователь не сохранён.

        // Тест создания
        User user = new User("John Doe", "john@example.com", 30, LocalDateTime.now());
        userDao.createUser(user);

        // Тест чтения
        User foundUser = userDao.getUserById(user.getId());
        System.out.println("Found user: " + foundUser);

        // Тест обновления
        user.setName("Jane Doe");
        userDao.updateUser(user);
        System.out.println("Updated user: " + userDao.getUserById(user.getId()));

        // Тест получения всех пользователей
        System.out.println("All users: " + userDao.getAllUsers());

        // Тест удаления
        userDao.deleteUser(user.getId());
        System.out.println("User after deletion: " + userDao.getUserById(user.getId()));
    }
}