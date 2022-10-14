package edwin0258;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polygon;

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

    private Piece currentPlayer = Piece.O;
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

        currentPlayer = Piece.O;
        spacesUsed = 0;
        this.infoText.setText("Current Player: O");
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

    private Polygon createHexagon() {
        Polygon hexagon = new Polygon();
        hexagon.getPoints().addAll(200.0, 50.0,
                400.0, 50.0,
                450.0, 150.0,
                400.0, 250.0,
                200.0, 250.0,
                150.0, 150.0);
        Button hexBtn = new Button();

        return hexagon;
    }
    private void createBoard() {
        this.gp = new GridPane();
        this.infoText = new Label();
        this.infoText.setText("Current Player: O");
        gp.setAlignment(Pos.CENTER);
        gp.getStyleClass().add("board");
        for(int i = 0; i < BOARD_SIZE; i++) {
            HBox hb = new HBox();
            for(int y = 0; y < BOARD_SIZE+i; y++) {
                Button squareBtn = new Button();
                squareBtn.setText("");

                if(y < i) { // filler buttons for styling
                    squareBtn.setPrefSize(15.0, 35.0);
                    squareBtn.setMaxSize(15.0, 35.0);
                    squareBtn.getStyleClass().add("fillerBtn");
                    hb.getChildren().add(squareBtn);
                    continue;
                }
                squareBtn.setPrefSize(30.0, 35.0);
                squareBtn.setMaxSize(30.0, 35.0);
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
        gp.setVgap(-10.0);
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
        headerBox.setVisible(false);
        startButtons.setVisible(false);
        createBoard();
    }

    public void onAboutClick() {
        headerBox.setVisible(false);
        startButtons.setVisible(false);

        Label aboutGame = new Label();
        Hyperlink gameInfoLink = new Hyperlink();
        gameInfoLink.setText("https://en.wikipedia.org/wiki/Hex_(board_game)");
        gameInfoLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //getHostServices().showDocument(gameInfoLink.getText());
            }
        });

        aboutGame.setText("Made by Devin Miller, 10/13/22\nGame Info: ");
        gridPane.getChildren().add(aboutGame);
        GridPane.setRowIndex(aboutGame, 0);
        gridPane.getChildren().add(gameInfoLink);
        GridPane.setRowIndex(gameInfoLink, 1);
    }
}
