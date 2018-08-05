package pl.sda.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import static pl.sda.server.ChatServer.sockets;
import static pl.sda.server.ChatServer.users;


/**
 * Created by MCK on 04.08.2018 14:54
 **/
public class ServerThreadService implements Runnable {

    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;

    public ServerThreadService(Socket socket) {
        this.socket = socket;
    }

    private void addUser() throws IOException{
        String user = reader.readLine();
        users.add(user);
        sendMessage(user + " - has joined to the chat");
    }

    private void removeUser(String user){
        users.remove(user);
    }

    @Override
    public void run() {
        try {
//            writer = new PrintWriter(socket.getOutputStream());
//            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true){
                String message;
                if ((message = reader.readLine()) != null){
                    for (Socket s : ChatServer.sockets){
                        PrintWriter printWriter = new PrintWriter(s.getOutputStream());
                        printWriter.println(message);
                        printWriter.flush();
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
                System.err.println("Can not close streams");
                e.printStackTrace();
            }

        }
    }
}
