module org.openjfx.mavenproject2 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.openjfx.mavenproject2 to javafx.fxml;
    exports org.openjfx.mavenproject2;
}
