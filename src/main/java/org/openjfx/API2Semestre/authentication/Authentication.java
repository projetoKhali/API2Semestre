package org.openjfx.api2semestre.authentication;

public class Authentication {
    static User currentUser;

    public void login (User user) {
        if (currentUser != null) {
            System.out.println("Authentication.login() -- Error: User is already logged in");
            return;
        }
        currentUser = user;
    }

    
}
