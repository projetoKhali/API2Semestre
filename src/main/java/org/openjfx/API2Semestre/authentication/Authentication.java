package org.openjfx.api2semestre.authentication;

import org.openjfx.api2semestre.data_utils.PasswordIncription;
import org.openjfx.api2semestre.database.QueryLibs;

public class Authentication {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    private static boolean setCurrentUser (User user) {
        if (currentUser != null) {
            System.out.println("Authentication.login() -- Error: User is already logged in");
            return false;
        }
        currentUser = user;
        return true;
    }

    public static boolean login(String email, String password) {
        try {

            // encontra o usuário pelo email
            User loginUser = QueryLibs.selectUserByEmail(email).get();

            // incripita a senha
            String insertPassword = PasswordIncription.encryptPassword(password);

            // verifica se a senha incriptada é a mesma da do banco
            if (insertPassword.equals(loginUser.getSenha())) {
                return setCurrentUser(loginUser);
            }

            else System.out.println("Erro: Senha inválida");
        }
        
        catch (Exception e) {}
        
        return false;

    }

    public static void logout () {
        currentUser = null;
    }


}
