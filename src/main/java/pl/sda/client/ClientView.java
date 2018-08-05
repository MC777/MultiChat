package pl.sda.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    TextField loginField;
    static TextField chatField;
    Scene chatScene;
    Stage window;
    ListView userList;
    static TextArea chatArea;
    ClientThreadService clientSocket;

    private static final String HOSTNAME = "localhost";
    private static final Integer PORT = 8434;
    private String username = "Anonymous";

    public static void main(String[] args) {
        launch(args);
        ClientThreadService threadService = null;

        //TODO: to do odzielnej klasy np connect zaincjuj   ClientThreadService clientSocket; bo teraz jest nullem
       //TODO:
        //threadService.sendMessage(SCANNER.nextLINE);


    }

    private void connectWithServer(){
        try {
            Socket socket = new Socket(HOSTNAME,PORT);
            System.out.println("You are connected to " + HOSTNAME + " port " + PORT);
            sendUsernameToServer(socket);
            clientSocket = new ClientThreadService(socket);
            Thread thread = new Thread(clientSocket);
            thread.start();

            //clientSocket= new Socket(HOSTNAME,PORT);
            //threadService = new ClientThreadService(clientSocket);
            //Thread thread = new Thread(threadService);
            //thread.start();
        } catch (IOException e) {
            System.err.println("Can not connect to server");
            e.printStackTrace();
        }
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        buildLoginLable();
        buildLoginInputField();
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

        loginView.getChildren().addAll(loginLable, loginField, loginButton,infoLable);

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

            if(loginField.getText().equals("")){
                infoLable.setText("Please provide login!");
            } else {
                username = loginField.getText().trim();
                primaryStage.setTitle(username.toUpperCase() + " - CHAT VIEW");
                window.setScene(chatScene);
            }
        });
    }

    private void buildLoginInputField() {
        loginField = new TextField();
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
            Platform.exit();
            System.exit(0);
        });

    }

    private void buildChatTextField() {
        chatField = new TextField();
        chatField.setPrefWidth(500);
        chatField.setPromptText("Type something and press ENTER");
        chatField.setOnKeyPressed(b -> {
            if (b.getCode() == KeyCode.ENTER && (!chatField.getText().equals(""))){
                //TODO:
                //LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                clientSocket.sendMessage(chatField.getText());

                chatField.clear();
                chatField.requestFocus();
            }
        });
    }

    private void buildChatList() {
        chatArea = new TextArea();
        chatArea.setPrefWidth(500);
        chatArea.setEditable(false);
        chatArea.setPadding(new Insets(0,0,0,5));

    }

    private void buildUsersChatList() {
        userList = new ListView();
        userList.setPrefWidth(200);
        //TODO:
        String loggedUser = loginField.getText();
        ObservableList<String> users = FXCollections.observableArrayList (loggedUser);
        userList.setItems(users);
    }

}
