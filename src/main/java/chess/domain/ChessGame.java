package chess.domain;

import chess.domain.board.Board;
import chess.domain.board.Position;
import chess.domain.gamestate.Ready;
import chess.domain.gamestate.State;

public class ChessGame {
    private State state;

    public ChessGame() {
        this.state = new Ready();
    }

    public void start() {
        this.state = this.state.start();
    }

    public void move(Position sourcePosition, Position targetPosition) {
        this.state = this.state.move(sourcePosition, targetPosition);
    }

    public void end() {
        this.state = this.state.end();
    }

    public double statusOfBlack() {
        return this.state.statusOfBlack();
    }

    public double statusOfWhite() {
        return this.state.statusOfWhite();
    }

    public boolean isFinished() {
        return this.state.isFinished();
    }

    public Board getBoard() {
        return this.state.getBoard();
    }

    public Camp getWinner() {
        return this.state.getWinner();
    }
}
