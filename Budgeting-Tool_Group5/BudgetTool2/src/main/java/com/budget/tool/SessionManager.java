package com.budget.tool;

public class SessionManager {
    private static int currentUserId = -1; // -1 indicates no user is logged in
    private static String currentUserName = null;

    // Set the logged-in user's ID and name
    public static void setCurrentUser(int userId, String username) {
        currentUserId = userId;
        currentUserName = username;
    }

    // Get the logged-in user's ID
    public static int getCurrentUserId() {
        return currentUserId;
    }

    // Get the logged-in user's name
    public static String getCurrentUserName() {
        return currentUserName;
    }

    // Clear session when logging out
    public static void clearSession() {
        currentUserId = -1;
        currentUserName = null;
    }
}

