package pl.sda.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by MCK on 04.08.2018 14:52
 **/
public class ClientView extends Application {

    Button loginButton;
    Button disconnectButton;
    Label loginLable;
    Label infoLable;
    static TextField loginField;
    static TextField chatField;
    Scene chatScene;
    Stage window;
    static ListView userList;
    static TextArea chatArea;
    ClientThreadService clientSocket;

    private static final String HOSTNAME = "localhost";
    private static final Integer PORT = 8434;
    private String username = "Anonymous";
    private String loggedUser;

    public static void main(String[] args) {
        launch(args);
    }

    private void connectWithServer() {
        try {
            Socket socket = new Socket(HOSTNAME, PORT);
            System.out.println("You are connected to " + HOSTNAME + " port " + PORT);
            sendUsernameToServer(socket);
            clientSocket = new ClientThreadService(socket);
            Thread thread = new Thread(clientSocket);
            thread.start();
        } catch (IOException e) {
            System.err.println("Can not connect to server");
            e.printStackTrace();
        }
    }

    private void sendUsernameToServer(Socket socket) throws IOException {
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.println(username);
        printWriter.flush();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        buildLoginLable();
        buildLoginInputField(primaryStage);
        buildLoginButton(primaryStage);
        buildInfoLable();

        primaryStage.setTitle(username);

        GridPane loginView = new GridPane();
        loginView.setPadding(new Insets(30, 5, 20, 5));
        loginView.setHgap(5);
        GridPane.setConstraints(loginLable, 0, 0);
        GridPane.setConstraints(loginField, 1, 0);
        GridPane.setConstraints(loginButton, 2, 0);
        GridPane.setConstraints(infoLable, 1, 1);

        loginView.getChildren().addAll(loginLable, loginField, loginButton, infoLable);

        BorderPane borderPanel = new BorderPane();
        borderPanel.setCenter(loginView);
        Scene loginScene = new Scene(borderPanel, 320, 100);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        buildChatView();

    }

    private void buildLoginButton(Stage primaryStage) {
        loginButton = new Button();
        loginButton.setText("Login");
        loginButton.setOnAction((event) -> {

            loginCheck(primaryStage);
        });
    }

    private void loginCheck(Stage primaryStage) {
        loggedUser = loginField.getText().trim();

        if (loginField.getText().equals("")) {
            infoLable.setText("Please provide login!");

        } else {

            for (int u = 0; u < userList.getItems().size(); u++) {

                if (loggedUser.equals(userList.getItems())) {
                    infoLable.setText("Provided login already exist!");
                    return;
                }
            }
            username = loginField.getText().trim();
            primaryStage.setTitle(username + " - chat view");
            window.setScene(chatScene);

            connectWithServer();
        }
    }

    private void buildLoginInputField(Stage primaryStage) {
        loginField = new TextField();
        loginField.setPromptText("Type your login here:");
        loginField.setOnKeyPressed(b -> {
            if (b.getCode() == KeyCode.ENTER) {
                loginCheck(primaryStage);
            }
        });
    }

    private void buildLoginLable() {
        loginLable = new Label();
        loginLable.setText("Enter Your Login: ");
        loginLable.setFont(Font.font("Calibri", 14));
    }

    private void buildInfoLable() {
        infoLable = new Label();
        infoLable.setTextFill(Color.web("#0076a3"));
        infoLable.setFont(Font.font("Calibri", 14));
    }

    private void buildChatView() {

        GridPane chatView = new GridPane();
        chatView.setPadding(new Insets(10, 10, 10, 10));
        chatView.setHgap(5);
        chatView.setVgap(5);
        buildDisconnectButton();
        buildChatTextField();
        buildChatList();
        buildUsersChatList();
        chatView.setConstraints(disconnectButton, 0, 0);
        chatView.setConstraints(chatArea, 0, 1);
        chatView.setConstraints(chatField, 0, 2);
        chatView.setConstraints(userList, 1, 1);

        chatView.getChildren().addAll(disconnectButton, chatField, chatArea, userList);

        BorderPane borderPanel = new BorderPane();
        borderPanel.setCenter(chatView);
        chatScene = new Scene(borderPanel, 730, 500);

    }

    private void buildDisconnectButton() {
        disconnectButton = new Button();
        disconnectButton.setText("Disconnect");
        disconnectButton.setOnAction(event -> {
//            Platform.exit();
//            System.exit(0);
            clientSocket.disconnect(username);
        });

    }

    private void buildChatTextField() {
        chatField = new TextField();
        chatField.setPrefWidth(500);
        chatField.setPromptText("Type something and press ENTER");
        chatField.setOnKeyPressed(b -> {
            if (b.getCode() == KeyCode.ENTER && (!chatField.getText().equals(""))) {
                clientSocket.sendMessage(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " [" + username + "]" + " - " + chatField.getText());
                chatField.clear();
                chatField.requestFocus();
            }
        });
    }

    private void buildChatList() {
        chatArea = new TextArea();
        chatArea.setPrefWidth(500);
        chatArea.setEditable(false);
        chatArea.setWrapText(true);
        chatArea.setPadding(new Insets(0, 0, 0, 5));

    }

    private void buildUsersChatList() {
        userList = new ListView();
        userList.setPrefWidth(200);
        //ObservableList<String> users;
        //users = FXCollections.observableArrayList(loggedUser);
        //userList.setItems(users);
    }

}
