package chess.command;

import chess.domain.ChessGame;
import chess.view.OutputView;

public class End implements Command {
    protected End() {
    }

    @Override
    public void execute(ChessGame chessGame, OutputView outputView) {
        outputView.printFinishMessage();
        chessGame.end();
        if (chessGame.isFinished()) {
            outputView.printStatus(chessGame.statusOfWhite(), chessGame.statusOfBlack());
            outputView.printWinner(chessGame.getWinner());
        }
    }

    @Override
    public boolean isEnd() {
        return true;
    }
}