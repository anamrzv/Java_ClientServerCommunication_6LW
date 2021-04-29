package itmo.lab.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerConnection {

    private static boolean listenerIsAlive = true;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final CollectionsKeeper collectionsKeeper = new CollectionsKeeper(); // главная коллекция людей!!!

    public static void main(String[] args) {
        ServerListener serverListener;
        try {
            serverListener = new ServerListener();
            System.out.println("Server started");
            serverListener.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ServerListener extends Thread {
        private final ServerSocketChannel ssChannel;

        public ServerListener() throws IOException {
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(false);
            ssChannel.socket().bind(new InetSocketAddress(4004));
        }

        @Override
        public void run() {
            while (listenerIsAlive) {
                try {
                    SocketChannel socketChannel = ssChannel.accept();
                    if (socketChannel != null) {
                        System.out.printf("New connection created: %s%n", socketChannel.getRemoteAddress());
                        DocumentHandler dh = new DocumentHandler(collectionsKeeper);
                        try {
                            dh.setRead();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new ClientHandler(socketChannel).start();
                    }
                } catch (IOException e) {
                    try {
                        e.printStackTrace();
                        listenerIsAlive = false;
                        ssChannel.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    static class ClientHandler extends Thread {
        private final SocketChannel sChannel;

        public ClientHandler(SocketChannel sChannel) {
            this.sChannel = sChannel;
        }

        @SneakyThrows
        @Override
        public void run() {
            sChannel.configureBlocking(false);
            if (sChannel.isOpen()) {
                sChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(collectionsKeeper)));
            }
            while (sChannel.isOpen()) {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(4096);
                    while (true) {
                        buffer.clear();
                        int read = sChannel.read(buffer); // non-blocking
                        if (read < 0) {
                            break;
                        }
                        if (read > 0) {
                            ServerResponse serverResponse = handleClientMessage(OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            sChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                        }
                        buffer.flip();
                    }
                } catch (Exception e) {
                    try {
                        sChannel.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

    private static ServerResponse handleClientMessage(Message message) {
        CommandHandler command;
        if (message == null) {
            return ServerResponse.builder().error("The client sent incorrect data").build();
        }
        if (message.getCommandName().equals("add")) {
            command = new CommandHandler(message.getCommandName(), message.getPerson(), collectionsKeeper);
        } else if (message.getCommandName().equals("update")) {
            command = new CommandHandler(message.getCommandName(), message.getCommandArgs(), message.getPerson(), collectionsKeeper);
        } else {
            command = new CommandHandler(message.getCommandName(), message.getCommandArgs(), collectionsKeeper);
        }
        return command.setRun();
        //return ServerResponse.builder().message("Hello, i am server").command(message.getCommandName()).build();
    }
}