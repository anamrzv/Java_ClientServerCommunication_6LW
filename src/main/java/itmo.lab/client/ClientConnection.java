package itmo.lab.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection {
    private static Socket clientSocket;
    private static OutputStream out;
    private static InputStream in;

    public static void main(String[] args) {
        try {
            try {
                InputHandler ih = new InputHandler();
                clientSocket = new Socket("localhost", 4004);
                System.out.println("Создан сокет");

                out = clientSocket.getOutputStream();
                in = clientSocket.getInputStream();

                System.out.println("Клиент запущен");

                while (clientSocket.isConnected()) {
                    try {
                        byte[] jsonBytes = ih.setStart();
                        out.write(jsonBytes);
                        out.flush();

                        int serverAnswer = in.read(); //ждем ответ сервера, т.е. строку-результат выполненной команды
                        System.out.println(serverAnswer);
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