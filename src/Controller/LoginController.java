package Controller;

import service.LoginService;

public class LoginController {

    private final LoginService loginService;

    public LoginController() {
        this.loginService = new LoginService();
    }

    // Input validation prior to authentication
    public String validateInput(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (username.trim().length() < 3) {
            return "Username must contain at least 3 characters";
        }
        if (password.trim().length() < 5) {
            return "Password must contain at least 5 characters";
        }
        return "VALID";
    }

    // Authenticates user via LoginService (which connects to UserDAO and MySQL DB)
    public String login(String username, String password) {
        String validationResult = validateInput(username, password);
        if (!validationResult.equals("VALID")) {
            return validationResult;
        }

        // LoginService එකේ ඇති authenticate method එක call කරයි
        boolean authenticated = loginService.authenticate(username, password);
        if (authenticated) {
            return "LOGIN_SUCCESS";
        }

        return "INVALID_CREDENTIALS";
    }
}