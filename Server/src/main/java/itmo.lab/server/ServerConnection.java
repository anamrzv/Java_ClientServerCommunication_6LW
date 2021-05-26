package itmo.lab.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itmo.lab.commands.SpecialSave;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


@Slf4j
public class ServerConnection {

    private static boolean listenerIsAlive = true;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final CollectionsKeeper collectionsKeeper = new CollectionsKeeper();
    private static Selector selector;
    private static int connectionCount = 0;

    public static void main(String[] args) {
        ServerListener serverListener;
        int port = 6667;
        try {

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Экстренное закрытие сервера.");
                SpecialSave save = new SpecialSave(collectionsKeeper);
                save.execute();
            }));

            if (args.length != 0) {
                try {
                    port = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    System.out.println("Порт должен быть числом");
                    System.exit(-1);
                }
                if (port <= 0) {
                    System.out.println("Порт не может быть отрицательным.");
                    System.exit(-1);
                } else if (port > 65535) {
                    System.out.println("Порт должен лежать в пределах 1-65535");
                    System.out.println(-1);
                }
            }
            selector = Selector.open();
            log.info("Selector opened");
            serverListener = new ServerListener(port);
            log.info("Server started");
            System.out.println("Server started");
            serverListener.start();
        } catch (IOException e) {
            System.out.println("Ошибка при создании сервера");
        }
    }

    static class ServerListener extends Thread {
        private final ServerSocketChannel ssChannel;
        int port;

        public ServerListener(int port) throws IOException {
            this.port = port;
            ssChannel = ServerSocketChannel.open();
            ssChannel.configureBlocking(false);
            try {
                ssChannel.socket().bind(new InetSocketAddress(port));
                log.info("Port bounded");
            } catch (IOException e) {
                System.out.println("Невозможно прослушать порт. Выберите другой и перезапустите сервер.");
                log.error("Can't listen port");
                System.exit(-1);
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
                            log.info("New connection created: {}", socketChannel.getRemoteAddress());
                            socketChannel.configureBlocking(false);
                            connectionCount += 1;
                            if (connectionCount == 1) {
                                DocumentHandler dh = new DocumentHandler(collectionsKeeper);
                                try {
                                    dh.setRead();
                                    log.info("Document loaded");
                                } catch (Exception e) {
                                    log.error("Document wasn't loaded");
                                    System.out.println("Ошибка при загрузке коллекции из документа");
                                }
                            }
                            new ClientHandler(socketChannel).start();
                        }
                    } catch (IOException e) {
                        try {
                            System.out.println("Соединение с клиентом отсутствует");
                            log.error("Connection with client is lost");
                            listenerIsAlive = false;
                            ssChannel.close();
                        } catch (IOException ioException) {
                            System.out.println("Ошибка при закрытии сервера");
                            log.error("Error while closing server");
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
                log.info("Document sent");
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
                            log.info("Got {}", OBJECT_MAPPER.readValue(buffer.array(), Message.class));
                            sChannel.write(ByteBuffer.wrap(OBJECT_MAPPER.writeValueAsBytes(serverResponse)));
                            if (serverResponse.getMessage()!=null && serverResponse.getMessage().equals("Disconnected successfully")) {
                                log.info("Client {} disconnected", sChannel.getRemoteAddress());
                                System.out.println("Клиент " + sChannel.getRemoteAddress() + " отсоединен");
                                if (serverResponse.getError() != null) System.out.println(serverResponse.getError());
                                sChannel.close();
                                break;
                            }
                        }
                        buffer.flip();
                    }
                } catch (Exception e) {
                    try {
                        System.out.println("Соединение с клиентом" + sChannel.getRemoteAddress() + " экстренно прекращено");
                        SpecialSave save = new SpecialSave(collectionsKeeper);
                        save.execute();
                        sChannel.close();
                    } catch (IOException ioException) {
                        System.out.println("Ошибка при закрытии клиента");
                    }
                }
            }
        }
    }

    /**
     * Обработка сообщения от клиента
     *
     * @param message Данные о команде
     * @return ServerResponse
     */
    private static ServerResponse handleClientMessage(Message message) {
        CommandHandler command;
        if (message == null) {
            return ServerResponse.builder().error("The client sent incorrect data").build();
        }
        if (message.getCommandName().equalsIgnoreCase("exit")) {
            SpecialSave save = new SpecialSave(collectionsKeeper);
            String error = save.execute();
            connectionCount -= 1;
            return ServerResponse.builder().error(error).message("Disconnected successfully").build();
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