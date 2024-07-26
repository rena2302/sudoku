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
    private ObjectInputStream in;
    public static Client getClient(){
        return insClient;
    }
    public void connect(String host, int port) {
        try {
            insClient = this;
            socket = new Socket(host, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Start listening for messages
            new Thread(this::listenForMessages).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listenForMessages() {
        try {
            while (true) {
                String message = (String) in.readObject();
                System.out.println(message);
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

    public void sendMessage(String message) {
        try {
            out.writeObject(message);
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
        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Clear current data and generate new puzzle based on mode
                Client.getClient().disconnect();
                sendMessage("shutdown");
                App.getInstance().returnToMainMenu();
            }
        });
    }
}
