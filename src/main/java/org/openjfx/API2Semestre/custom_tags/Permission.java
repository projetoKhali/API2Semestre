package org.openjfx.api2semestre.custom_tags;

import javafx.beans.DefaultProperty;
// import javafx.scene.Group;

@DefaultProperty("value")
public class Permission {
    public static final Permission APPOINT = new Permission("Appoint");
    public static final Permission VALIDATE = new Permission("Validate");
    public static final Permission REGISTER = new Permission("Register");

    private String value;

    public Permission (){}

    public Permission (String value) {
        super();
        this.value = value;
    }    

    public String getValue() {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public static Permission fromValue (String value) {
        // System.out.println("Permission.fromValue");
        switch (value) {
            case "Appoint":
                // System.out.println("Appoint");
                return APPOINT;
            case "Validate":
                // System.out.println("Validate");
                return VALIDATE;
            case "Register":
                // System.out.println("Register");
                return REGISTER;
            default:
                throw new IllegalArgumentException("Invalid permission value: " + value);
        }
    }
}
