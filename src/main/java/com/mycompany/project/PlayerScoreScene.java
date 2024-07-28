package com.mycompany.project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
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
        table.getColumns().clear();

        TableColumn<PlayerScore, String> modeColumn = new TableColumn<>("Mode");
        modeColumn.setCellValueFactory(new PropertyValueFactory<>("mode"));
        modeColumn.setPrefWidth(100);
        modeColumn.setCellFactory(column -> new TableCell<PlayerScore, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        TableColumn<PlayerScore, String> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeColumn.setPrefWidth(100);
        timeColumn.setCellFactory(column -> new TableCell<PlayerScore, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(item);
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        TableColumn<PlayerScore, Integer> scoreColumn = new TableColumn<>("Score");
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreColumn.setPrefWidth(100);
        scoreColumn.setCellFactory(column -> new TableCell<PlayerScore, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.toString());
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        TableColumn<PlayerScore, java.sql.Date> dateCompletedColumn = new TableColumn<>("Date Completed");
        dateCompletedColumn.setCellValueFactory(new PropertyValueFactory<>("dateCompleted"));
        dateCompletedColumn.setPrefWidth(150);
        dateCompletedColumn.setCellFactory(column -> new TableCell<PlayerScore, java.sql.Date>() {
            @Override
            protected void updateItem(java.sql.Date item, boolean empty) {
                super.updateItem(item, empty);
                setText(item == null ? "" : item.toString());
                setAlignment(javafx.geometry.Pos.CENTER);
            }
        });

        table.getColumns().addAll(modeColumn, timeColumn, scoreColumn, dateCompletedColumn);
    }


    private void loadPlayerScores(String userId) {
        List<PlayerScore> scores = MyConnection.getPlayerScores(userId);
        ObservableList<PlayerScore> observableScores = FXCollections.observableArrayList(scores);
        table.setItems(observableScores);
    }

    public Scene getScene() {
         Button backButton = new Button("Back to Menu");
        backButton.setOnAction(e -> App.getInstance().returnToMainMenu());

        VBox vbox = new VBox(10); // Add spacing between elements
        HBox hbox = new HBox(backButton); // Add the button to an HBox
        hbox.setStyle("-fx-padding: 10;"); // Optional padding style for better layout

        vbox.getChildren().addAll(hbox, table);

        return new Scene(vbox); // Adjust scene size as needed
    }
}
