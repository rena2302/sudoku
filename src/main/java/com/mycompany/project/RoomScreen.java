package com.mycompany.project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class RoomScreen {
    public interface onBackListener{
        void onBack();
    }
    private Scene roomScene;
    
    private Button btnCreateRoom;
    private Button btnJoinRoom;
    private Button btnBack;
    private Label lblTitle;
    private onBackListener onBackListener;
    
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
        
        HBox hbox = new HBox();
        hbox.getChildren().add(btnBack);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPadding(new Insets(20));
        
        VBox vbox = new VBox();
        vbox.getChildren().addAll(gridPane, hbox);
        vbox.setAlignment(Pos.CENTER);
        
        roomScene = new Scene(vbox, 800, 600);
    }
    
    private void setupActions() {
        btnBack.setOnAction(event -> {
            if(onBackListener != null){
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
    public void setOnBack(onBackListener listener){
        this.onBackListener = listener;
    }

    public Scene getRoomScene() {
        return roomScene;
    }
    
    public void createRoom() {
        // Logic to handle creating a room
        System.out.println("Creating a new room...");
        // Example: You can implement your logic here
    }
    
    public void joinRoom() {
        // Logic to handle joining a room by ID
        System.out.println("Joining an existing room...");
        // Example: You can implement your logic here
    }
}
