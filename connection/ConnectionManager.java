package connection;

import java.io.*;
import java.net.*;

public class ConnectionManager {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ConnectionManager(String serverAddress, int serverPort) {
        try {
            this.socket = new Socket(serverAddress, serverPort);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConnectionManager(Socket socket) {
        try {
            this.socket = socket;
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream()), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendData(String data) {
        if (out != null) {
            out.println(data);
        }
    }

    public String receiveData() {
        try {
            if (in != null) {
                return in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

