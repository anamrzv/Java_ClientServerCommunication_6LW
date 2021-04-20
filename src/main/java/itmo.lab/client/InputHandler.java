package itmo.lab.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.HashSet;

public class InputHandler {

    private final HashSet<String> names = new HashSet<>();

    {
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

    private ServerObject run() {
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
                    String[] args = getArguments(input);
                    if (names.contains(cmd)) {
                        return new ServerObject(cmd, args);
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
    public String[] getArguments(String input) {
        String[] elements = input.split(" +");
        if (elements.length > 1) {
            String[] args = new String[elements.length - 1];
            System.arraycopy(elements, 1, args, 0, args.length);
            return args;
        } else return null;
    }

    public ServerObject setStart() {
        return run();
    }
}
