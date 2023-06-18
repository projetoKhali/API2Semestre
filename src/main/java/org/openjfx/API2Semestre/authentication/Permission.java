package org.openjfx.api2semestre.authentication;

import java.util.ArrayList;
import java.util.Optional;

import org.openjfx.api2semestre.database.QueryLibs;

// Represents the user's permissions in the app
public enum Permission {

    // List of every Permission:
    // To add new permissions to the enum, insert them with a coma ( , ) at the end.
    // The last item on the list should have a semicolon instead ( ; ) to indicate the end of the list of values of the enum
    Appoint("Appoint"),
    Validate("Validate"),
    Register("Register"),
    Report("Report"),
    FullAccess("FullAccess"),
    ;

    // The String value between the parentheses of each of the enum values above
    private String stringValue;

    // Create a new Permission from a string. Exclusively for the list above
    private Permission (String value) {
        this.stringValue = value;
    }

    // returns the string value of the Permission
    public String getStringValue() {
        return stringValue;
    }

    public static Permission[] getPermissions (User u) {
        ArrayList<Permission> permissions = new ArrayList<>();
        if (u.getProfile() == Profile.Administrator) {
            permissions.add(Permission.FullAccess);
            permissions.add(Permission.Register);
            permissions.add(Permission.Report);
        }

        // Inicia a conexão com o banco de dados
        Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();

        if (QueryLibs.selectResultCentersManagedBy(u.getId(), connectionOptional).length > 0) permissions.add(Permission.Validate);
        if (QueryLibs.selectResultCentersOfMember(u.getId(), connectionOptional).length > 0) permissions.add(Permission.Appoint);

        // Fecha a conexão com o banco de dados
        QueryLibs.close(connectionOptional);

        return permissions.toArray(Permission[]::new);
    }

}
