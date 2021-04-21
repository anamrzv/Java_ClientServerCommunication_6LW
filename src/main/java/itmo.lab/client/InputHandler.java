package itmo.lab.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import itmo.lab.other.Message;
import itmo.lab.other.Person;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class InputHandler {

    private final HashSet<String> names = new HashSet<>();
    private final OtherCollections collections;

    {
        collections = new OtherCollections();
        names.add("add_if_max");
        names.add("add_if__min");
        names.add("clear");
        names.add("count_less_than_passport_id");
        names.add("execute_script");
        names.add("head");
        names.add("help");
        names.add("info");
        names.add("remove_by_id");
        names.add("remove_all_by_passport_id");
        names.add("show");
        names.add("add");
        names.add("sum_of_weight");
        names.add("update");
    }

    private byte[] run() {
        do {
            try {
                System.out.print(">");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String input = br.readLine().trim();
                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Клиент закрыт");
                    System.exit(0);
                } else {
                    String cmd = getCommandName(input);
                    List<String> args = getArguments(input);
                    if (names.contains(cmd)) {
                        ObjectMapper objectMapper = new ObjectMapper();
                        if (cmd.equals("add")) {
                            CreatePerson creation = new CreatePerson(collections);
                            Person newPerson = creation.setCreation(args);
                            return objectMapper.writeValueAsBytes(new Message("add", newPerson));
                        }
                        return objectMapper.writeValueAsBytes(new Message(cmd, args));
                    } else {
                        System.out.println("Пожалуйста, повторите ввод: команда не распознана");
                    }
                }
            } catch (Exception e) {
                System.out.println(e + "\nНеверный формат ввода команды. Введите команду еще раз.");
            }
        } while (true);
    }

    /**
     * Метод - возвращает имя команды
     *
     * @param input - строка
     * @return String - имя команды
     */
    public String getCommandName(String input) {
        String[] elements = input.split(" +");
        return elements[0].toLowerCase(); //только название команды
    }

    /**
     * Метод - возвращает аргументы команды
     *
     * @param input - строка
     * @return String[] - аргументы команды
     */
    public List<String> getArguments(String input) {
        List<String> elements = Arrays.stream(input.split(" +")).collect(Collectors.toList());
        if (elements.size() > 1) {
            return elements.stream().skip(0).collect(Collectors.toList());
        } else return null;
    }

    public byte[] setStart() {
        return run();
    }
}
