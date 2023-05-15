package org.openjfx.api2semestre.authentication;

import java.util.Optional;
import org.openjfx.api2semestre.data_utils.PasswordIncription;
import org.openjfx.api2semestre.database.QueryLibs;

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

    public static boolean verifyPassword(String password, User user) {
        // encontra o usuário pelo email
        Optional<User> loginUser = QueryLibs.selectUserByEmail(user.getEmail());
        User dataBaseUser = loginUser.get();
        // incripita a senha
        String insertPassword = PasswordIncription.encryptPassword(password);

        // verifica se a senha incriptada é a mesma da do banco
        if (dataBaseUser.getSenha() == insertPassword) {
            return true;
        }
        else{
            return false;
        }
    }

    public static void logout () {
        currentUser = null;
    }


}
