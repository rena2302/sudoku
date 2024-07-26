package com.mycompany.project.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.project.App;

public class Server {
    private ServerSocket serverSocket;
    private List<Socket> clients;

    public void start() {
        clients = new ArrayList<>();
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Server started on port 12345");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                clients.add(clientSocket);
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                
                // Start a new thread to handle the client
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
            //ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            
            while (true) {
                String message = (String) in.readObject();
                if (message.startsWith("completed_game:")) {
                    // Forward the completed game message to all clients
                    notifyAllClients(message);
                }
                // Handle other messages if necessary
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Clean up and remove the client
            clients.remove(clientSocket);
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyAllClients(String message) {
        for (Socket client : clients) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject(message);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasPlayers() {
        return !clients.isEmpty();
    }

    public void startGame() {
        // Notify all clients to start the game
        for (Socket client : clients) {
            try {
                ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
                out.writeObject("start_game");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Game started by the host.");
        // Implement game start logic for the host
        App.getInstance().startGame();
    }

    public void stop() throws IOException {
        for (Socket client : clients) {
            client.close();
        }
        serverSocket.close();
    }
}

