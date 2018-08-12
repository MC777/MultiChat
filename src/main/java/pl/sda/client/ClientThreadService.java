package pl.sda.client;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by MCK on 04.08.2018 14:53
 **/
public class ClientThreadService implements Runnable {
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ClientThreadService(Socket socket) throws IOException {
        this.socket = socket;
        writer = new PrintWriter(socket.getOutputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();
    }

    public void disconnect(String username) {
        writer.println("<levCht>" + username);
        writer.flush();
        System.exit(0);
    }

    @Override
    public void run() {
        try {
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            writer = new PrintWriter(this.socket.getOutputStream());
            while (true) {
                String message;
                if ((message = reader.readLine()) != null) {
                    if (message.contains("<userLst>")) {
                        String[] users = getUsers(message);
                        Platform.runLater(() -> {
                            ClientView.userList.getItems().clear();
                            ClientView.userList.getItems().addAll(users);
                            //ClientView.users.getItems().addAll(users);
                        });
                    } else {
                        ClientView.chatArea.appendText(message + "\n");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                writer.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private String[] getUsers(String message) {
        return message.substring(9)
                .replace("[", "")
                .replace("]", "")
                .split(", ");
    }
}
