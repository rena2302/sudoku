package com.mycompany.project;

import java.net.InetAddress;
import java.util.Optional;
import com.mycompany.project.handler.Client;
import com.mycompany.project.handler.Server;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class RoomScreen {
    public interface onBackListener {
        void onBack();
    }

    private Scene roomScene;
    private VBox mainLayout;
    private Button btnCreateRoom;
    private Button btnJoinRoom;
    private Button btnBack;
    private Label lblTitle;
    private onBackListener onBackListener;
    private String playerName;
    private String roomName;
    private Thread serverThread;
    private boolean isServerRunning = false;
    private Button btnStart;

    @SuppressWarnings("exports")
    public RoomScreen(Stage primaryStage) {
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
        mainLayout = new VBox();
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setSpacing(20);

        updateMainLayoutForHome();
        roomScene = new Scene(mainLayout, 800, 600);
    }

    private void updateMainLayoutForHome() {
        mainLayout.getChildren().clear();

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

        mainLayout.getChildren().addAll(gridPane, hbox);
    }

    private void updateMainLayoutForRoomCreated(String ipString, String playerName) {
        mainLayout.getChildren().clear();

        Label lblRoomCreated = new Label("Room Created");
        lblRoomCreated.setFont(Font.font("Arial", 18));

        Label lblServerIP = new Label("Server IP: " + ipString);
        lblServerIP.setFont(Font.font("Arial", 16));

        Label lblPlayerName = new Label("Host: " + playerName);

        Label lblWaiting = new Label("Waiting for another player...");

        btnStart = new Button("Start");
        btnStart.setDisable(true);

        btnStart.setOnAction(event -> startGame());

        Button btnBackToMenu = new Button("Back to Menu");
        btnBackToMenu.setOnAction(event -> {
            stopServer();
            updateMainLayoutForHome();
        });

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(lblRoomCreated, lblServerIP, lblPlayerName, lblWaiting, btnStart, btnBackToMenu);

        mainLayout.getChildren().add(vbox);
    }

    private void updateMainLayoutForJoinRoom(String hostName) {
        mainLayout.getChildren().clear();

        Label lblJoinRoom = new Label("Joined Room");
        lblJoinRoom.setFont(Font.font("Arial", 18));

        Label lblHostName = new Label("Host: " + hostName);
        lblHostName.setFont(Font.font("Arial", 16));

        Label lblYourName = new Label("You: " + playerName);

        Button btnBack = new Button("Back");
        btnBack.setOnAction(event -> updateMainLayoutForHome());

        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));
        vbox.getChildren().addAll(lblJoinRoom, lblHostName, lblYourName, btnBack);

        mainLayout.getChildren().add(vbox);
    }

    private void setupActions() {
        btnBack.setOnAction(event -> {
            if (onBackListener != null) {
                onBackListener.onBack();
            }
        });

        btnCreateRoom.setOnAction(event -> {
            showCreateRoomDialog();
        });

        btnJoinRoom.setOnAction(event -> joinRoom());
    }

    public void setOnBack(onBackListener listener) {
        this.onBackListener = listener;
    }

    @SuppressWarnings("exports")
    public Scene getRoomScene() {
        return roomScene;
    }

    private void showCreateRoomDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Room");
        dialog.setHeaderText("Enter your name:");
        dialog.setContentText("Player Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(playerName -> {
            this.playerName = playerName;
            createRoom(playerName);
        });
    }

    public void createRoom(String playerName) {
        try {
            serverThread = new Thread(() -> {
                try {
                    Server.main(null);
                    isServerRunning = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Platform.runLater(() -> showAlert("Error", "Failed to create room.", AlertType.ERROR));
                }
            });
            serverThread.start();
            InetAddress ipAddress = InetAddress.getLocalHost();
            updateMainLayoutForRoomCreated(ipAddress.getHostAddress(), playerName);
        } catch (Exception e) {
            showAlert("Error", "Failed to create room.", AlertType.ERROR);
        }
    }

    public void joinRoom() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Join Room");
        dialog.setHeaderText("Enter room details:");
        dialog.setContentText("Enter server IP address:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(serverAddress -> {
            try {
                Client.main(null);
                updateMainLayoutForJoinRoom(serverAddress); // Assuming serverAddress is host name
                showAlert("Connected", "You have joined the room.", AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Error", "Failed to join room.", AlertType.ERROR);
            }
        });
    }

    private void startGame() {
        Platform.runLater(() -> {
            GameScreen gameScreen = new GameScreen(); // Assuming GameScreen is your game screen class
            Stage stage = (Stage) mainLayout.getScene().getWindow();
            stage.setScene(gameScreen.getGameScene());
        });
    }

    private void stopServer() {
        if (serverThread != null && isServerRunning) {
            Server.stop(); // Assuming you have a stop method in Server class
            serverThread.interrupt();
            isServerRunning = false;
        }
    }

    private void showAlert(String title, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
