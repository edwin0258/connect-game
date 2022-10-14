package edwin0258;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.Objects;

public class HexController {

    @FXML
    private GridPane gridPane;
    @FXML
    private HBox headerBox;
    @FXML
    private HBox startButtons;

    private Piece currentPlayer = Piece.X;

    public void initialize() {
        gridPane.setAlignment(Pos.TOP_CENTER);
    }

    public void nextPlayer() {
        currentPlayer = (currentPlayer == Piece.X) ? Piece.O : Piece.X;
    }

    public void onSquareClick(ActionEvent e) {

        Button btn = (Button) e.getSource();
        String btnText = btn.getText();
        if(Objects.equals(btnText, "")) {
            if(currentPlayer == Piece.X) {
                btn.setText("X");
                btn.getStyleClass().add("boardBtnX");
            } else {
                btn.setText("O");
                btn.getStyleClass().add("boardBtnO");
            }

            nextPlayer();
        }


    }
    private void createBoard() {
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.getStyleClass().add("board");
        //gp.minHeight(400.0);
        for(int i = 0; i < 6; i++) {
            HBox hb = new HBox();
            for(int y = 0; y < 6; y++) {
                Button squareBtn = new Button();
                squareBtn.setText("");
                squareBtn.getStyleClass().add("boardBtn");
                squareBtn.setPrefSize(30.0, 30.0);
                squareBtn.setMaxSize(30.0, 30.0);
                squareBtn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        onSquareClick(actionEvent);
                    }
                });
                hb.getChildren().add(squareBtn);
            }

            gp.getChildren().add(hb);
            GridPane.setRowIndex(hb, i);
        }
        gridPane.getChildren().add(gp);
        gridPane.setAlignment(Pos.CENTER);
    }


    public void onStartGameClick(ActionEvent e) {
        System.out.println(e.getTarget());

        headerBox.setVisible(false);
        startButtons.setVisible(false);
        createBoard();
        System.out.println("Hello World");
    }
}
