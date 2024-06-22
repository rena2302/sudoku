package com.mycompany.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application {
    @Override
    public void start(Stage stage) {
        BorderPane bP=new BorderPane();
        
        Button btnNewGame=new Button("New Game");
        Button btnSize=new Button("Size");
        Button btnMode=new Button("Mode Play");
        Group grHeaderBtn = new Group(btnMode, btnNewGame, btnSize);
        HBox hHeaderBtn=new HBox(10);
        hHeaderBtn.setAlignment(Pos.TOP_CENTER);
        hHeaderBtn.getChildren().addAll(btnNewGame, btnMode, btnSize);

        Label lblScore=new Label("Score:   ");
        Label lblTime=new Label("Timer:   ");
        Group grHeaderLbl=new Group(lblScore, lblTime);
        HBox hHeaderlbl=new HBox(50);
        hHeaderlbl.setAlignment(Pos.TOP_LEFT);
        hHeaderlbl.getChildren().addAll(lblScore, lblTime);

        Button btnHint=new Button("Hint");
        Button btnDelete=new Button("Delete");
        Button btnUndo=new Button("Undo");
        Group grBottomBtn=new Group(btnDelete, btnHint, btnUndo);
        HBox hBottom=new HBox(grBottomBtn);
        hBottom.setAlignment(Pos.BOTTOM_CENTER);

        GridPane gridP=new GridPane();
        gridP.setHgap(30);
        gridP.setVgap(30);
        gridP.setPadding(new Insets(10));
        TextField[][] textFields=new TextField[9][9];
        for (int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++){
                TextField textField=new TextField();
                textField.setPrefHeight(40);
                textField.setPrefWidth(40);
                textField.setAlignment(Pos.CENTER);
                gridP.add(textField, i, j);
                textFields[i][j]=textField;
            }
        }

        VBox headerBox=new VBox(10);
        headerBox.getChildren().addAll(hHeaderBtn, hHeaderlbl);
        bP.setTop(headerBox);
        bP.setCenter(gridP);

        Scene scene=new Scene(bP, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

     public static void main(String[] args) {
            launch(args);
        
        };
}