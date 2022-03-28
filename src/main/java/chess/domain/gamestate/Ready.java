package chess.domain.gamestate;

import chess.domain.Camp;
import chess.domain.board.Board;
import chess.domain.board.Position;

public class Ready implements State {
    private static final String ERROR_CANT_MOVE = "게임이 시작되지 않아 기물을 이동할 수 없습니다.";
    private static final String ERROR_NO_STATUS = "게임이 시작되지 않아 상태를 확인할 수 없습니다.";
    private static final String ERROR_NO_WINNER = "게임이 시작되지 않아 승패를 판정할 수 없습니다.";
    private static final String ERROR_NO_BOARD = "체스판이 아직 준비되지 않았습니다.";

    @Override
    public State start() {
        return new Running(new Board());
    }

    @Override
    public State move(Position beforePosition, Position afterPosition) {
        throw new IllegalStateException(ERROR_CANT_MOVE);
    }

    @Override
    public State end() {
        return this;
    }

    @Override
    public double statusOfBlack() {
        throw new IllegalStateException(ERROR_NO_STATUS);
    }

    @Override
    public double statusOfWhite() {
        throw new IllegalStateException(ERROR_NO_STATUS);
    }

    @Override
    public Camp getWinner() {
        throw new IllegalStateException(ERROR_NO_WINNER);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Board getBoard() {
        throw new IllegalStateException(ERROR_NO_BOARD);
    }
}
