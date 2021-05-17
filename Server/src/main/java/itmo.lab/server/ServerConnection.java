package itmo.lab.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import itmo.lab.commands.SpecialSave;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class ServerConnection {

    /**
     * Логгер
     */
    private static final Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    /**
     * Слушатель
     */
    private static boolean listenerIsAlive = true;
    /**
     * Обработчик json в объект
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    /**
     * Главная первоначальная коллекция
     */
    private static final CollectionsKeeper collectionsKeeper = new CollectionsKeeper();

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();
        logger.info("Selector opened");
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        logger.info("Server started");
        System.out.println("Server started");
        try {
            serverSocketChannel.bind(new InetSocketAddress(6593));
            logger.info("Port bounded");
        } catch (IOException e) {
            System.out.println("Can't listen port");
            logger.error("Can't listen port");
            System.exit(1);
        }
        serverSocketChannel.register(selector, serverSocketChannel.validOps());
        while (true) {

            while (listenerIsAlive) {
                SocketChannel socketChannel = serverSocketChannel.accept(); // non-blocking
                if (socketChannel != null) {

                    System.out.printf("New connection created: %s%n", socketChannel.getRemoteAddress());
                    logger.info("New connection created: {}", socketChannel.getRemoteAddress());
                    socketChannel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(5120);
                    DocumentHandler dh = new DocumentHandler(collectionsKeeper);
                    try {
                        dh.setRead();
                        logger.info("Document loaded");
                    } catch (Exception e) {
                        logger.error("Document wasn't loaded");
                        e.printStackTrace();
                    }
                    if (socketChannel.isOpen()) {
                        socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(collectionsKeeper)));
                        logger.info("Document sent");
                    }
                    while (true) {
                        buffer.clear();
                        int read = socketChannel.read(buffer); // non-blocking
                        if (read < 0) {
                            break;
                        }
                        if (read > 0) {
                            ServerResponse serverResponse = handleClientMessage(OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            logger.info("Got {}", OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            socketChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                            if (serverResponse.getMessage().equals("Server is disconnected")) {
                                listenerIsAlive = false;
                                logger.info("Server is closing");
                                socketChannel.close();

                                System.exit(0);
                                break;
                            }
                        }
                        buffer.flip();
                    }

                }

            }
            serverSocketChannel.close();
        }

    }

    /**
     * Обработка сообщения от клиента
     *
     * @param message
     * @return ServerResponse
     */
    private static ServerResponse handleClientMessage(Message message) {
        CommandHandler command;
        if (message == null) {
            return ServerResponse.builder().error("The client sent incorrect data").build();
        }
        if (message.getCommandName().equalsIgnoreCase("exit")) {
            SpecialSave save = new SpecialSave(collectionsKeeper);
            save.execute();
            return ServerResponse.builder().message("Server is disconnected").build();
        } else if (message.getCommandName().equals("add")) {
            command = new CommandHandler(message.getCommandName(), message.getPerson(), collectionsKeeper);
        } else if (message.getCommandName().equals("update")) {
            command = new CommandHandler(message.getCommandName(), message.getCommandArgs(), message.getPerson(), collectionsKeeper);
        } else {
            command = new CommandHandler(message.getCommandName(), message.getCommandArgs(), collectionsKeeper);
        }
        return command.setRun();
    }
}