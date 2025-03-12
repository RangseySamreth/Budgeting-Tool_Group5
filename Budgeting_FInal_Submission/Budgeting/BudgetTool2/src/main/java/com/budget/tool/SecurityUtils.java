package com.budget.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {
    // Method to hash the password using SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error hashing password: " + e.getMessage());
            return null;
        }
    }

    // Method to verify the password by comparing hashes
    public boolean verifyPassword(String enteredPassword, String storedPassword) {
        String hashedEnteredPassword = hashPassword(enteredPassword);
        return hashedEnteredPassword != null && hashedEnteredPassword.equals(storedPassword);
    }
}
