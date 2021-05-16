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

public class ClientConnection {
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().registerModule(new JavaTimeModule()).configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) {
        try {
            try {
                InputHandler ih = null;
                clientSocket = new Socket("localhost", 6592);
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
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Отсутствует подключение к серверу");
                }
                while (clientSocket.isConnected()) {
                    try {
                        Message message = ih.setStart();
                        if (message == null) continue;
                        out.write(OBJECT_MAPPER.writeValueAsBytes(message));
                        out.flush();
                        if (message.getCommandName().equalsIgnoreCase("exit")) {
                            System.out.println("Client kills connection");
                            Thread.sleep(2000);
                            int serverAnswer = in.read(buffer.array());
                            if (serverAnswer > 0) {
                                ServerResponse sr = OBJECT_MAPPER.readValue(buffer.array(), ServerResponse.class);
                                if (sr.getError() == null) {
                                    System.out.println(sr.getMessage());
                                } else System.out.println(sr.getError());
                            }
                            System.exit(0);
                        }

                        buffer.clear();
                        int serverAnswer = in.read(buffer.array());
                        if (serverAnswer > 0) {
                            ServerResponse sr = OBJECT_MAPPER.readValue(buffer.array(), ServerResponse.class);
                            if (sr.getError() == null) {
                                System.out.println(sr.getMessage());
                            } else System.out.println(sr.getError());
                        }
                        buffer.flip();
                    } catch (IOException e) {
                        System.out.println("Отсутствует подключение к серверу");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
                System.out.println("Ошибка при подключении к серверу");
            } finally {
                System.out.println("Клиент закрыт");
                try {
                    clientSocket.close();
                    in.close();
                    out.close();
                } catch (NullPointerException e) {
                    System.out.println("Клиентский сокет не был создан");
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента " + e.getMessage());
        }
    }
}