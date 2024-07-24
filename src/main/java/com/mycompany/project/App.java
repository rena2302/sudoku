package com.mycompany.project;

import javafx.application.Application;
import javafx.stage.Stage;



public class App extends Application {
    
    private Stage primaryStage;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private RoomScreen roomScreen;
    private RegisterAndLogin registerAndLogin;
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        registerAndLogin = new RegisterAndLogin();

        primaryStage.setScene(registerAndLogin.getScene());
        primaryStage.setTitle("Sudoku Game - Register Login");
        primaryStage.setResizable(true);
        primaryStage.show();
        
        handleRegisterLoginScene();
    }
    private void handleRegisterLoginScene(){
        registerAndLogin.setOnLoginSuccess(event -> {
            menuScreen = new MenuScreen();
            primaryStage.setScene(menuScreen.getMenuScene());
            primaryStage.setTitle("Sudoku Game - Menu");
            handleMenuScene();
        });
    }
    private void handleMenuScene(){
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
            roomScreen = new RoomScreen();

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