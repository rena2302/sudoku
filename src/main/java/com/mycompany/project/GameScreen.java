package com.mycompany.project;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

public class GameScreen {
    public interface OnBackListener{
        void onBack();
    }
    private OnBackListener onBackListener;
    
    private String mode = "Medium";

    private Button btnClassic;
    private Button btnMed;
    private Button btnAdvance;
    private Button btnEx;
    private Button btnMas;
    private Button btnPro;

    private Button btnPause;
    private Button btnUndo;
    private Button btnDelete;
    private Button btnNote;
    private Button btnHint;
    private Button btnNew;
    private Button btnBack;

    private Label lblMis;
    private Label lblScore;
    private Label lblTime;
    private Label lblHint;
    private Label lblMode;
    
    private GridPane suGrid;
    private final SudokuPanel sudokuPanel = new SudokuPanel();

    private AnchorPane mainContainer;
    private HBox root;
    private VBox controlPanel;
    private HBox controlHeader;
    private HBox footerLbl;

    private int secondsPassed = 0;
    private boolean timerIsRunning = false;
    private Timeline timer;

    private Button selectedModeButton;

    private Scene scene;

    public GameScreen(){
        suGrid = sudokuPanel.createSudokuGrid();
        controlPanel = createControlPanel();
        controlHeader = createControlHeader();

        lblMis = new Label("Mistake: 0/3");
        lblScore = new Label("Score: 0");
        lblTime = new Label("Timer: 00:00");
        lblHint = new Label("Hint: 0/5");

        String labelStyle = "-fx-text-fill: darkblue;";
        lblMis.setStyle(labelStyle);
        lblScore.setStyle(labelStyle);
        lblTime.setStyle(labelStyle);
        lblHint.setStyle(labelStyle);

        lblMis.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblScore.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblTime.setFont(Font.font("System", FontWeight.BOLD, 14));
        lblHint.setFont(Font.font("System", FontWeight.BOLD, 14));

        footerLbl = new HBox(50);
        footerLbl.setAlignment(Pos.CENTER);
        footerLbl.getChildren().addAll(lblHint, lblMis, lblScore, lblTime);

        root = new HBox(30, suGrid, controlPanel);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        suGrid.prefWidthProperty().bind(root.widthProperty());
        suGrid.prefHeightProperty().bind(root.heightProperty());

        mainContainer = new AnchorPane(controlHeader, root, footerLbl);
        //mainContainer.setPadding(new Insets(20));

        //chỉnh background
        // String imageUrl = getClass().getResource("/background.jpg").toExternalForm();
        // mainContainer.setStyle(
        //     "-fx-background-image: url('/background.jpg');" +
        //     "-fx-background-size: cover;" );
        
       // Anchor the controlHeader and hHeaderlbl
        AnchorPane.setTopAnchor(controlHeader, 10.0);
        AnchorPane.setLeftAnchor(controlHeader, 10.0);
        AnchorPane.setRightAnchor(controlHeader, 10.0);

        // Anchor the root and make suGrid grow with the container
        AnchorPane.setTopAnchor(root, 50.0);
        AnchorPane.setLeftAnchor(root, 10.0);
        AnchorPane.setRightAnchor(root, 10.0);
        AnchorPane.setBottomAnchor(root, 20.0);

        // Make suGrid grow with the container
        HBox.setHgrow(suGrid, Priority.ALWAYS);

        AnchorPane.setBottomAnchor(footerLbl, 10.0);
        AnchorPane.setLeftAnchor(footerLbl, 10.0);
        AnchorPane.setRightAnchor(footerLbl, 10.0);
            

        scene = new Scene(mainContainer, 960, 720);
        scene.setFill(Color.WHITE);

        sudokuPanel.setAppUI(this);
        initializeTimer();
        handleButtonClick();
        sudokuPanel.playSound("/sounds/gamemusic.wav");
    }
    private VBox createControlPanel() {
        VBox controlPanel = new VBox(20);
        controlPanel.setAlignment(Pos.TOP_CENTER);
    
        btnUndo = new Button("Undo");
        btnDelete = new Button("Delete");
        btnNote = new Button("Note");
        btnHint = new Button("Hint");
        btnNew = new Button("NEW GAME");
        btnPause = new Button("PAUSE");
        btnBack = new Button("Back to Menu");

        btnNew.setPrefWidth(200);
        btnUndo.setPrefWidth(200);
        btnDelete.setPrefWidth(200);
        btnNote.setPrefWidth(200);
        btnHint.setPrefWidth(200);
        btnPause.setPrefWidth(200);
        btnBack.setPrefWidth(200);
    
        // Thiết lập nền màu theo mã màu RGB và opacity là 1.000 (100%)
        String buttonStyle = "-fx-background-color: rgba(234, 238, 244, 1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;-fx-font-size: 16px;";
    
        btnNew.setStyle(buttonStyle);
        btnUndo.setStyle(buttonStyle);
        btnDelete.setStyle(buttonStyle);
        btnNote.setStyle(buttonStyle);
        btnHint.setStyle(buttonStyle);
        btnPause.setStyle(buttonStyle);
        btnBack.setStyle(buttonStyle);
    
        GridPane numberPad = createNumberPad();
        controlPanel.getChildren().add(numberPad);
    
        controlPanel.getChildren().addAll(btnNew, btnUndo, btnDelete, btnNote, btnHint, btnPause, btnBack);
        return controlPanel;
    }    

    private GridPane createNumberPad() {
        GridPane numberPad = new GridPane();
        numberPad.setHgap(3);
        numberPad.setVgap(3);
    
        for (int i = 1; i <= 9; i++) {
            Button numberButton = new Button(String.valueOf(i));
            numberButton.setPrefSize(70, 70);
            numberButton.setFont(Font.font(20));
    
            String buttonStyle = "-fx-background-color: rgba(234,238,244,1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;";
            numberButton.setStyle(buttonStyle);

            numberButton.setOnAction(event -> sudokuPanel.handleButtonClick(numberButton.getText()));
            numberPad.add(numberButton, (i - 1) % 3, (i - 1) / 3);
        }
    
        return numberPad;
    }

    private HBox createControlHeader() {
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER);
        HBox.setHgrow(header, Priority.ALWAYS);
        lblMode = new Label("Mode:");
        btnClassic = new Button("Classic");
        btnMed = new Button("Medium");
        btnAdvance = new Button("Advance");
        btnEx = new Button("Expert");
        btnMas = new Button("Master");
        btnPro = new Button("Pro");
    
        btnClassic.setPrefWidth(120);
        btnMed.setPrefWidth(120);
        btnAdvance.setPrefWidth(120);
        btnEx.setPrefWidth(120);
        btnMas.setPrefWidth(120);
        btnPro.setPrefWidth(120);
    
        // Thiết lập nền màu và chữ màu xanh biển nhạt
        String buttonStyle = "-fx-background-color: rgba(234, 238, 244, 1.000); -fx-text-fill: darkblue; -fx-background-radius: 10;-fx-font-size: 16px;";

        lblMode.setStyle("-fx-text-fill: darkblue;-fx-font-size: 24px;");
        lblMode.setFont(Font.font(buttonStyle, FontWeight.BOLD, 13));
        btnClassic.setStyle(buttonStyle);
        btnMed.setStyle(buttonStyle);
        btnAdvance.setStyle(buttonStyle);
        btnEx.setStyle(buttonStyle);
        btnMas.setStyle(buttonStyle);
        btnPro.setStyle(buttonStyle);
    
        header.getChildren().addAll(lblMode, btnClassic, btnMed, btnAdvance, btnEx, btnMas, btnPro);
        return header;
    }
    //Getter Setter
    public GridPane getSuGrid() {
        return suGrid;
    }

    public void setSuGrid(GridPane suGrid) {
        this.suGrid = suGrid;
    }
    
    public Button getBtnHint() {
        return btnHint;
    }

    public void setBtnHint(Button btnHint) {
        this.btnHint = btnHint;
    }
    
    public String getMode() {
        return mode;
    }

    public HBox getRoot(){
        return root;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getSecondPassed(){
        return secondsPassed;
    }

    public Scene getGameScene() {
        return scene;
    }
    public void setOnBack(OnBackListener listener){
        this.onBackListener = listener;
    }
    //Handle Event
    private void handleButtonClick(){
        btnClassic.setOnAction(event -> handleModeButtonClick(btnClassic, "Classic"));
        btnMed.setOnAction(event -> handleModeButtonClick(btnMed, "Medium"));
        btnAdvance.setOnAction(event -> handleModeButtonClick(btnAdvance, "Advance"));
        btnEx.setOnAction(event -> handleModeButtonClick(btnEx, "Expert"));
        btnMas.setOnAction(event -> handleModeButtonClick(btnMas, "Master"));
        btnPro.setOnAction(event -> handleModeButtonClick(btnPro, "Pro"));

        btnDelete.setOnAction(event -> sudokuPanel.deleteValue());
        btnHint.setOnAction(event -> sudokuPanel.autoFill());
        btnNew.setOnAction(event -> sudokuPanel.playAgain(this.mode));
        btnNote.setOnAction(event -> sudokuPanel.takeNote());
        btnPause.setOnAction(event -> pauseAction());
        btnUndo.setOnAction(event -> sudokuPanel.undoMove());

        btnBack.setOnAction(event -> {
            if(onBackListener != null){
                onBackListener.onBack();
            }
        });
    }

    private void handleModeButtonClick(Button clickedButton, String mode) {
        // Update the mode
        this.mode = mode;
        System.out.println(mode);
        rebuildMode(mode);

        // Highlight the selected button
        updateButtonStyles(clickedButton);

        // Optional: Show confirmation dialog
        showConfirmationDialog(mode);
    }

    private void updateButtonStyles(Button selectedButton) {
        String selectedStyle = "-fx-background-color: rgba(100, 150, 255, 1.000); -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 16px;";
        String defaultStyle = "-fx-background-color: rgba(234, 238, 244, 1.000); -fx-text-fill: darkblue; -fx-background-radius: 10; -fx-font-size: 16px;";

        // Reset style for all buttons
        btnClassic.setStyle(defaultStyle);
        btnMed.setStyle(defaultStyle);
        btnAdvance.setStyle(defaultStyle);
        btnEx.setStyle(defaultStyle);
        btnMas.setStyle(defaultStyle);
        btnPro.setStyle(defaultStyle);

        // Apply style for the selected button
        selectedButton.setStyle(selectedStyle);
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
    private void pauseAction(){
        // Create overlay for PAUSE
        StackPane pauseOverlay = new StackPane();
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5);");
        Label pausedText = new Label("PAUSED");
        pausedText.setFont(Font.font("System", FontWeight.BOLD, 40));
        pauseOverlay.getChildren().add(pausedText);
        pauseOverlay.setVisible(false);

        mainContainer.getChildren().add(pauseOverlay);
        AnchorPane.setTopAnchor(pauseOverlay, 0.0);
        AnchorPane.setBottomAnchor(pauseOverlay, 0.0);
        AnchorPane.setLeftAnchor(pauseOverlay, 0.0);
        AnchorPane.setRightAnchor(pauseOverlay, 0.0);

        // Add blur effect
        BoxBlur blur = new BoxBlur();
        blur.setWidth(10);
        blur.setHeight(10);
        blur.setIterations(3);

        // Pause button event handler
        btnPause.setOnAction(event -> {
            pauseTimer();
            root.setEffect(blur);
            controlHeader.setEffect(blur);
            footerLbl.setEffect(blur);
            pauseOverlay.setVisible(true);
        });

        // Resume game on click anywhere
        pauseOverlay.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            resumeTimer();
            root.setEffect(null);
            controlHeader.setEffect(null);
            footerLbl.setEffect(null);
            pauseOverlay.setVisible(false);
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
        suGrid.getChildren().clear(); // Clear all components in suGrid
        // Recreate the Sudoku grid
        suGrid = sudokuPanel.createSudokuGrid();
        suGrid.prefWidthProperty().bind(root.widthProperty());
        suGrid.prefHeightProperty().bind(root.heightProperty());
        // Apply layout properties if necessary
        suGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Ensure it can grow
        
        // Remove old grid and add the new one
        root.getChildren().remove(0);
        root.getChildren().add(0, suGrid);
        
        // Update other control components if necessary
        resetTimer();
        startTimer();
        sudokuPanel.resetMoveHistory();
        sudokuPanel.resetHint();
        sudokuPanel.resetMistakes();
        sudokuPanel.resetScore();
    }
    
    public void rebuildMode(String mode){
        SudokuPuzzle newPuzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.NINEBYNINE, mode);
        sudokuPanel.newSudokuPuzzle(newPuzzle);
        suGrid.getChildren().clear(); // Clear all components in suGrid
        
        // Recreate the Sudoku grid
        suGrid = sudokuPanel.createSudokuGrid();
        suGrid.prefWidthProperty().bind(root.widthProperty());
        suGrid.prefHeightProperty().bind(root.heightProperty());
        // Apply layout properties if necessary
        suGrid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Ensure it can grow
        
        // Remove old grid and add the new one
        root.getChildren().remove(0);
        root.getChildren().add(0, suGrid);
        
        // Update other control components if necessary
        resetTimer();
        startTimer();
        sudokuPanel.resetMoveHistory();
        sudokuPanel.resetHint();
        sudokuPanel.resetMistakes();
        sudokuPanel.resetScore();
    }
    
    public void updateMistakeLabel(int mistakes) {
        lblMis.setText("Mistakes: " + mistakes + "/3");
    }
    public void updateHint(int hint){
        lblHint.setText("Hint: " + hint + "/5");
    }
    public void updateScore(int score){
        lblScore.setText("Score: " + score);
    }
}
