package org.openjfx.api2semestre.custom_tags;
import javafx.beans.DefaultProperty;

/// Custom FXML tag that acts as a list-item inside ViewConfig.
/// Each Permission tag inside of a ViewConfig contains the value of one Permission enum as a String value
@DefaultProperty("value")
public class Permission {
    
    // static array of Permission enum to reference during Permission tag instantiation
    private static final org.openjfx.api2semestre.authentication.Permission[] PERMISSIONS 
    = org.openjfx.api2semestre.authentication.Permission.values();

    // the Permission value
    private String value;

    // Empty constructor required by JavaFX
    public Permission (){
        super();
    }

    // Get method for the String value of the Permission tag
    public String getValue() {
        return value;
    }

    // Sets the value of the Permission tag to the given String value.
    // 
    // This method validates the given value and throws an IllegalArgumentException in case of an invalid value.
    // Values must match the values contained in the Permission enum:
    // src\main\java\org\openjfx\api2semestre\classes\Permission.java
    // 
    // Examples of valid and invalid values:
    // 
    // <Permission>oi</Permission> <!-- This will throw an IllegalArgumentException since "oi" is not a Permission-->
    // <Permission>Validate</Permission> <!-- This is a valid tag since the Permission enum contains this value-->
    // <Permission>this is an invalid value</Permission> <!-- This will also throw an IllegalArgumentException -->    
    public Permission setValue (String value) {
        for (org.openjfx.api2semestre.authentication.Permission permission : PERMISSIONS) {
            if (permission.getStringValue().equals(value)) {
                this.value = value;
                return this;
            }
        }
        throw new IllegalArgumentException("Invalid permission value: at" + value);
    }

    // Creates a new Permission tag from the given String value.
    // Usage, while creating a new View (.fxml) file :
    // 
    // <ViewConfig>
    //     <permissions>
    //         <Permission>Appoint</Permission>         <!-- Permission tag in this line-->
    //     </permissions>
    // </ViewConfig>
    // 
    // Make sure to create the Permission tags inside between the "<permissions>" and "</permissions>" tags 
    // inside the ViewConfig tag as shown above. You can add as many Permission tags as needed for the view.
    public static Permission fromValue(String value) {
        return new Permission().setValue(value);
    }
}
