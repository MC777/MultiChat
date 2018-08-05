package pl.sda.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MCK on 04.08.2018 14:53
 **/
public class ChatServer {
    private static final Integer PORT = 8434;
    static List<Socket> sockets = new ArrayList<>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            while (true){
                Socket clientSocket = serverSocket.accept();
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


}
