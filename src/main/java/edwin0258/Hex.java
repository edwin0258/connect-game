package edwin0258;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Hex extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Hex.class.getResource("hex.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 400);
        scene.getStylesheets().add(this.getClass().getResource("hex.css").toExternalForm());
        stage.setTitle("Hex");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
