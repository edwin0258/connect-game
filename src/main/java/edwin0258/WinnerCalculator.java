package edwin0258;

import javafx.util.Pair;

import java.util.*;
import static java.util.Arrays.stream;

public class WinnerCalculator {
    private List<List<Piece>> board = new ArrayList<>();
    private Map<String, Boolean> checked = new HashMap<>();
    private final Integer boardWidth, boardHeight;
    public WinnerCalculator(List<List<String>> board) {
        board.forEach(line -> {
            this.board.add(line.stream().map(x -> {
                if(x.equals("X")) return Piece.X;
                if(x.equals("O")) return Piece.O;
                return Piece.NONE;
            }).toList());
        });
        boardWidth = this.board.get(0).size();
        boardHeight = this.board.size();
    }
    private void markSquareAsChecked(List<Integer> square) {
        String squareKey = String.format("%o:%o", square.get(0), square.get(1));
        checked.put(squareKey, true);
    }
    private boolean isSquareChecked(List<Integer> square) {
        String squareKey = String.format("%o:%o", square.get(0), square.get(1));
        return checked.containsKey(squareKey);
    }
    private List<List<Integer>> findNextSquares(List<Integer> square, Piece p) {
        if(isSquareChecked(square)) {
            return null;
        }
        markSquareAsChecked(square);
        Integer x = square.get(0);
        Integer y = square.get(1);
        List<List<Integer>> neighbors = Arrays.asList(
                List.of(x + 1, y), List.of(x - 1, y),
                List.of(x, y + 1), List.of(x, y - 1),
                List.of(x + 1, y - 1), List.of(x - 1, y + 1));
        neighbors = neighbors.stream().filter(n -> {
            if(isSquareChecked(n)) return false;
            if(n.get(0) >= 0 && n.get(0) < boardWidth && n.get(1) >= 0 && n.get(1) < boardHeight)
                if(board.get(n.get(1)).get(n.get(0)) == p) // check if piece is correct e.g X == X
                    return true;
            return false;
        }).toList();
        return neighbors;
    }
    private List<List<Integer>> findStartingSquares(Piece p) {
        List<List<Integer>> squares = new ArrayList<>();
        if(p == Piece.O) {
            for(int i = 0; i < boardWidth; i++) {
                // top
                if(board.get(0).get(i) == Piece.O) {
                    squares.add(Arrays.asList(i, 0));
                }
                // bottom
                if(board.get(boardHeight - 1).get(i) == Piece.O) {
                    squares.add(Arrays.asList(i, boardHeight - 1));
                }
            }
        } else if(p == Piece.X) {
            for(int i = 0; i < boardHeight; i++) {
                // left
                if(board.get(i).get(0) == Piece.X) {
                    squares.add(Arrays.asList(0, i));
                }
                // right
                if(board.get(i).get(boardWidth - 1) == Piece.X) {
                    squares.add(Arrays.asList(boardWidth - 1, i));
                }
            }
        }
        return squares;
    }
    private List<List<Integer>> isAWinner(List<List<Integer>> startingSquares, Piece p) {
        int xOrY = (p == Piece.X) ? 0 : 1; // x coord for X piece, y coord for O piece
        List<List<Integer>> winPath = new ArrayList<>();
        for(List<Integer> square : startingSquares) {
            winPath.clear();
            winPath.add(square);
            Integer startCoord = square.get(xOrY);
            List<List<Integer>> neighbors = List.of(square);
            do {
                Stack<List<Integer>> nextSquares = new Stack<>();
                for(List<Integer> n : neighbors) {
                    List<List<Integer>> result = findNextSquares(n, p);
                    if(result != null) {
                        winPath.add(n);
                        nextSquares.addAll(result);
                    }
                }
                neighbors = nextSquares.stream().toList();
                List<List<Integer>> winSquares = neighbors.stream().filter(n -> {
                    return n.get(xOrY) + startCoord == boardWidth - 1;
                }).toList();
                winPath.addAll(winSquares);
                if(winSquares.size() > 0) return winPath;
            } while(neighbors.size() > 0);

        }
        return null;
    }
    public Pair<Winner, List<List<Integer>>> computeWinner() {
        if(boardWidth == 1 && boardHeight == 1) {
            if(board.get(0).get(0) == Piece.X) return new Pair<>(Winner.PLAYER_X, List.of(List.of(0, 0)));
            if(board.get(0).get(0) == Piece.O) return new Pair<>(Winner.PLAYER_O, List.of(List.of(0, 0)));;
            return new Pair<>(Winner.NONE, null);
        }
        List<List<Integer>> startingO = findStartingSquares(Piece.O);
        List<List<Integer>> startingX = findStartingSquares(Piece.X);
        List<List<Integer>> winPath;

        winPath = isAWinner(startingO, Piece.O);
        if(winPath != null) return new Pair<>(Winner.PLAYER_O, winPath);
        winPath = isAWinner(startingX, Piece.X);
        if(winPath != null) return new Pair<>(Winner.PLAYER_X, winPath);

        return new Pair<>(Winner.NONE, null);
    }
}