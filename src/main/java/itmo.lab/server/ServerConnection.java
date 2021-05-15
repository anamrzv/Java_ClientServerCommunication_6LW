package itmo.lab.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerConnection {

    private static boolean listenerIsAlive = true;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final CollectionsKeeper collectionsKeeper = new CollectionsKeeper(); // главная коллекция людей!!!

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        System.out.println("Server started");
        serverSocketChannel.bind(new InetSocketAddress(4004));
        serverSocketChannel.register(selector, serverSocketChannel.validOps());
        while (true) {
            while (listenerIsAlive) {
                SocketChannel socketChannel = serverSocketChannel.accept(); // non-blocking
                if (socketChannel != null) {
                    System.out.printf("New connection created: %s%n", socketChannel.getRemoteAddress());
                    socketChannel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(5120);
                    DocumentHandler dh = new DocumentHandler(collectionsKeeper);
                    try {
                        dh.setRead();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (socketChannel.isOpen()) {
                        socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(collectionsKeeper)));
                    }
                    while (true) {
                        buffer.clear();
                        int read = socketChannel.read(buffer); // non-blocking
                        if (read < 0) {
                            break;
                        }
                        if (read > 0) {
                            ServerResponse serverResponse = handleClientMessage(OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                        }
                        buffer.flip();
                    }
                    socketChannel.close();
                }
            }
            serverSocketChannel.close();
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
    }
}