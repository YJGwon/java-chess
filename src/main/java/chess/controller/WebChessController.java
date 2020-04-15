package chess.controller;

import chess.Scores;
import chess.dao.IsFinishedDao;
import chess.dao.PiecesDao;
import chess.dao.TurnDao;
import chess.dto.BoardDto;
import chess.exception.InvalidMovementException;
import chess.service.ChessGameService;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class WebChessController {
    public static void main(String[] args) {
        staticFiles.location("/public");
        ChessGameService service = new ChessGameService(new PiecesDao(), new TurnDao(), new IsFinishedDao());

        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            if (service.isEmpty()) {
                service.init();
            }
            constructModel(service, model);
            return render(model, "index.html");
        });

        post("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            String source = req.queryParams("source");
            String target = req.queryParams("target");
            try {
                service.play(source, target);
            } catch (RuntimeException e) {
                model.put("error-message", e.getMessage());
                constructModel(service, model);
                return render(model, "index.html");
            }
            if (service.isFinished()) {
                model.put("winner", service.isTurnWhite() ? "흑팀" : "백팀");
                return render(model, "result.html");
            }
            constructModel(service, model);
            return render(model, "index.html");
        });

        post("/newgame", (req, res) -> {
            service.init();
            res.redirect("/");
            return null;
        });

        post("/scores", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("scores", Scores.calculateScores(service.getBoard()));
            return render(model, "scores.html");
        });
    }

    private static void constructModel(ChessGameService service, Map<String, Object> model) throws SQLException {
        BoardDto boardDTO = new BoardDto(service.getBoard());
        Map<String, String> pieces = boardDTO.getBoard();
        for (String positionKey : pieces.keySet()) {
            String imageName = pieces.get(positionKey);
            if (imageName.equals(".")) {
                imageName = "blank";
            }
            model.put(positionKey, imageName);
        }
    }

    private static String render(Map<String, Object> model, String templatePath) {
        return new HandlebarsTemplateEngine().render(new ModelAndView(model, templatePath));
    }
}