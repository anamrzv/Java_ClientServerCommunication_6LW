package itmo.lab.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection {
    private static Socket clientSocket;
    private static ObjectOutputStream out;
    private static ObjectInputStream in;

    public static void main(String[] args) {
        try {
            try {
                InputHandler ih = new InputHandler();
                clientSocket = new Socket("localhost", 4004);
                System.out.println("Создан сокет");

                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("Клиент запущен");

                while (clientSocket.isConnected()) {
                    try {
                        ServerObject so = ih.setStart();

                        out.writeObject(so); // пишем сообщение (команда и ее аргументы) на сервер
                        out.flush();

                        String serverAnswer = (String) in.readObject(); //ждем ответ сервера, т.е. строку-результат выполненной команды
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