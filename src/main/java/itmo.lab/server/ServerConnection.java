package itmo.lab.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.lab.other.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
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
        private final InputStream inputStream;
        private final OutputStream outputStream;

        public ClientHandler(SocketChannel sChannel) throws IOException {
            this.sChannel = sChannel;
            outputStream = sChannel.socket().getOutputStream();
            inputStream = sChannel.socket().getInputStream();
        }

        @Override
        public void run() {
            while (sChannel.isOpen()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    Message newMessage = objectMapper.readValue(inputStream, Message.class);//неправильно
                    System.out.println("Client object: " + newMessage);

                    //outputStream.writeObject("Hello"); // send here answer to client
                    //outputStream.flush();
                } catch (Exception e) {
                    try {
                        inputStream.close();
                        outputStream.close();
                        sChannel.close();
                        e.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}