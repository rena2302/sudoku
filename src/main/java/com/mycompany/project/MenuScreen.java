package com.mycompany.project;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;


public class MenuScreen {
    public interface OnPlayOfflineListener {
        void onPlayOffline();
    }
    public interface OnPlayOnlineListener {
        void onPlayOnline();
    }
    public interface OnRedcordListener {
        void onRedcord();
    }
    private Scene scene;
    private VBox menuLayout;
    private OnPlayOfflineListener onPlayOfflineListener;
    private OnPlayOnlineListener onPlayOnlineListener;
    private OnRedcordListener onRedcordListener;
    public MenuScreen() {
        // Tạo các button
        Button howtoplaybtn=new Button("How to play");
        Button playOnlineButton = new Button("Play Online");
        Button playOfflineButton = new Button("Play Offline");
        Button viewScoreButton = new Button("Records");

        // Xử lý sự kiện khi nhấn các button (nếu cần)
        howtoplaybtn.setOnAction(event -> showHowToPlay());
        playOnlineButton.setOnAction(event -> {
            if(onPlayOnlineListener != null){
                onPlayOnlineListener.onPlayOnline();
            }
        });
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
        viewScoreButton.setOnAction(event -> {
            if(onRedcordListener != null){
                onRedcordListener.onRedcord();
            }
        });

        // Sắp xếp các button vào một VBox
        menuLayout = new VBox(10);
        menuLayout.getChildren().addAll(howtoplaybtn, playOnlineButton, playOfflineButton, viewScoreButton);
        menuLayout.setPrefSize(300, 200);
        menuLayout.setStyle("-fx-background-image: url('/images.png');" +
            "-fx-background-size: cover;" +
            "-fx-padding: 10px;" +
            "-fx-alignment: center");

        // Tạo Scene chứa menuLayout
        scene = new Scene(menuLayout);

        // Bind font sizes to scene width
        NumberBinding fontSizeBinding = Bindings.divide(scene.widthProperty(), 25);

        // Apply the font size binding to buttons
        howtoplaybtn.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));
        playOnlineButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));
        playOfflineButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));
        viewScoreButton.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));

        // Bind spacing to scene height
        NumberBinding spacingBinding = Bindings.divide(scene.heightProperty(), 25);
        menuLayout.spacingProperty().bind(spacingBinding);
    }
    
    private void showHowToPlay() {
        // Tạo và hiển thị thông báo hướng dẫn cách chơi
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("How to Play");
        alert.setHeaderText(null);
        alert.setContentText("Sudoku is a logic-based, combinatorial number-placement puzzle. The objective is to fill a 9×9 grid with digits so that each column, each row, and each of the nine 3×3 subgrids that compose the grid (also called \"boxes\", \"blocks\", or \"regions\") contains all of the digits from 1 to 9. The puzzle setter provides a partially completed grid, which typically has a unique solution.");

        alert.showAndWait();
    }

    public void setOnPlayOffline(OnPlayOfflineListener listener) {
        this.onPlayOfflineListener = listener;
    }

    public void setOnPlayOnline(OnPlayOnlineListener listener) {
        this.onPlayOnlineListener = listener;
    }

    public void setOnRecord(OnRedcordListener listener){
        this.onRedcordListener = listener;
    }

    // Method để trả về Scene của menu
    public Scene getMenuScene() {
        return scene;
    }
}
