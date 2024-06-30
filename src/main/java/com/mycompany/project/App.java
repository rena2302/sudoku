package com.mycompany.project;

import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class App extends Application {
    private static final int SIZE=9;
    private static final int SUBGRID_SIZE=3;
    private static final int CELL_SIZE=50;

    private Button btnEasy;
    private Button btnMed;
    private Button btnHard;
    private Button btnEx;
    private Button btnMas;
    private Button btnEXtr;

    private Label lblMis;
    private Label lblScore;
    private Label lblTime;

    private TextField[][] cells = new TextField[SIZE][SIZE];
    private GridPane suGrid;
    private SudokuPanel sudokuPanel = new SudokuPanel();

    private VBox maincontainer;
    private HBox root;

    private int secondsPassed = 0;
    private boolean timerIsRunning = false;
    private Timeline timer;

    public GridPane getSuGrid() {
        return suGrid;
    }

    public void setSuGrid(GridPane suGrid) {
        this.suGrid = suGrid;
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
            numberButton.setOnAction(event -> sudokuPanel.handleButtonClick(numberButton.getText()));
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

        btnEasy.setOnAction(event -> showConfirmationDialog("Easy"));
        btnMed.setOnAction(event -> showConfirmationDialog("Medium"));
        btnHard.setOnAction(event -> showConfirmationDialog("Hard"));
        btnEx.setOnAction(event -> showConfirmationDialog("Expert"));
        btnMas.setOnAction(event -> showConfirmationDialog("Master"));
        btnEXtr.setOnAction(event -> showConfirmationDialog("Extremely"));
        
        header.getChildren().addAll(mode, btnEasy, btnMed, btnHard, btnEx, btnMas, btnEXtr);
        return header;
    }

    @Override
    public void start(Stage stage) {
        suGrid = sudokuPanel.createSudokuGrid();
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
    
        root=new HBox(20, suGrid, controlHeader, controlPanel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        maincontainer=new VBox(20, controlHeader, root, hHeaderlbl);

        maincontainer.setPadding(new Insets(20));
        maincontainer.setAlignment(Pos.CENTER);

        VBox.setMargin(controlHeader, new Insets(0, 0, 0, 0));

        Button btnToggleFullscreen=new Button("Fullscreen");
        btnToggleFullscreen.setOnAction(e -> {
            stage.setFullScreen(!stage.isFullScreen());
        });

        maincontainer.getChildren().add(btnToggleFullscreen);
        VBox.setMargin(btnToggleFullscreen, new Insets(0, 0, 0, 0));

        initializeTimer();
        startTimer();
        Scene scene=new Scene(maincontainer, 800, 600);
        stage.setTitle("SUDOKU");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        sudokuPanel.setAppUI(this);
        
    }


    //Handle Event
    private void showConfirmationDialog(String mode) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Change Puzzle Mode");
        alert.setHeaderText("Change Puzzle Mode to " + mode);
        alert.setContentText("Are you sure you want to change the puzzle mode? All current data will be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Clear current data and generate new puzzle based on mode
                rebuildMode(mode);
            }
        });
    }

    private void initializeTimer() {
        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            updateTimerDisplay();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
    }

    public void startTimer() {
        if (!timerIsRunning) {
            timer.play();
            timerIsRunning = true;
        }
    }

    public void pauseTimer() {
        if (timerIsRunning) {
            timer.stop();
            timerIsRunning = false;
        }
    }

    public void resumeTimer() {
        if (!timerIsRunning) {
            startTimer();
        }
    }

    public void resetTimer() {
        pauseTimer();
        timerIsRunning = false;
        secondsPassed = 0;
        updateTimerDisplay();
    }

    private void updateTimerDisplay() {
        int minutes = secondsPassed / 60;
        int seconds = secondsPassed % 60;
        String timeFormatted = String.format("Timer: %02d:%02d", minutes, seconds);
        lblTime.setText(timeFormatted);
    }
    public void rebuildInterface(){
        suGrid.getChildren().clear(); // Xóa hết các thành phần trong gridP
        // Tạo lại lưới Sudoku
        suGrid = sudokuPanel.createSudokuGrid();
        root.getChildren().remove(0);
        root.getChildren().add(0, suGrid);
        // Cập nhật lại các thành phần điều khiển khác nếu cần
        // Đặt lại gridP vào giao diện người dùng
    }
    public void rebuildMode(String mode){
        SudokuPuzzle newPuzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.NINEBYNINE, mode);
        sudokuPanel.newSudokuPuzzle(newPuzzle);
        suGrid.getChildren().clear(); // Xóa hết các thành phần trong gridP
        // Tạo lại lưới Sudoku
        suGrid = sudokuPanel.createSudokuGrid();
        root.getChildren().remove(0);
        root.getChildren().add(0, suGrid);
    }
    public void updateMistakeLabel(int mistakes) {
        lblMis.setText("Mistakes: " + mistakes + "/3");
    }
    public static void main(String[] args) {
            launch(args);
        
    };
}