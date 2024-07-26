package com.mycompany.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Consumer;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

import com.mycompany.project.database.MyConnection;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;


public class RegisterAndLogin {
    private Scene scene;
    private Consumer<Void> onLoginSuccess;

    public RegisterAndLogin(){
        
        TabPane tabPane = new TabPane();

        StackPane root = new StackPane(tabPane);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(10));

        scene = new Scene(root, 400, 205);
        
        Tab registerTab = new Tab("Register");
        registerTab.setContent(createRegisterPane());

        Tab loginTab = new Tab("Login");
        loginTab.setContent(createLoginPane());

        tabPane.getTabs().addAll(registerTab, loginTab);

        // Bind font sizes to scene width
        NumberBinding fontSizeBinding = Bindings.divide(scene.widthProperty(), 40);

        // Apply the font size binding to labels and text fields
        for (Tab tab : tabPane.getTabs()) {
            GridPane pane = (GridPane) tab.getContent();
            pane.getChildren().forEach(node -> {
                if (node instanceof Labeled) {
                    ((Labeled) node).styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));
                } else if (node instanceof TextInputControl) {
                    ((TextInputControl) node).styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSizeBinding.asString(), ";"));
                }
            });
        }
    }
     // Method to create the registration pane
     private GridPane createRegisterPane() {
        GridPane registerPane = new GridPane();
        registerPane.setPadding(new Insets(10, 10, 10, 10));
        registerPane.setVgap(8);
        registerPane.setHgap(10);
        registerPane.setAlignment(Pos.CENTER);
        
        Image backgroundImage = new Image("/images6.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        registerPane.setBackground(new Background(background));

        Label username = new Label("Username:");
        GridPane.setConstraints(username, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);

        Label emailLabel = new Label("Email:");
        GridPane.setConstraints(emailLabel, 0, 1);
        TextField emailInput = new TextField();
        GridPane.setConstraints(emailInput, 1, 1);

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 2);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 2);

        Label rePasswordLabel = new Label("Re-Password:");
        GridPane.setConstraints(rePasswordLabel, 0, 3);
        PasswordField rePasswordInput = new PasswordField();
        GridPane.setConstraints(rePasswordInput, 1, 3);

        Button registerButton = new Button("Register");
        GridPane.setConstraints(registerButton, 1, 4);

        registerButton.setOnAction(e -> handleRegister(usernameInput, emailInput, passwordInput, rePasswordInput));

        registerPane.getChildren().addAll(username, usernameInput, emailLabel, emailInput, passwordLabel, passwordInput, rePasswordLabel, rePasswordInput, registerButton);
        
        handleEnter(registerButton);
        return registerPane;
    }

    // Method to create the login pane
    private GridPane createLoginPane() {
        GridPane loginPane = new GridPane();
        loginPane.setPadding(new Insets(10, 10, 10, 10));
        loginPane.setVgap(8);
        loginPane.setHgap(10);
        loginPane.setAlignment(Pos.CENTER);
        
        Image backgroundImage = new Image("/images5.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true);
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        loginPane.setBackground(new Background(background));

        Label loginEmailLabel = new Label("Email:");
        GridPane.setConstraints(loginEmailLabel, 0, 0);
        TextField loginEmailInput = new TextField();
        GridPane.setConstraints(loginEmailInput, 1, 0);

        Label loginPasswordLabel = new Label("Password:");
        GridPane.setConstraints(loginPasswordLabel, 0, 1);
        PasswordField loginPasswordInput = new PasswordField();
        GridPane.setConstraints(loginPasswordInput, 1, 1);

        Button loginButton = new Button("Login");
        GridPane.setConstraints(loginButton, 1, 2);

        loginButton.setOnAction(e -> handleLogin(loginEmailInput, loginPasswordInput));

        loginPane.getChildren().addAll(loginEmailLabel, loginEmailInput, loginPasswordLabel, loginPasswordInput, loginButton);

        handleEnter(loginButton);
        return loginPane;
    }

    // Method to handle registration
    private void handleRegister(TextField usernameInput, TextField emailInput, PasswordField passwordInput, PasswordField rePasswordInput) {
        String email = emailInput.getText();
        String password = passwordInput.getText();
        String userName = usernameInput.getText();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    
        if (validateEmail(email) && validatePassword(password) && rePasswordInput.getText().equals(password)) {
            try (Connection conn = MyConnection.getConnection()) {
                // Check if the email already exists
                String checkQuery = "SELECT COUNT(*) FROM users WHERE email = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                    checkStmt.setString(1, email);
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt(1) > 0) {
                        showAlert(Alert.AlertType.ERROR, "Registration", "Account already exists with this email!");
                        return;
                    }
                }
    
                // Insert new user
                String insertQuery = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                    stmt.setString(1, userName);
                    stmt.setString(2, email);
                    stmt.setString(3, hashedPassword);
                    stmt.executeUpdate();
                    showAlert(Alert.AlertType.INFORMATION, "Registration", "Registration Successful!");
                }
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Registration", "An error occurred while registering. Please try again.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Registration", "Invalid email or password format!");
        }
    }    

    // Method to handle login
    private void handleLogin(TextField loginEmailInput, PasswordField loginPasswordInput) {
        String email = loginEmailInput.getText();
        String password = loginPasswordInput.getText();

        try (Connection conn = MyConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT username, password FROM users WHERE email = ?")) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hasPassword = rs.getString("password");
                if (BCrypt.checkpw(password, hasPassword)) {
                    saveEmailToLocalStorage(email);
                    String username = rs.getString("username");
                    saveUsernameToLocalStorage(username);
                    showAlert(Alert.AlertType.INFORMATION, "Login", "Login Successful!");
                    if (onLoginSuccess != null) {
                        onLoginSuccess.accept(null);
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login", "Invalid email or password!");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login", "Invalid email or password!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void handleEnter(Button button){
        scene.setOnKeyPressed(event ->{
            switch (event.getCode()) {
                case ENTER:
                    button.fire();
                    break;
                default:
                    break;
            }
        });
    }
    private void saveUsernameToLocalStorage(String username) {
        Preferences prefs = Preferences.userNodeForPackage(RegisterAndLogin.class);
        prefs.put("username", username);
    }

    public String getUserNameFromLocalStorage(){
        Preferences prefs = Preferences.userNodeForPackage(RegisterAndLogin.class);
        return prefs.get("username", null);
    }
    private void saveEmailToLocalStorage(String email) {
        Preferences prefs = Preferences.userNodeForPackage(RegisterAndLogin.class);
        prefs.put("userEmail", email);
    }

    
    public String getEmailFromLocalStorage() {
        Preferences prefs = Preferences.userNodeForPackage(RegisterAndLogin.class);
        return prefs.get("userEmail", null); // Trả về null nếu không tìm thấy email
    }

    public void removeEmailFromLocalStorage() {
        Preferences prefs = Preferences.userNodeForPackage(RegisterAndLogin.class);
        prefs.remove("userEmail");
    }

    // Method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean validatePassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,20}$";
        Pattern pattern = Pattern.compile(passwordRegex);
        return pattern.matcher(password).matches();
    }

    public void setOnLoginSuccess(Consumer<Void> onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public Scene getScene(){
        return scene;
    }
}
