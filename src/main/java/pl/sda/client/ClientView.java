package pl.sda.client;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by MCK on 04.08.2018 14:52
 **/
public class ClientView extends Application {

    Button loginButton;
    Button disconnectButton;
    Label loginLable;
    TextField loginField;
    TextField chatField;
    Scene chatScene;
    Stage window;
    ListView userList;
    ListView chatList;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;

        buildLoginLable();
        buildLoginInputField();
        buildLoginButton();

        primaryStage.setTitle("Anonymouse");

        GridPane loginView = new GridPane();
        loginView.setPadding(new Insets(30, 5, 20, 5));
        loginView.setHgap(5);
        GridPane.setConstraints(loginLable, 0, 0);
        GridPane.setConstraints(loginField, 1, 0);
        GridPane.setConstraints(loginButton, 2, 0);
        loginView.getChildren().addAll(loginLable, loginField, loginButton);

        BorderPane borderPanel = new BorderPane();
        borderPanel.setCenter(loginView);
        Scene loginScene = new Scene(borderPanel, 320, 100);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        buildChatView();

    }

    private void buildLoginButton() {
        loginButton = new Button();
        loginButton.setText("Login");
        loginButton.setOnAction((event) -> {
            window.setScene(chatScene);

            //System.out.println("Button clicked");
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

    private void buildChatView() {

        GridPane chatView = new GridPane();
        chatView.setPadding(new Insets(10, 10, 10, 10));
        chatView.setHgap(5);
        chatView.setVgap(5);
        buildDisconnectButton();
        builChatTextField();
        buildChatList();
        buildUsersChatList();
        chatView.setConstraints(disconnectButton, 0, 0);
        chatView.setConstraints(chatList, 0, 1);
        chatView.setConstraints(chatField, 0, 2);
        chatView.setConstraints(userList, 1, 1);

        chatView.getChildren().addAll(disconnectButton, chatField, chatList, userList);

        BorderPane borderPanel = new BorderPane();
        borderPanel.setCenter(chatView);
        chatScene = new Scene(borderPanel, 730, 500);
    }

    private void buildDisconnectButton() {
        disconnectButton = new Button();
        disconnectButton.setText("Disconnect");
        disconnectButton.setOnAction(event -> {
            System.out.println("Button clicked");
        });

    }

    private void builChatTextField() {
        chatField = new TextField();
        chatField.setPrefWidth(500);
        chatField.setPromptText("Type something and press ENTER");
    }

    private void buildChatList() {
        chatList = new ListView();
        chatList.setPrefWidth(500);
//        ObservableList<String> items = FXCollections.observableArrayList (
//                "Line1", "Line2", "Line3", "Line4");
//        userList.setItems(items);
    }

    private void buildUsersChatList() {
        userList = new ListView();
        userList.setPrefWidth(200);

//        ObservableList<String> items = FXCollections.observableArrayList (
//                "User1", "User2", "User3", "User4");
//        userList.setItems(items);
    }

}
