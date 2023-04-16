module org.openjfx.API2Semestre {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.openjfx.API2Semestre to javafx.fxml;
    exports org.openjfx.API2Semestre;
}
