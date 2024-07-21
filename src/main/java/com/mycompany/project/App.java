package com.mycompany.project;

import javafx.application.Application;
import javafx.stage.Stage;



public class App extends Application {
    
    private Stage primaryStage;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private RoomScreen roomScreen;
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        menuScreen = new MenuScreen();
        

        primaryStage.setScene(menuScreen.getMenuScene());
        primaryStage.setTitle("Sudoku Game - Menu");
        primaryStage.setResizable(true);
        primaryStage.show();
        
        handleScene();
    }
    private void handleScene(){
        menuScreen.setOnPlayOffline(() -> {
            gameScreen = new GameScreen();
            primaryStage.setScene(gameScreen.getGameScene());
            primaryStage.setTitle("Sudoku Game - Play Offline");
            gameScreen.startTimer();

            gameScreen.setOnBack(() -> {
                primaryStage.setScene(menuScreen.getMenuScene());
                primaryStage.setTitle("Sudoku Game - Menu");
            });
            
        });

        menuScreen.setOnPlayOnline(() -> {
            roomScreen = new RoomScreen(primaryStage);
            primaryStage.setScene(roomScreen.getRoomScene());
            primaryStage.setTitle("Sudoku Game - Play Online");
            
            roomScreen.setOnBack(() ->{
                primaryStage.setScene(menuScreen.getMenuScene());
                primaryStage.setTitle("Sudoku Game - Menu");
            });
        });

        
    }
    public static void main(String[] args) {
            launch(args);
    };
}