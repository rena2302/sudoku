package com.mycompany.project;

import javafx.application.Application;
import javafx.stage.Stage;



public class App extends Application {
    
    private Stage primaryStage;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        menuScreen = new MenuScreen();
        gameScreen = new GameScreen();

        primaryStage.setScene(menuScreen.getMenuScene());
        primaryStage.setTitle("Sudoku Game - Menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        
        handleScene();
    }
    private void handleScene(){
        menuScreen.setOnPlayOffline(() -> {
            primaryStage.setScene(gameScreen.getMenuScene());
            primaryStage.setTitle("Sudoku Game - Play Offline");
            gameScreen.startTimer();
        });
    }
    public static void main(String[] args) {
            launch(args);
    };
}