module com.project.audioasis2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens com.project to javafx.fxml;
    exports com.project;
}