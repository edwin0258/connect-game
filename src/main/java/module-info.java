module com.example.connectgame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    exports edwin0258;
    opens edwin0258 to javafx.fxml;
}