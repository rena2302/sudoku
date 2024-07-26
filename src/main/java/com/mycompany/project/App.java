package com.mycompany.project;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;



public class App extends Application {
    
    private Stage primaryStage;
    private MenuScreen menuScreen;
    private GameScreen gameScreen;
    private RoomScreen roomScreen;
    private PlayerScoreScene playerScoreScene;
    private RegisterAndLogin registerAndLogin;
    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        registerAndLogin = new RegisterAndLogin();

        primaryStage.setScene(registerAndLogin.getScene());
        primaryStage.setTitle("Sudoku Game - Register Login");
        primaryStage.setResizable(true);
        primaryStage.show();

        addSizeChangeListener();
        handleRegisterLoginScene();
    }
    
    private void centerWindow() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((screenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((screenBounds.getHeight() - primaryStage.getHeight()) / 2);
    }

    private void addSizeChangeListener() {
        ChangeListener<Number> sizeChangeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                centerWindow();
            }
        };

        primaryStage.widthProperty().addListener(sizeChangeListener);
        primaryStage.heightProperty().addListener(sizeChangeListener);
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
            primaryStage.setOnShowing(event -> centerWindow());
            primaryStage.sizeToScene();
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

        menuScreen.setOnRecord(() ->{
            playerScoreScene = new PlayerScoreScene(registerAndLogin.getEmailFromLocalStorage());
            
            primaryStage.setScene(playerScoreScene.getScene());
            primaryStage.setTitle("Sudoku Game - Record");
        });
        
    }
    public static void main(String[] args) {
            launch(args);
    };
}