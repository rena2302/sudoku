package com.mycompany.project.database;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyConnection {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        if (URL == null || USER == null || PASSWORD == null) {
            throw new IllegalStateException("Database environment variables are not set.");
        }
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static List<PlayerScore> getPlayerScores(String userId) {
        List<PlayerScore> scores = new ArrayList<>();
        String query = "SELECT mode, score, time, date_completed FROM record where user_id = ?";
    
        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                String mode = rs.getString("mode");
                int score = rs.getInt("score");
                int time = rs.getInt("time");
                String timeFormatted = updateTimerDisplay(time);
                java.sql.Date dateCompleted = rs.getDate("date_completed");
    
                scores.add(new PlayerScore(mode, timeFormatted, score, dateCompleted));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return scores;
    }
    
    private static String updateTimerDisplay(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        return timeFormatted;
    }
}
