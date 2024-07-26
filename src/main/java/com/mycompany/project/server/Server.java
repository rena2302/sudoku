package com.mycompany.project.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.mycompany.project.App;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class Server {
    private static Server inServer;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients;

    public void start() {
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started on port 12345");
            inServer = this;
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                clients.add(clientHandler);
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Start a new thread to handle the client
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            for (ClientHandler client : clients) {
                client.close();
            }
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void notifyAllClients(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public void notifyGameCompletion(String message) {
        notifyAllClients(message);
        String playerName = message.split(":")[1];
        Platform.runLater(() -> showAlert(playerName + " has completed the game!"));
    }

    public boolean hasPlayers() {
        return !clients.isEmpty();
    }

    public void startGame() {
        // Notify all clients to start the game
        for (ClientHandler client : clients) {
            client.sendMessage("start_game");
        }
        System.out.println("Game started by the host.");
        // Implement game start logic for the host
        App.getInstance().startGame();
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
                shutdown();
                App.getInstance().returnToMainMenu();
            }
        });
    }
    
    public static Server getServer(){
        return inServer;
    }
    private class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;

        public ClientHandler(Socket socket) throws IOException {
            this.clientSocket = socket;
            this.out = new ObjectOutputStream(clientSocket.getOutputStream());
            this.in = new ObjectInputStream(clientSocket.getInputStream());
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String message = (String) in.readObject();
                    System.out.println(message);
                    if (message.startsWith("completed_game:")) {
                        // Forward the completed game message to all clients
                        notifyAllClients(message);
                    } else if (message.equals("shutdown")){
                        shutdown();
                    }
                    // Handle other messages if necessary
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                close();
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

        public void close() {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            clients.remove(this);
        }
    }
}


