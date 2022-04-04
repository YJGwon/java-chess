package chess.dao;

import chess.domain.Camp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GameDao {

    public void save() throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        final String sql = chooseSaveSql();

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setBoolean(1, Camp.BLACK.isNotTurn());
        statement.execute();
        DatabaseConnector.close(connection, statement);
    }

    private String chooseSaveSql() throws SQLException {
        String sql = "insert into game (no, white_turn) values (0,?)";
        if (isGameExist()) {
            sql = "update game set white_turn = ?";
        }
        return sql;
    }

    private boolean isGameExist() throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        final String sql = "select no from game";

        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        boolean gameExist = resultSet.next();
        DatabaseConnector.close(connection, statement, resultSet);
        return gameExist;
    }

    public boolean isWhiteTurn() throws SQLException {
        Connection connection = DatabaseConnector.getConnection();
        final String sql = "select white_turn from game";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        boolean whiteTurn = resultSet.getBoolean("white_turn");
        DatabaseConnector.close(connection, statement, resultSet);
        return whiteTurn;
    }
}