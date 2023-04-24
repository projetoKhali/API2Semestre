package org.openjfx.api2semestre.authentication;

public class Authentication {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static boolean login (User user) {
        if (currentUser != null) {
            System.out.println("Authentication.login() -- Error: User is already logged in");
            return false;
        }
        currentUser = user;
        return true;
    }

    public static void logout () {
        currentUser = null;
    }


}
