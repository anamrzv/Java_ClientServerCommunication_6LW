package itmo.lab.server;

import javax.net.ServerSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerConnection {

    private static boolean listenerIsAlive = true;

    public static void main(String[] args) {
        ServerListener serverListener;
        try {
            serverListener = new ServerListener();
            serverListener.start();
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerListener extends Thread {
        private final ServerSocket socket;

        public ServerListener() throws IOException {
            socket = ServerSocketFactory.getDefault().createServerSocket(4004);
        }

        @Override
        public void run() {
            while (listenerIsAlive) {
                System.out.println("Wait connections...");
                try {
                    Socket clientSocket = socket.accept();
                    System.out.println("New connection created, address:" + clientSocket.getInetAddress());
                    new ClientHandler(clientSocket).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    listenerIsAlive = false;
                }
            }
        }
    }

    static class ClientHandler extends Thread {
        private final Socket socket;
        private final ObjectInputStream inputStream;
        private final ObjectOutputStream outputStream;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            while (socket.isConnected()) {
                try {
                    Object o = inputStream.readObject(); // need correct cast to server object
                    System.out.println("Client object: " + o);
                    outputStream.writeObject("Hello"); // send here answer to client
                    outputStream.flush();
                } catch (Exception e) {
                    try {
                        inputStream.close();
                        outputStream.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}