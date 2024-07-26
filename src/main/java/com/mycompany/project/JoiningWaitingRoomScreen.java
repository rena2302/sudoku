package com.mycompany.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class JoiningWaitingRoomScreen {
    private Scene waitingRoomScene;

    public JoiningWaitingRoomScreen(String serverAddress, int port) {
        Label lblWaitingMessage = new Label("Waiting for the host to start the game...");
        lblWaitingMessage.setFont(Font.font("Arial", 18));
        lblWaitingMessage.setTextFill(Color.BLACK);

        Label lblServerInfo = new Label("Server Address: " + serverAddress);
        lblServerInfo.setFont(Font.font("Arial", 14));
        lblServerInfo.setTextFill(Color.BLACK);

        Label lblPortInfo = new Label("Port: " + port);
        lblPortInfo.setFont(Font.font("Arial", 14));
        lblPortInfo.setTextFill(Color.BLACK);

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(lblWaitingMessage, lblServerInfo, lblPortInfo);

        waitingRoomScene = new Scene(vbox, 300, 200, Color.WHITE);
    }

    public Scene getWaitingRoomScene() {
        return waitingRoomScene;
    }
}
