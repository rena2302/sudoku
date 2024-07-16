package com.mycompany.project;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MenuScreen {
    public interface OnPlayOfflineListener {
        void onPlayOffline();
    }
    private Scene scene;
    private VBox menuLayout;
    private OnPlayOfflineListener onPlayOfflineListener;
    
    public MenuScreen() {
        // Tạo các button
        Button playOnlineButton = new Button("Play Online");
        Button playOfflineButton = new Button("Play Offline");
        Button settingsButton = new Button("Settings");

        // Xử lý sự kiện khi nhấn các button (nếu cần)
        playOnlineButton.setOnAction(e -> handlePlayOnline());
        playOfflineButton.setOnAction(event -> {
            if (onPlayOfflineListener != null) {
                onPlayOfflineListener.onPlayOffline();
            }
        });
        settingsButton.setOnAction(e -> handleSettings());

        // Sắp xếp các button vào một VBox
        menuLayout = new VBox(10);
        menuLayout.getChildren().addAll(playOnlineButton, playOfflineButton, settingsButton);
        menuLayout.setPrefSize(300, 200);
        menuLayout.setStyle("-fx-background-color: lightblue; -fx-padding: 10px; -fx-alignment: center");

        // Tạo Scene chứa menuLayout
        scene = new Scene(menuLayout);
    }

    private void handlePlayOnline() {
        // Xử lý khi nhấn nút Play Online
        System.out.println("Play Online button clicked");
        // Điều hướng sang màn hình play online
        // Ví dụ:
        // OnlineScreen onlineScreen = new OnlineScreen();
        // primaryStage.setScene(onlineScreen.getScene());
    }

    public void setOnPlayOffline(OnPlayOfflineListener listener) {
        this.onPlayOfflineListener = listener;
    }

    private void handleSettings() {
        // Xử lý khi nhấn nút Settings
        System.out.println("Settings button clicked");
        // Điều hướng sang màn hình settings
        // Ví dụ:
        // SettingsScreen settingsScreen = new SettingsScreen();
        // primaryStage.setScene(settingsScreen.getScene());
    }

    // Method để trả về Scene của menu
    @SuppressWarnings("exports")
    public Scene getMenuScene() {
        return scene;
    }
}