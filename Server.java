import java.io.*;
import java.net.*;

import connection.ConnectionManager;
import controller.Controller;

public class Server {
    private final static int PORT = 5075;
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.toString());

                ClientHandler clientThread = new ClientHandler(clientSocket);
                clientThread.start();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static class ClientHandler extends Thread {
        private static int threadNum = 0;
        private ConnectionManager connection;
        private final int width = 350;
        private final int height = 350;

        ClientHandler(Socket socket) {
            super("client-" + (++threadNum));
            this.connection = new ConnectionManager(socket);
        }

        public void run() {
            Controller controller = new Controller(this.connection, this.width, this.height);
            controller.openConnectionListener();
        }
    }
}


