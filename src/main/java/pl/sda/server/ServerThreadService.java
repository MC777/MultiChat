package pl.sda.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import static pl.sda.server.ChatServer.sockets;
import static pl.sda.server.ChatServer.users;

public class ServerThreadService implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ServerThreadService(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) throws IOException {
        for (Socket socket : sockets) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(message);
            printWriter.flush();
        }
    }

    private void removeSocket() {
        for (Socket socket : sockets) {
            if (this.socket == socket) {
                sockets.remove(socket);
            }
        }
    }

    private void addUser() throws IOException {
        String user = reader.readLine();
        users.add(user);
        sendMessage(user + " - has joined to the chat");
    }

    private void removeUser(String user) {
        users.remove(user);
    }

    @Override
    public void run() {
        try {
            writer = new PrintWriter(this.socket.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            addUser();
            sendMessage("<userLst>" + users);
            while (true) {
                String message;
                if ((message = reader.readLine()) == null) {
                    return;
                }
                if (message.contains("<levCht>")) {
                    sendMessage(message.substring(8) + " leave chat");
                    removeUser(message.substring(8));
                    sendMessage("<userLst>" + users);
                    removeSocket();
                    return;
                }
                sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                writer.close();
                reader.close();
            } catch (IOException e) {
                System.err.println("Can not close streams");
                e.printStackTrace();
            }
        }
    }
}
