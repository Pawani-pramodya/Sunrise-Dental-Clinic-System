package Test;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoginControllerTest {

    private String login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return "Username is required";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Password is required";
        }
        if (username.length() < 3) {
            return "Username must contain at least 3 characters";
        }
        if (password.length() < 5) {
            return "Password must contain at least 5 characters";
        }
        if (username.equals("admin") && password.equals("admin123")) {
            return "LOGIN_SUCCESS";
        }
        return "INVALID_CREDENTIALS";
    }

    @Test
    public void testValidLogin() {
        assertEquals("LOGIN_SUCCESS", login("admin", "admin123"));
    }

    @Test
    public void testInvalidPassword() {
        assertEquals("INVALID_CREDENTIALS", login("admin", "wrongPass"));
    }

    @Test
    public void testEmptyUsername() {
        assertEquals("Username is required", login("", "admin123"));
    }

    @Test
    public void testEmptyPassword() {
        assertEquals("Password is required", login("admin", ""));
    }

    @Test
    public void testShortUsername() {
        assertEquals("Username must contain at least 3 characters", login("ad", "admin123"));
    }

    @Test
    public void testShortPassword() {
        assertEquals("Password must contain at least 5 characters", login("admin", "123"));
    }

    @Test
    public void testNonExistingUser() {
        assertEquals("INVALID_CREDENTIALS", login("unknown", "admin123"));
    }
}