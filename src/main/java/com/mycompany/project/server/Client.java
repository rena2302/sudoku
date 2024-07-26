package com.mycompany.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.mycompany.project.App;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Client {
    private static Client insClient;
    private Socket socket;
    private ObjectOutputStream out;

    public static Client getClient(){
        return insClient;
    }
    public void connect(String serverAddress, int port) {
        try {
            insClient = this ;
            socket = new Socket(serverAddress, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            System.out.println("Connected to server");

            // Start listening for messages from the server
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            while (true) {
                String message = (String) in.readObject();
                if (message.equals("start_game")) {
                    // Transition to game screen
                    Platform.runLater(() -> App.getInstance().startGame());
                } else if (message.startsWith("completed_game:")) {
                    // Show the message that a player has completed the game
                    String playerName = message.split(":")[1];
                    Platform.runLater(() -> showAlert(playerName + " has completed the game!"));
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void sendGameCompleted(String playerName) {
        try {
            out.writeObject("completed_game:" + playerName);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Update");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
