package chess.domain.command;

import chess.domain.ChessGame;
import chess.domain.board.Position;

public abstract class Command {
    private final Type type;

    public Command(Type type) {
        this.type = type;
    }

    public static Command createStart() {
        return new Start();
    }

    public static Command createMove(Position sourcePosition, Position targetPosition) {
        return new Move(sourcePosition, targetPosition);
    }

    public static Command createStatus() {
        return new Status();
    }

    public static Command createEnd() {
        return new End();
    }

    public final boolean isType(Type type) {
        return this.type == type;
    }

    public abstract void execute(ChessGame chessGame);
}
