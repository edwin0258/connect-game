module com.example.connectgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.connectgame to javafx.fxml;
    exports com.example.connectgame;
}