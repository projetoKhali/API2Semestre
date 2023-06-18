module org.openjfx.api2semestre {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive org.controlsfx.controls;
    requires transitive java.sql;
    requires transitive com.opencsv;

    opens org.openjfx.api2semestre.view.controllers.custom_tags to javafx.fxml;
    opens org.openjfx.api2semestre.view.controllers.templates to javafx.fxml;
    opens org.openjfx.api2semestre.view.controllers.popups to javafx.fxml;
    opens org.openjfx.api2semestre.view.controllers.views to javafx.fxml;
    opens org.openjfx.api2semestre.view.controllers to javafx.fxml;

    opens org.openjfx.api2semestre.view.utils.pretty_table_cell to javafx.base;
    opens org.openjfx.api2semestre.view.utils.wrappers to javafx.base;
    opens org.openjfx.api2semestre.view.utils.filters to javafx.base;
    opens org.openjfx.api2semestre.authentication to javafx.base;
    opens org.openjfx.api2semestre.data to javafx.base;

    exports org.openjfx.api2semestre;
}
