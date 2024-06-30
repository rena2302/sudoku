package com.mycompany.project;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class App extends Application {
    private static final int SIZE=9;
    private static final int SUBGRID_SIZE=3;
    private static final int CELL_SIZE=60;

    private Button btnEasy;
    private Button btnMed;
    private Button btnHard;
    private Button btnEx;
    private Button btnMas;
    private Button btnEXtr;

    private Label lblMis;
    private Label lblScore;
    private Label lblTime;

    private TextField textField;
    private GridPane gridP;
    
    private GridPane createSudokuGrid(){
        gridP=new GridPane();
        gridP.setGridLinesVisible(true);
        for(int i = 0; i < SIZE; i++){
            for(int j = 0; j < SIZE; j++){
                StackPane cell=createCell(i, j);
                gridP.add(cell, j, i);
            }
        }
        return gridP;
    }

    private StackPane createCell(int i, int j){
        StackPane stackP=new StackPane();
        textField=new TextField();
        textField.setPrefSize(CELL_SIZE, CELL_SIZE);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font(20));
        textField.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.5;");
        if((i / SUBGRID_SIZE + j /SUBGRID_SIZE) % 2 == 0 ) {
            textField.setStyle("-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 0.5;");
        }
        stackP.getChildren().add(textField);
        return stackP;
    }

    private VBox createControlPanel(){
        VBox controlPanel=new VBox(20);
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setPrefWidth(200);

        Button btnUndo=new Button("Undo");
        Button btnDelete=new Button("Delete");
        Button btnNote=new Button("Note");
        Button btnHint=new Button("Hint");

        btnUndo.setPrefWidth(100);
        btnDelete.setPrefWidth(100);
        btnNote.setPrefWidth(100);
        btnHint.setPrefWidth(100);

        GridPane numberPad=createNumberPad();
        controlPanel.getChildren().add(numberPad);
        
        Button btnNew=new Button("NEW GAME");
        btnNew.setPrefWidth(200);

        controlPanel.getChildren().addAll(btnUndo, btnDelete, btnNote, btnHint, btnNew);
        return controlPanel;
    }

    private GridPane createNumberPad(){
        GridPane numberPad = new GridPane();
        numberPad.setHgap(10);
        numberPad.setVgap(10);

        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefSize(50, 50);
            numberButton.setFont(Font.font(18));
            numberPad.add(numberButton, (i - 1) % 3, (i - 1) / 3);
        }
        return numberPad;
    }

    private HBox createControlHeader(){
        HBox header=new HBox(20);
        header.setAlignment(Pos.TOP_LEFT);

        Label mode=new Label("Mode:");
        btnEasy=new Button("Easy");
        btnMed=new Button("Medium");
        btnHard=new Button("Hard");
        btnEx=new Button("Expert");
        btnMas=new Button("Master");
        btnEXtr=new Button("Extremely");

        btnEasy.setPrefWidth(100);
        btnMed.setPrefWidth(100);
        btnHard.setPrefWidth(100);
        btnEx.setPrefWidth(100);
        btnMas.setPrefWidth(100);
        btnEXtr.setPrefWidth(100);

        header.getChildren().addAll(mode, btnEasy, btnMed, btnHard, btnEx, btnMas, btnEXtr);
        return header;
    }

    @Override
    public void start(Stage stage) {
        GridPane suGrid= createSudokuGrid();
        VBox controlPanel=createControlPanel();
        HBox controlHeader=createControlHeader();
        
        lblMis=new Label("Mistake: 0/3");
        lblScore=new Label("Score: 0");
        lblTime=new Label("Timer: 00:00");
        //Group grHeaderLbl=new Group(lblScore, lblTime);
        HBox hHeaderlbl=new HBox(50);
        hHeaderlbl.setAlignment(Pos.TOP_RIGHT);
        hHeaderlbl.getChildren().addAll(lblMis, lblScore, lblTime);
        hHeaderlbl.setMargin(lblMis, new Insets(0, 50, 0, 0));
        hHeaderlbl.setMargin(lblScore, new Insets(0, 50, 0, 0));
        hHeaderlbl.setMargin(lblTime, new Insets(0, 70, 0, 0));
    
        HBox root=new HBox(20, suGrid, controlHeader, controlPanel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        VBox maincontainer=new VBox(20, controlHeader, root, hHeaderlbl);

        maincontainer.setPadding(new Insets(20));
        maincontainer.setAlignment(Pos.CENTER);

        VBox.setMargin(controlHeader, new Insets(0, 0, 0, 0));

        Button btnToggleFullscreen=new Button("Fullscreen");
        btnToggleFullscreen.setOnAction(e -> {
            stage.setFullScreen(!stage.isFullScreen());
        });

        maincontainer.getChildren().add(btnToggleFullscreen);
        VBox.setMargin(btnToggleFullscreen, new Insets(0, 0, 0, 0));

        Scene scene=new Scene(maincontainer, 800, 600);
        stage.setTitle("SUDOKU");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


    //Handle Event
    public void updateTimerLabel(String time) {
        lblTime.setText(time);
    }
     public static void main(String[] args) {
            launch(args);
        
    };
}