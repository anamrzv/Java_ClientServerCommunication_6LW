package itmo.lab.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itmo.lab.commands.SpecialSave;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class ServerConnection {

    private static final Logger logger = LoggerFactory.getLogger(itmo.lab.server.ServerConnection.class);
    private static boolean listenerIsAlive = true;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final CollectionsKeeper collectionsKeeper = new CollectionsKeeper();
    private static Selector selector;
    private static int connectionCount = 0;

    public static void main(String[] args) {
        ServerListener serverListener;
        try {
            selector = Selector.open();
            logger.info("Selector opened");
            serverListener = new ServerListener();
            logger.info("Server started");
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
            try {
                ssChannel.socket().bind(new InetSocketAddress(6595));
                logger.info("Port bounded");
            } catch (IOException e) {
                System.out.println("Can't listen port");
                logger.error("Can't listen port");
                System.exit(1);
            }
        }

        @SneakyThrows
        @Override
        public void run() {
            ssChannel.register(selector, ssChannel.validOps());
            while (true) {
                while (listenerIsAlive) {
                    try {
                        SocketChannel socketChannel = ssChannel.accept();
                        if (socketChannel != null) {
                            System.out.printf("New connection created: %s%n", socketChannel.getRemoteAddress());
                            logger.info("New connection created: {}", socketChannel.getRemoteAddress());
                            socketChannel.configureBlocking(false);
                            connectionCount += 1;
                            if (connectionCount == 1) {
                                DocumentHandler dh = new DocumentHandler(collectionsKeeper);
                                try {
                                    dh.setRead();
                                    logger.info("Document loaded");
                                } catch (Exception e) {
                                    logger.error("Document wasn't loaded");
                                    System.out.println("Ошибка при загрузке коллекции из документа");
                                }
                            }
                            new ClientHandler(socketChannel).start();
                        }
                    } catch (IOException e) {
                        try {
                            System.out.println("Соединение с клиентом отсутствует");
                            logger.error("Connection with client is lost");
                            listenerIsAlive = false;
                            ssChannel.close();
                        } catch (IOException ioException) {
                            System.out.println("Ошибка при закрытии сервера");
                            logger.error("Error while closing server");
                        }
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
                logger.info("Document sent");
            }
            while (sChannel.isOpen()) {
                try {
                    ByteBuffer buffer = ByteBuffer.allocate(5120);
                    while (true) {
                        buffer.clear();
                        int read = sChannel.read(buffer); // non-blocking
                        if (read < 0) {
                            break;
                        }
                        if (read > 0) {
                            ServerResponse serverResponse = handleClientMessage(OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            logger.info("Got {}", OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            sChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                            if (serverResponse.getMessage().equals("Server is disconnected")) {
                                logger.info("Client {} disconnected", sChannel.getRemoteAddress());
                                System.out.println("Клиент "+sChannel.getRemoteAddress()+" отсоединен");
                                sChannel.close();
                                if (connectionCount == 0) {
                                    listenerIsAlive = false;
                                    logger.info("Server closed");
                                    System.out.println("Сервер закрыт");
                                    System.exit(0);
                                    break;
                                }
                                break;
                            }
                        }
                        buffer.flip();
                    }
                } catch (Exception e) {
                    try {
                        System.out.println("Соединение с клиентом прекращено");
                        sChannel.close();
                    } catch (IOException ioException) {
                        System.out.println("Ошибка при закрытии сервера");
                    }
                }
            }
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
            connectionCount -= 1;
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