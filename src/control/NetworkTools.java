package control;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Sugirjan on 16/12/16.
 */
public class NetworkTools {
    private static NetworkTools instance;

    private NetworkTools() {

    }
    //singeleton implementation
    public static NetworkTools getInstance() {
        if (instance == null) {
            instance = new NetworkTools();
        }
        return instance;
    }

    //to send message for game server
    public void sendMessage(String msg) {
        try (Socket serverSocket = new Socket(Config.SERVER_IP, Config.SERVER_PORT);) {
            if (serverSocket.isConnected()) {
                try (BufferedWriter output = new BufferedWriter(
                        new OutputStreamWriter(serverSocket.getOutputStream()));) {
                    output.write(msg);
                }
            }

        } catch (IOException e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    //to receive message from game server
    public String receiveMessage() {
        String readLine = "#";
        try (ServerSocket ServerSocketForClient = new ServerSocket(Config.CLIENT_PORT);
             Socket clientSocket = ServerSocketForClient.accept();) {
            if (ServerSocketForClient.isBound()) {
                try (BufferedReader input = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));) {
                    while (!input.ready()) {
                        Thread.sleep(500);
                    }
                    readLine = input.readLine();
                    return readLine.split("#")[0];
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Exception: " + e.getMessage());
        }
        return "";
    }
}
