module org.openjfx.api2semestre {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.controlsfx.controls;
    requires transitive java.sql;
    requires transitive com.opencsv;

    opens org.openjfx.api2semestre.view_controllers.templates to javafx.fxml;
    opens org.openjfx.api2semestre.view_controllers.popups to javafx.fxml;
    opens org.openjfx.api2semestre.view_controllers to javafx.fxml;

    opens org.openjfx.api2semestre.custom_tags to javafx.fxml;
    opens org.openjfx.api2semestre.view_utils to javafx.base;
    opens org.openjfx.api2semestre.data to javafx.base;
    opens org.openjfx.api2semestre.authentication to javafx.base;
    
    exports org.openjfx.api2semestre;
}
