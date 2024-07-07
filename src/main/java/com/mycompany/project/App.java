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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;


public class App extends Application {
    private String mode = "Medium";

    private Button btnEasy;
    private Button btnMed;
    private Button btnHard;
    private Button btnEx;
    private Button btnMas;
    private Button btnEXtr;

    private Button btnUndo;
    private Button btnDelete;
    private Button btnNote;
    private Button btnHint;
    private Button btnNew;

    private Label lblMis;
    private Label lblScore;
    private Label lblTime;
    private Label lblHint;

    private GridPane suGrid;
    private SudokuPanel sudokuPanel = new SudokuPanel();

    private AnchorPane maincontainer;
    private HBox root;

    private int secondsPassed = 0;
    private boolean timerIsRunning = false;
    private Timeline timer;

    private VBox createControlPanel() {
        VBox controlPanel = new VBox(20);
        controlPanel.setAlignment(Pos.TOP_CENTER);
        controlPanel.setPrefWidth(200);
    
        btnUndo = new Button("Undo");
        btnDelete = new Button("Delete");
        btnNote = new Button("Note");
        btnHint = new Button("Hint");
        btnNew = new Button("NEW GAME");
    
        btnNew.setPrefWidth(100);
        btnUndo.setPrefWidth(100);
        btnDelete.setPrefWidth(100);
        btnNote.setPrefWidth(100);
        btnHint.setPrefWidth(100);
    
        // Thiết lập nền màu theo mã màu RGB và opacity là 1.000 (100%)
        String buttonStyle = "-fx-background-color: rgba(234, 238, 244, 1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;";
    
        btnNew.setStyle(buttonStyle);
        btnUndo.setStyle(buttonStyle);
        btnDelete.setStyle(buttonStyle);
        btnNote.setStyle(buttonStyle);
        btnHint.setStyle(buttonStyle);
    
        GridPane numberPad = createNumberPad();
        controlPanel.getChildren().add(numberPad);
    
        controlPanel.getChildren().addAll(btnNew, btnUndo, btnDelete, btnNote, btnHint);
        return controlPanel;
    }    

    private GridPane createNumberPad() {
        GridPane numberPad = new GridPane();
        numberPad.setHgap(3);
        numberPad.setVgap(3);
    
        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefSize(50, 50);
            numberButton.setFont(Font.font(18));
    
            // Thiết lập nền màu xanh biển nhạt và chữ màu xanh biển đậm
            numberButton.setStyle("-fx-background-color: rgba(234,238,244,1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;");
    
            numberButton.setOnAction(event -> sudokuPanel.handleButtonClick(numberButton.getText()));
            numberPad.add(numberButton, (i - 1) % 3, (i - 1) / 3);
        }
    
        return numberPad;
    }

    private HBox createControlHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
    
        Label mode = new Label("Mode:");
        btnEasy = new Button("Easy");
        btnMed = new Button("Medium");
        btnHard = new Button("Hard");
        btnEx = new Button("Expert");
        btnMas = new Button("Master");
        btnEXtr = new Button("Extremely");
    
        btnEasy.setPrefWidth(100);
        btnMed.setPrefWidth(100);
        btnHard.setPrefWidth(100);
        btnEx.setPrefWidth(100);
        btnMas.setPrefWidth(100);
        btnEXtr.setPrefWidth(100);
    
        // Thiết lập nền màu và chữ màu xanh biển nhạt
        String buttonStyle = "-fx-background-color: rgba(234, 238, 244, 1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;";

        mode.setStyle("-fx-text-fill: darkblue;");
        mode.setFont(Font.font(buttonStyle, FontWeight.BOLD, 13));
        btnEasy.setStyle(buttonStyle);
        btnMed.setStyle(buttonStyle);
        btnHard.setStyle(buttonStyle);
        btnEx.setStyle(buttonStyle);
        btnMas.setStyle(buttonStyle);
        btnEXtr.setStyle(buttonStyle);
    
        header.getChildren().addAll(mode, btnEasy, btnMed, btnHard, btnEx, btnMas, btnEXtr);
        return header;
    }
    

    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        suGrid = sudokuPanel.createSudokuGrid();
        VBox controlPanel = createControlPanel();
        HBox controlHeader = createControlHeader();

        lblMis = new Label("Mistake: 0/3");
        lblScore = new Label("Score: 0");
        lblTime = new Label("Timer: 00:00");
        lblHint = new Label("Hint: 0/5");

        // Thiết lập màu chữ xanh
        String labelStyle = "-fx-text-fill: darkblue;";
        lblMis.setStyle(labelStyle);
        lblScore.setStyle(labelStyle);
        lblTime.setStyle(labelStyle);
        lblHint.setStyle(labelStyle);
        
        lblMis.setFont(Font.font(labelStyle, FontWeight.BOLD, 14));
        lblScore.setFont(Font.font(labelStyle, FontWeight.BOLD, 14));
        lblTime.setFont(Font.font(labelStyle, FontWeight.BOLD, 14));
        lblHint.setFont(Font.font(labelStyle, FontWeight.BOLD, 14));

        HBox hFooterlbl = new HBox(50);
        hFooterlbl.setAlignment(Pos.CENTER); // Căn giữa các label
        hFooterlbl.getChildren().addAll(lblHint, lblMis, lblScore, lblTime);

        // Thiết lập root và maincontainer
        root = new HBox(20, suGrid, controlPanel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        maincontainer = new AnchorPane(controlHeader, root, hFooterlbl);
        maincontainer.setPadding(new Insets(20));
        AnchorPane.setTopAnchor(controlHeader, 10.0);
        AnchorPane.setLeftAnchor(controlHeader, 10.0);
        AnchorPane.setRightAnchor(controlHeader, 10.0);

        AnchorPane.setTopAnchor(root, 50.0);
        AnchorPane.setBottomAnchor(root, 10.0);
        AnchorPane.setLeftAnchor(root, 10.0);
        AnchorPane.setRightAnchor(root, 10.0);

        AnchorPane.setBottomAnchor(hFooterlbl, 10.0);
        AnchorPane.setLeftAnchor(hFooterlbl, 10.0);
        AnchorPane.setRightAnchor(hFooterlbl, 10.0);
        // Thiết lập scene và stage
        Scene scene = new Scene(maincontainer, 900, 800);
        scene.setFill(Color.WHITE); // Đặt màu nền của Scene là màu trắng

        // Thiết lập sự kiện thay đổi kích thước cho Scene
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents(newVal.doubleValue());
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            resizeComponents(newVal.doubleValue());
        });

        stage.setTitle("SUDOKU");
        stage.setResizable(true); // Cho phép thay đổi kích thước cửa sổ
        stage.setScene(scene);
        stage.show();

        // Đặt AppUI cho sudokuPanel và khởi tạo timer
        sudokuPanel.setAppUI(this);
        initializeTimer();
        startTimer();
        handleButtonClick();
    }
    // Phương thức để thay đổi kích thước các thành phần bên trong theo kích thước Scene mới
        private void resizeComponents(double newSceneSize) {
        // Thay đổi kích thước các thành phần trong maincontainer theo kích thước Scene mới
        maincontainer.setPrefWidth(newSceneSize);
        maincontainer.setPrefHeight(newSceneSize);
    }
    //Getter Setter
    @SuppressWarnings("exports")
    public GridPane getSuGrid() {
        return suGrid;
    }

    @SuppressWarnings("exports")
    public void setSuGrid(GridPane suGrid) {
        this.suGrid = suGrid;
    }
    
    @SuppressWarnings("exports")
    public Button getBtnHint() {
        return btnHint;
    }

    @SuppressWarnings("exports")
    public void setBtnHint(Button btnHint) {
        this.btnHint = btnHint;
    }
    
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    //Handle Event
    private void handleButtonClick(){
        btnEasy.setOnAction(event -> showConfirmationDialog("Easy"));
        btnMed.setOnAction(event -> showConfirmationDialog("Medium"));
        btnHard.setOnAction(event -> showConfirmationDialog("Hard"));
        btnEx.setOnAction(event -> showConfirmationDialog("Expert"));
        btnMas.setOnAction(event -> showConfirmationDialog("Master"));
        btnEXtr.setOnAction(event -> showConfirmationDialog("Extremely"));

        btnDelete.setOnAction(event -> sudokuPanel.deleteValue());
        btnHint.setOnAction(event -> sudokuPanel.autoFill());
        btnNew.setOnAction(event -> sudokuPanel.playAgain(this.mode));
    }
    private void showConfirmationDialog(String mode) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Change Puzzle Mode");
        alert.setHeaderText("Change Puzzle Mode to " + mode);
        alert.setContentText("Are you sure you want to change the puzzle mode? All current data will be lost.");

        alert.showAndWait().ifPresent(response -> {
            if (response == javafx.scene.control.ButtonType.OK) {
                // Clear current data and generate new puzzle based on mode
                this.mode = mode;
                System.out.println(mode);
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
        resetTimer();
        startTimer();
        sudokuPanel.resetMoveHistory();
        sudokuPanel.resetHint();
        sudokuPanel.resetMistakes();
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
        // Cập nhật lại các thành phần điều khiển khác nếu cần
        resetTimer();
        startTimer();
        sudokuPanel.resetMoveHistory();
        sudokuPanel.resetHint();
        sudokuPanel.resetMistakes();
    }
    public void updateMistakeLabel(int mistakes) {
        lblMis.setText("Mistakes: " + mistakes + "/3");
    }
    public void updateHint(int hint){
        lblHint.setText("Hint: " + hint + "/5");
    }
    
    public static void main(String[] args) {
            launch(args);
        
    };
}