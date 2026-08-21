/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package service;

/**
 *
 * @author pawan
 */

import dao.UserDAO;

public class LoginService {

    private final UserDAO userDAO;

    public LoginService() {
        userDAO = new UserDAO();
    }

    public boolean authenticate(String username, String password) {

        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        if (password == null || password.trim().isEmpty()) {
            return false;
        }

        return userDAO.login(username.trim(), password);
    }
}