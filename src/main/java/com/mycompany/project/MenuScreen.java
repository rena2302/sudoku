package com.mycompany.project;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.VBox;


public class MenuScreen {
    public interface OnPlayOfflineListener {
        void onPlayOffline();
    }
    public interface OnPlayOnlineListener {
        void onPlayOnline();
    }
    private Scene scene;
    private VBox menuLayout;
    private OnPlayOfflineListener onPlayOfflineListener;
    private OnPlayOnlineListener onPlayOnlineListener;
    private Button playOnlineButton;
    private Button playOfflineButton;
    private Button settingsButton;
    public MenuScreen() {
        // Tạo các button
        playOnlineButton = new Button("Play Online");
        playOfflineButton = new Button("Play Offline");
        settingsButton = new Button("Settings");

        // Sắp xếp các button vào một VBox
        menuLayout = new VBox(10);
        menuLayout.getChildren().addAll(playOnlineButton, playOfflineButton, settingsButton);
        menuLayout.setPrefSize(300, 200);
        menuLayout.setStyle("-fx-padding: 10px; -fx-alignment: center");

        Image image = new Image("file:./src/main/java/com/mycompany/project/image/images.jpg");
        image.errorProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                System.err.println("Error loading image: " + image.getUrl());
            }
        });
        BackgroundImage backgroundImage = new BackgroundImage(image, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.DEFAULT, 
                BackgroundSize.DEFAULT);

        menuLayout.setBackground(new Background(backgroundImage));
        // Tạo Scene chứa menuLayout
        scene = new Scene(menuLayout);

        handleButton();
    }

    public void setOnPlayOffline(OnPlayOfflineListener listener) {
        this.onPlayOfflineListener = listener;
    }
    public void setOnPlayOnline(OnPlayOnlineListener listener) {
        this.onPlayOnlineListener = listener;
    }
    private void handleButton() {
         // Xử lý sự kiện khi nhấn các button (nếu cần)
         playOnlineButton.setOnAction(event -> {
            if(onPlayOnlineListener != null){
                onPlayOnlineListener.onPlayOnline();
            }
        });
        playOfflineButton.setOnAction(event -> {
            if (onPlayOfflineListener != null) {
                onPlayOfflineListener.onPlayOffline();
            }
        });
        settingsButton.setOnAction(e -> System.out.println("setting clicked"));
    }

    // Method để trả về Scene của menu
    @SuppressWarnings("exports")
    public Scene getMenuScene() {
        return scene;
    }
}
