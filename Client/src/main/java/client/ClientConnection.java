package client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import other.CollectionsKeeper;
import other.Message;
import other.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.PortUnreachableException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * Главный клиентский класс, создает потоки ввода-вывода и устанавливает соединение
 */
public class ClientConnection {
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) {
        int port = 6667;
        try {
            try {
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
                InputHandler ih = null;
                clientSocket = new Socket("localhost", port);
                System.out.println("Создан сокет");

                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();
                System.out.println("Клиент запущен");

                ByteBuffer buffer = ByteBuffer.allocate(5120);
                try {
                    if (clientSocket.isConnected()) {
                        try {
                            buffer.clear();
                            int serverAnswer = in.read(buffer.array());
                            if (serverAnswer > 0) {
                                CollectionsKeeper ck = OBJECT_MAPPER.readValue(buffer.array(), CollectionsKeeper.class);
                                ih = new InputHandler(ck);
                            }
                            buffer.flip();
                        } catch (Exception e) {
                            System.out.println("Ошибка при получении сообщения от сервера");
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Отсутствует подключение к серверу.");
                    try {
                        in.close();
                        out.close();
                        clientSocket.close();
                        System.exit(0);
                    } catch (NullPointerException ne) {
                        System.out.println("Клиентский сокет не был создан");
                    }
                }
                while (clientSocket.isConnected()) {
                    try {
                        Message message = ih.setStart();
                        if (message == null) continue;
                        out.write(OBJECT_MAPPER.writeValueAsBytes(message));
                        out.flush();
                        if (message.getCommandName().equalsIgnoreCase("exit")) {
                            System.out.println("Client kills connection");
                            Thread.sleep(1000);
                            handleRequest(buffer);
                            System.exit(0);
                        }
                        buffer.clear();
                        handleRequest(buffer);
                        buffer.flip();
                    } catch (IOException e) {
                        System.out.println("Отсутствует подключение к серверу. Клиент отключается.");
                        try {
                            in.close();
                            out.close();
                            clientSocket.close();
                            System.exit(0);
                        } catch (NullPointerException ne) {
                            System.out.println("Клиентский сокет не был создан");
                        }
                    } catch (InterruptedException e) {
                        System.out.println("Ожидающий поток был прерван");
                    } catch (NullPointerException e) {
                        System.out.println("Команда не может быть обработана.");
                    }
                }
            } catch (PortUnreachableException e) {
                System.out.println("Не удалось получить данные по указанному порту/сервер не доступен");
            } catch (UnknownHostException e) {
                System.out.println("Неизвестный хост");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("Ошибка при подключении к серверу. Выберите другой порт.");
                System.exit(-1);
            } finally {
                try {
                    clientSocket.close();
                    in.close();
                    out.close();
                    System.out.println("Клиент закрыт");
                } catch (NullPointerException e) {
                    System.out.println("Клиентский сокет не был создан");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента " + e.getMessage());
        }
    }

    private static void handleRequest(ByteBuffer buffer) throws IOException {
        int serverAnswer = in.read(buffer.array());
        if (serverAnswer > 0) {
            ServerResponse sr = OBJECT_MAPPER.readValue(buffer.array(), ServerResponse.class);
            if (sr.getError() == null) {
                System.out.println(sr.getMessage());
            } else System.out.println(sr.getError());
        }
    }
}