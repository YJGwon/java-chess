package chess;

import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.staticFileLocation;

import chess.dao.BoardDao;
import chess.dao.GameDao;
import chess.domain.Camp;
import chess.domain.board.Position;
import chess.domain.gamestate.Score;
import chess.domain.piece.Piece;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class WebApplication {

    public static void main(String[] args) {
        staticFileLocation("/static");
        GameController gameController = new GameController();
        JsonTransformer jsonTransformer = new JsonTransformer();

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("ready", true);
            return render(model, "index.html");
        });

        get("/start", (req, res) -> {
            gameController.start();
            res.redirect("/play");
            return null;
        });

        get("/play", (req, res) -> {
            Map<Position, Piece> board = gameController.getBoard();
            Map<String, Object> model = board.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
            model.put("started", true);
            model.put("ready", false);
            return render(model, "index.html");
        });

        post("/move", (req, res) -> {
            Map<String, String> positions = Arrays.stream(req.body().split("&"))
                    .collect(Collectors.toMap(
                            data -> data.substring(0, data.indexOf("=")),
                            data -> data.substring(data.indexOf("=") + 1)
                    ));
            System.out.println("소스 위치 : " + positions.get("source"));
            gameController.move(Position.of(positions.get("source")), Position.of(positions.get("target")));
            if (gameController.isGameFinished()) {
                res.redirect("/end");
                return null;
            }
            res.redirect("/play");
            return null;
        });

        get("/status", (req, res) -> {
            Map<Camp, Score> scores = gameController.status();
            Map<String, Object> model = scores.entrySet().stream()
                    .collect(Collectors.toMap(entry -> entry.getKey().toString(), Map.Entry::getValue));
            return jsonTransformer.render(model);
        });

        get("/save", (req, res) -> {
            try {
                GameDao gameDao = new GameDao();
                gameDao.save();
                BoardDao boardDao = new BoardDao();
                boardDao.save(gameController.getBoard());
            } catch (Exception e) {
                res.status(500);
                return res;
            }
            res.status(201);
            return res;
        });

        get("/end", (req, res) -> {
            gameController.end();
            Map<String, Object> model = new HashMap<>();
            Camp winner = gameController.getWinner();
            model.put("winner", winner);
            if (winner == Camp.NONE) {
                model.put("tie", true);
            }
            model.put("started", false);
            model.put("ready", true);
            return render(model, "result.html");
        });

        exception(Exception.class, (exception, request, response) -> {
            response.status(400);
            response.body("[ERROR] " + exception.getMessage());
        });
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}
