package org.openjfx.api2semestre.authentication;

// Represents the user's permissions in the app
public enum Permission {

    // List of every Permission:
    // To add new permissions to the enum, insert them with a coma ( , ) at the end.
    // The last item on the list should have a semicolon instead ( ; ) to indicate the end of the list of values of the enum
    Appoint("Appoint"),
    Validate("Validate"),
    Register("Register");

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
}
