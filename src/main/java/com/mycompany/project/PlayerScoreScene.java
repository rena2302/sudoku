package com.mycompany.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.util.List;

import com.mycompany.project.database.MyConnection;
import com.mycompany.project.database.PlayerScore;

public class PlayerScoreScene {
    private TableView<PlayerScore> table;

    public PlayerScoreScene(String userId) {
        table = new TableView<>();
        setupTableColumns();
        loadPlayerScores(userId);
    }

    @SuppressWarnings("unchecked")
    private void setupTableColumns() {
        TableColumn<PlayerScore, String> modeColumn = new TableColumn<>("Mode");
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));

        TableColumn<PlayerScore, Integer> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<PlayerScore, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));

        TableColumn<PlayerScore, java.sql.Date> dateCompletedColumn = new TableColumn<>("Date Completed");
        dateCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));

        table.getColumns().addAll(modeColumn, timeColumn, scoreColumn, dateCompletedColumn);
    }

    private void loadPlayerScores(String userId) {
        List<PlayerScore> scores = MyConnection.getPlayerScores(userId);
        ObservableList<PlayerScore> observableScores = FXCollections.observableArrayList(scores);
        table.setItems(observableScores);
    }

    public Scene getScene() {
        VBox vbox = new VBox(table);
        return new Scene(vbox);
    }
}
