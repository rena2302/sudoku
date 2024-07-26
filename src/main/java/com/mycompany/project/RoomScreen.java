package com.mycompany.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

import com.mycompany.project.server.Client;
import com.mycompany.project.server.Server;

public class RoomScreen {
    public interface onBackListener {
        void onBack();
    }

    private Scene roomScene;
    private Button btnCreateRoom;
    private Button btnJoinRoom;
    private Button btnBack;
    private Label lblTitle;
    private TextField txtServerAddress;
    private TextField txtPort;
    private onBackListener onBackListener;
    private Server server; // Instance of the server
    private Client client; // Instance of the client

    public RoomScreen() {
        initializeComponents();
        setupLayout();
        setupActions();
        
        roomScene.setFill(Color.WHITE);
    }

    private void initializeComponents() {
        btnCreateRoom = new Button("Create Room");
        btnJoinRoom = new Button("Join Room");
        btnBack = new Button("Back to Menu");
        
        lblTitle = new Label("Sudoku Online");
        lblTitle.setFont(Font.font("Arial", 24));

        txtServerAddress = new TextField("localhost"); // Default to localhost
        txtPort = new TextField("12345"); // Default port
    }

    private void setupLayout() {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(40));
        
        gridPane.add(lblTitle, 0, 0, 2, 1);
        gridPane.add(btnCreateRoom, 0, 1);
        gridPane.add(btnJoinRoom, 1, 1);
        gridPane.add(new Label("Server Address:"), 0, 2);
        gridPane.add(txtServerAddress, 1, 2);
        gridPane.add(new Label("Port:"), 0, 3);
        gridPane.add(txtPort, 1, 3);
        
        HBox hbox = new HBox();
        hbox.getChildren().add(btnBack);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(gridPane, hbox);
        vbox.setAlignment(Pos.CENTER);
        
        vbox.setStyle(
            "-fx-background-image: url('/snow.jpg');" +
            "-fx-background-size: cover;"
        );
        
        roomScene = new Scene(vbox, 300, 300);
    }

    private void setupActions() {
        btnBack.setOnAction(event -> {
            if (onBackListener != null) {
                onBackListener.onBack();
            }
        });
        
        btnCreateRoom.setOnAction(event -> {
            createRoom();
        });
        
        btnJoinRoom.setOnAction(event -> {
            joinRoom();
        });
    }
    
    public void setOnBack(onBackListener listener) {
        this.onBackListener = listener;
    }

    public Scene getRoomScene() {
        return roomScene;
    }
    
    public void createRoom() {
        String serverAddress = txtServerAddress.getText();
        int port = Integer.parseInt(txtPort.getText());
        // Initialize and start the server in a new thread
        System.out.println("Creating a new room...");
        server = new Server(); // Create server instance
        new Thread(() -> {
            server.start(); // Start the server
        }).start();

        // Show waiting room screen
        WaitingRoomScreen waitingRoomScreen = new WaitingRoomScreen(serverAddress, port, server);
        Stage primaryStage = (Stage) roomScene.getWindow();
        primaryStage.setScene(waitingRoomScreen.getWaitingRoomScene());
    }
    
    public void joinRoom() {
        String serverAddress = txtServerAddress.getText();
        int port = Integer.parseInt(txtPort.getText());
        
        client = new Client(); // Create client instance
        client.connect(serverAddress, port); // Connect to the server
        System.out.println("Joined the room.");
        // Additional logic to handle post-connection behavior can be added here

        JoiningWaitingRoomScreen joiningWaitingRoomScreen = new JoiningWaitingRoomScreen(serverAddress, port);
        Stage primaryStage = (Stage) roomScene.getWindow();
        primaryStage.setScene(joiningWaitingRoomScreen.getWaitingRoomScene());
    }

    public void stopServer() {
        if (server != null) {
            try {
                server.stop(); // Stop the server
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

