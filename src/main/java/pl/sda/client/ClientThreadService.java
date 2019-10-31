package pl.sda.client;

import javafx.application.Platform;
import pl.sda.server.ChatServer;

import java.io.*;
import java.net.Socket;

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
        trim(username);
        writer.println("<levCht>" + username);
        writer.flush();
        System.exit(0);

    }

    public void trim(String username){
        try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                } else {
                    if (username.equals(line)) {
                        line.trim();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                            ChatServer.updateUsersList(getUsers(message));
                            ChatServer.usersList = users;
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
