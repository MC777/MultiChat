package pl.sda.client;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import pl.sda.server.ChatServer;

import java.awt.*;
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

    public void sendMessage(String message){
        writer.println(message);
        writer.flush();
    }

    public void disconnect(String username){
        writer.println("<levCht>" + username);
        writer.flush();
        System.exit(0);
    }

    @Override
    public void run() {
        try {
            while (true) {
                String message;
                if ((message = reader.readLine()) != null){
                    //TODO

                    ClientView.chatArea.appendText(message + "\n");
                    //System.out.println(message); // ta wartosc do TextArea append text
                }
            }
        } catch (IOException e){
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
}
