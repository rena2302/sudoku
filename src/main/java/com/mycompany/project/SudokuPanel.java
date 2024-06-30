package com.mycompany.project;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class SudokuPanel{
    private static final int SIZE=9;
    private static final int CELL_SIZE=50;

    private App appUI;
    private int currentlySelectedCol = -1;
    private int currentlySelectedRow = -1;
    private TextField[][] cells = new TextField[SIZE][SIZE];
    private GridPane gridP;
    private int mistake;
    private int hint;
    private SudokuGenerator generator = new SudokuGenerator();
    private SudokuPuzzle puzzle = generator.generateRandomSudoku(SudokuPuzzleType.NINEBYNINE, "Medium");
    private final Stack<int[]> moveHistory = new Stack<>();
    //Setup
    public void newSudokuPuzzle(SudokuPuzzle puzzle) {
		this.puzzle = puzzle;
	}
    public void setAppUI(App appUI){
        this.appUI = appUI;
    }
    
    @SuppressWarnings("exports")
    public GridPane createSudokuGrid() {
        gridP = new GridPane();
        gridP.setGridLinesVisible(false);
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                String value = puzzle.getValue(row, col);
                cells[row][col] = new TextField();
                setupTextField(cells[row][col]); // Gọi phương thức để chỉ cho phép nhập số
                cells[row][col].setText(value);
                cells[row][col].setPrefSize(50, 50);
                cells[row][col].setAlignment(Pos.CENTER);
                gridP.add(cells[row][col], col, row);
                final int currentRow = row;
                final int currentCol = col;
                cells[row][col].setOnMouseClicked(event -> {
                    currentlySelectedRow = currentRow;
                    currentlySelectedCol = currentCol;
                    // Cập nhật màu nền hoặc các xử lý khác ở đây nếu cần
                });
                cells[row][col].setOnKeyPressed(this::handleKeyPress);
            }
        }
        
        return gridP;
    }
    private void setupTextField(TextField textField) {
        textField.setPrefSize(CELL_SIZE, CELL_SIZE);
        textField.setAlignment(Pos.CENTER);
        textField.setFont(Font.font(20));
        textField.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.4;-fx-text-fill: black;");
        // // Sự kiện MouseEvent để điều khiển màu nền khi chọn
        // textField.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> {
        //     if (textField.isEditable()) {
        //         textField.setStyle("-fx-background-color: rgba(0, 128, 0, 0.3); -fx-border-color: black; -fx-border-width: 0.5;");
        //     }
        // });
        // Sự kiện MouseEvent để điều khiển màu nền khi không chọn
        // textField.addEventFilter(MouseEvent.MOUSE_EXITED, event -> {
        //     if (textField.isEditable()) {
        //         textField.setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.4;");
        //     }
        // });
        
        // Chỉ cho phép nhập số
        textField.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[1-9]?")) {
                return change;
            }
            return null;
        }));
    }
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text needed
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void resetMoveHistory() {
        moveHistory.clear();
    }
    public void resetHint(){
        this.hint = 0;
        if (appUI != null) {
            appUI.updateHint(hint);
        }
    }
    public void resetMistakes() {
        this.mistake = 0;
        if (appUI != null) {
            appUI.updateMistakeLabel(mistake);
        }
    }
    //Event
    public void autoFill(){
        Stack<int[]> emptySlots = puzzle.emptySlot();
        if (hint >= 5){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(appUI.getBtnHint().getScene().getWindow());
            alert.setTitle("Thông báo");
            alert.setHeaderText(null);
            alert.setContentText("Đã hết số lần gợi ý");
            alert.showAndWait();
        }
        if (currentlySelectedRow != -1 && currentlySelectedCol != -1) {
            currentlySelectedRow = -1;
            currentlySelectedCol = -1;
        }

        Random rand = new Random();
        String[][] solution = puzzle.getSolution();
        while (!emptySlots.isEmpty()) {
            int randomIndex = rand.nextInt(emptySlots.size());
            int[] emptySlot = emptySlots.remove(randomIndex); 
    
            int row = emptySlot[0];
            int col = emptySlot[1];
    
            String value = solution[row][col];
    
            if (puzzle.isValidMove(row, col, value) && hint < 5) {
                cells[row][col].setText(value);
                cells[row][col].setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.4; -fx-text-fill: #FFD700;");
                puzzle.board[row][col] = value;
                hint++;
                appUI.updateHint(hint);
                return; 
            }
        }
    }
    public void deleteValue() {
        if (currentlySelectedRow != -1 && currentlySelectedCol != -1) {
            if (puzzle.getBoard()[currentlySelectedRow][currentlySelectedCol].equals("")) {
                showAlert("Thông báo", "Cell trống", AlertType.INFORMATION);
            } else {
                if (!cells[currentlySelectedRow][currentlySelectedCol].getStyle().contains("-fx-text-fill: black")) {
                    puzzle.getBoard()[currentlySelectedRow][currentlySelectedCol] = "";
                    cells[currentlySelectedRow][currentlySelectedCol].setText("");
                } else {
                    showAlert("Thông báo", "Cell đề bài", AlertType.INFORMATION);
                }
            }
            // Assuming repaint() is used to refresh the UI in Swing context; adjust as needed for JavaFX
        } else {
            showAlert("Thông báo", "Chưa chọn ô để xóa", AlertType.INFORMATION);
        }
    }
    public void gameOver(int mistake){
            if (mistake == 3) {
                showAlert("GameOver", "You make 3/3 mistakes", AlertType.WARNING);
                playAgain(appUI.getMode());
                this.mistake = 0; // Reset mistake
                if (appUI != null) {
                    appUI.updateMistakeLabel(this.mistake); 
            }
        }
    }

    public void playAgain(String mode) {
        System.err.println(mode);
        SudokuPuzzle newPuzzle = new SudokuGenerator().generateRandomSudoku(SudokuPuzzleType.NINEBYNINE, mode);
        newSudokuPuzzle(newPuzzle);
        currentlySelectedCol = -1;
        currentlySelectedRow = -1;
        appUI.rebuildInterface();
    }
    private void checkBoardFull() {
        if (puzzle.boardFull()) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Chúc mừng");
            alert.setHeaderText("Bạn đã chiến thắng!");
            alert.setContentText("Bạn có muốn chơi lại không?");

            ButtonType playAgainButton = new ButtonType("Play Again");
            ButtonType exitButton = new ButtonType("Exit");

            alert.getButtonTypes().setAll(playAgainButton, exitButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == playAgainButton) {
                playAgain(appUI.getMode());
            } else {
                System.exit(0);
            }
        }
    }
    private void handleKeyPress(KeyEvent event) {
        String number = event.getText();
        if (number.matches("[1-9]")) {
            event.consume();
            handleButtonClick(number);
        }
    }
    public void handleButtonClick(String number){
        // Check if the currently selected row and column are set
        if (currentlySelectedRow == -1 || currentlySelectedCol == -1) {
            showAlert("No Cell Selected", "Please select a cell before making a move.", AlertType.WARNING);
            return; // Exit the method as no cell is selected
        }
        //Check user make right choice or not
        if (!puzzle.getSolutionValue(currentlySelectedRow, currentlySelectedCol).equals(number)) {
            cells[currentlySelectedRow][currentlySelectedCol].setText(number);
            cells[currentlySelectedRow][currentlySelectedCol].setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.4;-fx-text-fill: red;");
            puzzle.getBoard()[currentlySelectedRow][currentlySelectedCol] = number;
            moveHistory.push(new int[]{currentlySelectedRow, currentlySelectedCol});
            mistake++;
            appUI.updateMistakeLabel(mistake);
            gameOver(mistake);
        }
        else{
            cells[currentlySelectedRow][currentlySelectedCol].setText(number);
            cells[currentlySelectedRow][currentlySelectedCol].setStyle("-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.4;-fx-text-fill: blue;");
            puzzle.getBoard()[currentlySelectedRow][currentlySelectedCol] = number;
            moveHistory.push(new int[]{currentlySelectedRow, currentlySelectedCol});
            checkBoardFull();
        }
    }
}
