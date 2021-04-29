package itmo.lab.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Message;
import itmo.lab.other.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

public class ClientConnection {
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules().configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) {
        try {
            try {
                InputHandler ih = null;
                clientSocket = new Socket("localhost", 4004);
                System.out.println("Создан сокет");

                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();

                System.out.println("Клиент запущен");

                ByteBuffer buffer = ByteBuffer.allocate(4096);
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
                while (clientSocket.isConnected()) {
                    try {
                        Message message = ih.setStart();
                        if (message == null) continue;
                        out.write(OBJECT_MAPPER.writeValueAsBytes(message));
                        out.flush();

                        buffer.clear();
                        int serverAnswer = in.read(buffer.array());
                        if (serverAnswer > 0) {
                            ServerResponse sr = OBJECT_MAPPER.readValue(buffer.array(), ServerResponse.class);
                            if (sr.getError()==null) {
                                System.out.println(sr.getMessage());
                            } else System.out.println(sr.getError());
                        }
                        buffer.flip();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            } catch (UnknownHostException e) {
                System.out.println("Неизвестный хост");
                e.printStackTrace();
            } finally {
                System.out.println("Клиент закрыт");
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println("Ошибка клиента" + e.getMessage());
        }
    }
}