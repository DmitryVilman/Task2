package com.example;

import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.entity.User;
import com.example.util.HibernateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        UserDao userDao = new UserDaoImpl();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== User Service Menu ===");
            System.out.println("1. Create User");
            System.out.println("2. Get User by ID");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("5. List All Users");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                logger.error("Invalid input: {}", e.getMessage());
                System.out.println("Please enter a valid number.");
                continue;
            }

            if (choice == 0) {
                logger.info("Exiting application");
                break;
            }

            switch (choice) {

                case 1:
                    logger.info("User selected: Create User");
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age;
                    try {
                        age = Integer.parseInt(scanner.nextLine());
                        if (age <= 0 || age > 150) {
                            logger.warn("Invalid age: {}", age);
                            System.out.println("Age must be between 1 and 150.");
                            break;
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Invalid age input: {}", e.getMessage());
                        System.out.println("Please enter a valid age.");
                        break;
                    }
                    // Валидация email (простая проверка на наличие @)
                    if (!email.contains("@")) {
                        logger.warn("Invalid email: {}", email);
                        System.out.println("Email must contain '@'.");
                        break;
                    }
                    User newUser = new User(name, email, age, LocalDateTime.now());
                    try {
                        userDao.createUser(newUser);
                        logger.info("User created: {}", newUser);
                        System.out.println("User created successfully: " + newUser);
                    } catch (RuntimeException e) {
                        logger.error("Failed to create user: {}", e.getMessage());
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    logger.info("User selected: Get User by ID");
                    System.out.print("Enter user ID: ");
                    Long id;
                    try {
                        id = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        logger.error("Invalid ID input: {}", e.getMessage());
                        System.out.println("Please enter a valid ID.");
                        break;
                    }
                    try {
                        User user = userDao.getUserById(id);
                        if (user != null) {
                            logger.info("User found: {}", user);
                            System.out.println("User: " + user);
                        } else {
                            logger.warn("User not found with ID: {}", id);
                            System.out.println("User with ID " + id + " not found.");
                        }
                    } catch (RuntimeException e) {
                        logger.error("Failed to get user: {}", e.getMessage());
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 3:
                    logger.info("User selected: Update User");
                    System.out.print("Enter user ID to update: ");
                    Long updateId;
                    try {
                        updateId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        logger.error("Invalid ID input: {}", e.getMessage());
                        System.out.println("Please enter a valid ID.");
                        break;
                    }
                    try {
                        User existingUser = userDao.getUserById(updateId);
                        if (existingUser == null) {
                            logger.warn("User not found with ID: {}", updateId);
                            System.out.println("User with ID " + updateId + " not found.");
                            break;
                        }
                        System.out.print("Enter new name (leave blank to keep '" + existingUser.getName() + "'): ");
                        String newName = scanner.nextLine();
                        if (!newName.isEmpty()) {
                            existingUser.setName(newName);
                        }
                        System.out.print("Enter new email (leave blank to keep '" + existingUser.getEmail() + "'): ");
                        String newEmail = scanner.nextLine();
                        if (!newEmail.isEmpty()) {
                            if (!newEmail.contains("@")) {
                                logger.warn("Invalid email: {}", newEmail);
                                System.out.println("Email must contain '@'.");
                                break;
                            }
                            existingUser.setEmail(newEmail);
                        }
                        System.out.print("Enter new age (enter 0 to keep " + existingUser.getAge() + "): ");
                        try {
                            int newAge = Integer.parseInt(scanner.nextLine());
                            if (newAge > 0 && newAge <= 150) {
                                existingUser.setAge(newAge);
                            } else if (newAge != 0) {
                                logger.warn("Invalid age: {}", newAge);
                                System.out.println("Age must be between 1 and 150.");
                                break;
                            }
                        } catch (NumberFormatException e) {
                            logger.error("Invalid age input: {}", e.getMessage());
                            System.out.println("Please enter a valid age.");
                            break;
                        }
                        userDao.updateUser(existingUser);
                        logger.info("User updated: {}", existingUser);
                        System.out.println("User updated successfully: " + existingUser);
                    } catch (RuntimeException e) {
                        logger.error("Failed to update user: {}", e.getMessage());
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 4:
                    logger.info("User selected: Delete User");
                    System.out.print("Enter user ID to delete: ");
                    Long deleteId;
                    try {
                        deleteId = Long.parseLong(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        logger.error("Invalid ID input: {}", e.getMessage());
                        System.out.println("Please enter a valid ID.");
                        break;
                    }
                    try {
                        userDao.deleteUser(deleteId);
                        logger.info("User deleted with ID: {}", deleteId);
                        System.out.println("User with ID " + deleteId + " deleted successfully.");
                    } catch (RuntimeException e) {
                        logger.error("Failed to delete user: {}", e.getMessage());
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 5:
                    logger.info("User selected: List All Users");
                    try {
                        List<User> users = userDao.getAllUsers();
                        if (users.isEmpty()) {
                            logger.info("No users found");
                            System.out.println("No users found.");
                        } else {
                            logger.info("Found {} users", users.size());
                            System.out.println("All users:");
                            for (User user : users) {
                                System.out.println(user);
                            }
                        }
                    } catch (RuntimeException e) {
                        logger.error("Failed to list users: {}", e.getMessage());
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                default:
                    logger.warn("Invalid choice: {}", choice);
                    System.out.println("Invalid choice. Please try again.");
            }

        }

        scanner.close();
        HibernateUtil.shutdown();
        logger.info("Application shutdown complete");
    }
}
