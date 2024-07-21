package com.mycompany.project.handler;

// Client.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static String SERVER_ADDRESS = "192.168.101.167";
    private static final int PORT = 12345;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public static void main(String[] args) {
        new Client().start();
    }
    public void start() {
        try {
            socket = new Socket(SERVER_ADDRESS, PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            Scanner scanner = new Scanner(System.in);

            // Thread to read messages from server
            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        System.out.println("Received from server: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            // Read user input and send it to server
            while (scanner.hasNextLine()) {
                String message = scanner.nextLine();
                out.println(message);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
