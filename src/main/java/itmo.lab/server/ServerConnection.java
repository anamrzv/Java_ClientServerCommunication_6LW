package itmo.lab.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

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
        private final ServerSocketChannel ssChannel;

        public ServerListener() throws IOException {
            SocketAddress a = new InetSocketAddress(4004);
            ssChannel = ServerSocketChannel.open();
            ssChannel.socket().bind(a);
        }

        @Override
        public void run() {
            while (listenerIsAlive) {
                System.out.println("Wait connections...");
                try {
                    SocketChannel socketChannel = ssChannel.accept();
                    System.out.println("New connection created");
                    new ClientHandler(socketChannel).start();
                } catch (IOException e) {
                    e.printStackTrace();
                    listenerIsAlive = false;
                }
            }
        }
    }

    static class ClientHandler extends Thread {
        private final SocketChannel sChannel;
        private final ObjectInputStream inputStream;
        private final ObjectOutputStream outputStream;

        public ClientHandler(SocketChannel sChannel) throws IOException {
            this.sChannel = sChannel;
            outputStream = new ObjectOutputStream(sChannel.socket().getOutputStream());
            inputStream = new ObjectInputStream(sChannel.socket().getInputStream());
        }

        @Override
        public void run() {
            while (sChannel.isConnected()) {
                try {
                    ServerObject so = (ServerObject) inputStream.readObject(); // need correct cast to server object

                    System.out.println("Client object: " + so);

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