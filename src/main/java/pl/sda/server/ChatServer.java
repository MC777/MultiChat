package pl.sda.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
    private static final Integer PORT = 8434;
    static List<Socket> sockets = new ArrayList<>();
    static List<String> users = new ArrayList<>();
    public static String[] usersList;

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Started server at port: " + PORT);
            while (true) {
                System.out.println("Server waiting for client...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client added");
                sockets.add(clientSocket);
                ServerThreadService threadService = new ServerThreadService(clientSocket);
                Thread thread = new Thread(threadService);
                thread.start();
            }

        } catch (IOException e) {
            System.err.println("Can not create server");
            e.printStackTrace();
        }
    }

    public static List<String> getUsers() {
        return users;
    }

    public static void setUsers(List<String> users) {
        ChatServer.users = users;

    }

    public static void addUser(String u) {
        users.add(u);
    }

    public static void updateUsersList(String[] list) {
        usersList = list;
        try {
            PrintWriter pr = new PrintWriter("file.txt");

            for (int i = 0; i < usersList.length; i++) {
                pr.println(usersList[i]);
            }
            pr.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("No such file exists.");
        }
    }
}
