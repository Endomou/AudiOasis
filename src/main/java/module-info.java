module com.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires charm.glisten;

    opens com.project to javafx.fxml;
    exports com.project;
}