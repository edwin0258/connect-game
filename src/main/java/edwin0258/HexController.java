package edwin0258;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HexController {

    @FXML
    private GridPane gridPane;
    @FXML
    private HBox headerBox;
    @FXML
    private HBox startButtons;

    private Piece currentPlayer = Piece.X;
    private final int BOARD_SIZE = 11;
    private int spacesUsed = 0;
    private Label infoText;
    private GridPane gp; // game grid pane

    public void initialize() {
        gridPane.setAlignment(Pos.TOP_CENTER);

    }

    public void nextPlayer() {
        currentPlayer = (currentPlayer == Piece.X) ? Piece.O : Piece.X;
    }

    public void disableBoard() {
        gp.getChildren().forEach(row -> ((HBox) row).getChildren().forEach(btn -> {
            ((Button) btn).setDisable(true);
        }));
    }

    public void resetBoard() {
        gp.getChildren().forEach(row -> ((HBox) row).getChildren().stream()
        .filter(btn -> {
            return (((Button) btn).getStyleClass().contains("boardBtn"));
        })
        .forEach(btn -> {
            Button boardBtn = (Button) btn;
            boardBtn.setDisable(false);
            boardBtn.setText("");
            boardBtn.getStyleClass().clear();
            boardBtn.getStyleClass().add("button");
            boardBtn.getStyleClass().add("boardBtn");
        }));

        currentPlayer = Piece.X;
        spacesUsed = 0;
        this.infoText.setText("Current Player: X");
    }
    private void calculateWinner() {
        List<List<String>> boardValues = new ArrayList<>();
        gp.getChildren().forEach(row -> {
            boardValues.add(((HBox) row).getChildren().stream()
            .filter(btn -> {
              return (((Button) btn).getStyleClass().contains("boardBtn"));
            })
            .map(btn -> {
                String squareValue = ((Button) btn).getText();
                if(squareValue.equals("")) return ".";
                else return squareValue;
            }).toList());
        });

        WinnerCalculator wc = new WinnerCalculator(boardValues);
        Winner winner = wc.computeWinner();
        if(winner != Winner.NONE) {
            String winnerText = (winner == Winner.PLAYER_O) ? "O" : "X";
            this.infoText.setText(String.format("Winner is: %s", winnerText));
            disableBoard();
        }

    }

    public void onSquareClick(ActionEvent e) {

        Button btn = (Button) e.getSource();
        String btnText = btn.getText();
        if(Objects.equals(btnText, "")) {
            spacesUsed += 1;
            if(currentPlayer == Piece.X) {
                this.infoText.setText("Current Player: O");
                btn.setText("X");
                btn.isDisabled();
                btn.getStyleClass().add("boardBtnX");
            } else {
                this.infoText.setText("Current Player: X");
                btn.setText("O");
                btn.isDisabled();
                btn.getStyleClass().add("boardBtnO");
            }

            calculateWinner();
            nextPlayer();
        }


    }
    private void createBoard() {
        this.gp = new GridPane();
        this.infoText = new Label();
        this.infoText.setText("Current Player: X");
        gp.setAlignment(Pos.CENTER);
        gp.getStyleClass().add("board");
        for(int i = 0; i < BOARD_SIZE; i++) {
            HBox hb = new HBox();
            for(int y = 0; y < BOARD_SIZE+i; y++) {
                Button squareBtn = new Button();
                squareBtn.setText("");
                squareBtn.setPrefSize(30.0, 30.0);
                squareBtn.setMaxSize(30.0, 30.0);
                if(y < i) { // filler buttons for styling
                   squareBtn.getStyleClass().add("fillerBtn");
                   hb.getChildren().add(squareBtn);
                   continue;
                }
                squareBtn.getStyleClass().add("boardBtn");
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

        gridPane.getChildren().add(this.infoText);
        GridPane.setRowIndex(this.infoText, 0);
        gridPane.getChildren().add(gp);
        GridPane.setRowIndex(gp, 1);
        gridPane.setAlignment(Pos.CENTER);

        Button resetGame = new Button();
        resetGame.setText("Reset");
        resetGame.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                resetBoard();
            }
        });

        gridPane.getChildren().add(resetGame);
        GridPane.setRowIndex(resetGame, 2);
    }


    public void onStartGameClick(ActionEvent e) {
        System.out.println(e.getTarget());

        headerBox.setVisible(false);
        startButtons.setVisible(false);
        createBoard();
        System.out.println("Hello World");
    }
}
