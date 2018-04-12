import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.shape.Box;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import java.sql.SQLException;

public class Window extends Application
{
    Stage window;
    private static boolean logged = false;
    private Scene scene;
    /*private Scene browseScene, documentScene;
    private Scene libraryCardScene, libraryCardOrdersScene, libraryCardHistoryScene;
    private Scene manageScene, findUserScene, addScene, historyScene;
    private Scene notificationScene;
    private Scene helpScene;*/

    public static void main (String[] args) throws SQLException, ClassNotFoundException
    {
        Conn.access();
        logged = Conn.createDB();

        Administrator librarian = new Librarian(-1);

        if (logged)
        {
            String[] newUser = {"0", "admin", "", "", "1234"};
            librarian.addUser(newUser);
            Conn.addEntryToHistory(1, "A librarian has entered the empty library.");
        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        window = primaryStage;
        window.setTitle("Library View");
        window.setMinWidth(400);
        window.setMinHeight(540);

        window.setOnCloseRequest(e -> {
            e.consume();
            closeWindow();
        });

        setWelcomeScene();

        window.show();
    }

    private void setWelcomeScene()
    {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        Label welcomeMessage = new Label("Welcome to the Library View!");
        welcomeMessage.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, welcomeMessage.getFont().getSize()));

        Label loginLabel = new Label("Librarian ID:");

        TextField loginField = new TextField("");
        loginField.setPromptText("Enter your Librarian ID...");

        Label passwordLabel = new Label("Password: ");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password...");

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            boolean loginSuccessful = isInt(loginField.getText());
            if (loginSuccessful)
                try {
                    if (Conn.requestLogIn(Integer.parseInt(loginField.getText()), passwordField.getText()) == -1)
                        loginSuccessful = false;
                } catch (NullPointerException|SQLException exception) {
                    System.out.println("Error in [setWelcomeScene]: interrupted connection to database.");
                    loginSuccessful = false;
                }
            if (loginSuccessful) {
                logged = true;
                setConsoleScene();//setBrowseScene();
            } else {
                Popups.alert("Invalid login or password.");
            }
        });
        loginButton.setDefaultButton(true);

        int rowIndexCounter = 0;
        GridPane.setConstraints(welcomeMessage, 0, rowIndexCounter++);
        GridPane.setConstraints(loginLabel, 0, rowIndexCounter++);
        GridPane.setConstraints(loginField, 0, rowIndexCounter++);
        GridPane.setConstraints(passwordLabel, 0, rowIndexCounter++);
        GridPane.setConstraints(passwordField, 0, rowIndexCounter++);
        GridPane.setConstraints(loginButton, 0, rowIndexCounter++);

        grid.getChildren().addAll(welcomeMessage, loginLabel, loginField, passwordLabel, passwordField, loginButton);

        scene = new Scene(grid, 960, 600);

        window.setScene(scene);
    }

    private void setBrowseScene()
    {
        if (banishUnlogged()) return;

        VBox grid = new VBox(30);
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setAlignment(Pos.BASELINE_CENTER);

        Label libraryName = new Label("Library View");
        libraryName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, libraryName.getFont().getSize()*2));

        HBox upperPanel = new HBox(20);
        upperPanel.setAlignment(Pos.CENTER_RIGHT);
        grid.setPadding(new Insets(15, 30, 15, 30));
        double upperPanelFontSize = 1.1;

        Label userName = new Label("Librarian"/*Librarian.name*/);
        userName.setFont(Font.font("Verdana", userName.getFont().getSize()*upperPanelFontSize));

        Label notifications = new Label("Notifications"/*Image here*/);

        upperPanel.getChildren().addAll(userName, notifications);

        HBox menuPanel = new HBox(100);
        menuPanel.setAlignment(Pos.CENTER);
        double menuPanelFontSize = 1.2;

        Label browseAndSearch = new Label("Browse & Search");
        browseAndSearch.setFont(Font.font("Verdana", browseAndSearch.getFont().getSize()*menuPanelFontSize));
        browseAndSearch.setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));
        browseAndSearch.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        browseAndSearch.setOnMouseClicked(e -> {
            setBrowseScene();
        });

        Label manageLibrary = new Label("Manage Library");
        manageLibrary.setFont(Font.font("Verdana", manageLibrary.getFont().getSize()*menuPanelFontSize));
        manageLibrary.setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));
        manageLibrary.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        manageLibrary.setOnMouseClicked(e -> {
            //setManageLibraryScene();
        });

        Label account = new Label("Account");
        account.setFont(Font.font("Verdana", account.getFont().getSize()*menuPanelFontSize));
        account.setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));
        account.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        account.setOnMouseClicked(e -> {
            //setAccountScene();
        });

        Label help = new Label("Help");
        help.setFont(Font.font("Verdana", help.getFont().getSize()*menuPanelFontSize));
        help.setOnMouseEntered(e -> scene.setCursor(Cursor.HAND));
        help.setOnMouseExited(e -> scene.setCursor(Cursor.DEFAULT));
        help.setOnMouseClicked(e -> {
            setHelpScene();
        });

        menuPanel.getChildren().addAll(browseAndSearch, manageLibrary, account, help);

        grid.getChildren().addAll(libraryName, upperPanel, menuPanel);

        scene = new Scene(grid, 960, 600);

        window.setScene(scene);
    }

    private void setConsoleScene()
    {
        if (banishUnlogged()) return;

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        Label libraryName = new Label("Library View");
        libraryName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, libraryName.getFont().getSize()*1.5));
        libraryName.setAlignment(Pos.CENTER_LEFT);

        Label welcomeName = new Label("Logged in as " + "ID1"/*Librarian.name*/);
        welcomeName.setFont(Font.font("Verdana", welcomeName.getFont().getSize()*0.9));
        welcomeName.setAlignment(Pos.CENTER_RIGHT);

        TextField inputField = new TextField("");
        inputField.setPromptText("Type the command and press 'Enter'...");

        TextArea outputField = new TextArea("Type in 'help' and press 'Enter' if you're stuck.\n");
        outputField.setEditable(false);
        outputField.setWrapText(true);
        outputField.getParagraphs();
        outputField.setPrefWidth(400);
        outputField.setPrefHeight(400);
        outputField.textProperty().addListener(e -> outputField.setScrollTop(Double.MAX_VALUE));

        inputField.setOnKeyPressed(e -> {
            if (e.getCode().equals(KeyCode.ENTER)) {
                outputField.appendText(
                        outputField.getParagraphs() + ". Good morning!\n");
            }
        });

        int rowIndexCounter = 0;
        GridPane.setConstraints(libraryName, 0, rowIndexCounter++);
        GridPane.setConstraints(welcomeName, 0, rowIndexCounter++);
        GridPane.setConstraints(inputField, 0, rowIndexCounter++);
        GridPane.setConstraints(outputField, 0, rowIndexCounter++);

        grid.getChildren().addAll(libraryName, welcomeName, inputField, outputField);

        scene = new Scene(grid, 960, 600);

        window.setScene(scene);
    }

    private void setHelpScene()
    {
        Label libraryName = new Label("Library View");
        libraryName.setFont(Font.font("Verdana", FontWeight.BOLD, FontPosture.ITALIC, libraryName.getFont().getSize()*2));

        ToolBar toolBar = new ToolBar(
                new Button("Browse & Search"),
                new Separator(Orientation.HORIZONTAL),
                new Button("Manage Library"),
                new Separator(Orientation.HORIZONTAL),
                new Button("Account"),
                new Separator(Orientation.HORIZONTAL),
                new Button("Help"),
                new Separator(Orientation.HORIZONTAL),
                new Label("Welcome, Librarian!")
        );

        VBox topBar = new VBox(20);
        topBar.getChildren().addAll(libraryName, toolBar);

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(topBar);

        scene = new Scene(borderPane, 960, 600);

        window.setScene(scene);
    }

    private boolean banishUnlogged()
    {
        if (!logged) {
            System.out.println("Error in [setBrowseScene]: user is not logged in.");
            setWelcomeScene();
            Popups.alert("You are currently not logged in! Please, log in.");
            return true;
        }
        return false;
    }

    private void closeWindow()
    {
        boolean answer = Popups.confirm("Are you sure you want to quit the library?");
        if (answer)
            window.close();
    }

    private boolean isInt(String message)
    {
        try {
            int number = Integer.parseInt(message);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Error in [isInt]: " + message + " is not a number.");
            return false;
        }
    }

}
