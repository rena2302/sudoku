package com.mycompany.project;


import com.mycompany.project.server.Server;
import com.mycompany.project.util.SoundManager;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class WaitingRoomScreen {
    private Scene waitingRoomScene;
    private Server server; // Reference to the server instance
    private Button btnStartGame;
    private SoundManager soundManager = new SoundManager();

    public WaitingRoomScreen(String serverAddress, int port, Server server) {
        this.server = server;

        Label lblWaitingMessage = new Label("Waiting for players...");
        lblWaitingMessage.setFont(Font.font("Arial", 18));
        lblWaitingMessage.setTextFill(Color.BLACK);

        Label lblServerInfo = new Label("Server Address: " + serverAddress);
        lblServerInfo.setFont(Font.font("Arial", 14));
        lblServerInfo.setTextFill(Color.BLACK);

        Label lblPortInfo = new Label("Port: " + port);
        lblPortInfo.setFont(Font.font("Arial", 14));
        lblPortInfo.setTextFill(Color.BLACK);

        btnStartGame = new Button("Start Game");
        btnStartGame.setOnAction(event -> startGame());

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(lblWaitingMessage, lblServerInfo, lblPortInfo, btnStartGame);

        waitingRoomScene = new Scene(vbox, 300, 200, Color.WHITE);
    }

    public Scene getWaitingRoomScene() {
        return waitingRoomScene;
    }

    private void startGame() {
        if (server.hasPlayers()) {
            // Notify the server to start the game
            server.startGame();
        } else {
            // Show dialog indicating that no players have joined
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Cannot Start Game");
            alert.setHeaderText(null);
            alert.setContentText("No players have joined the room yet. Please wait for at least one player to join.");
            alert.showAndWait().ifPresent(respon -> soundManager.playSoundEffect("button.wav", 1.0));;
        }
    }
}
