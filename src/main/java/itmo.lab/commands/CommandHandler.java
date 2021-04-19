package itmo.lab.commands;

import itmo.lab.server.CollectionsKeeper;
import itmo.lab.server.DocumentHandler;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionsKeeper ck = new CollectionsKeeper();

    {
        Command c = new Help(ck, this);
        commands.put(c.getName(), c);
        c = new Info(ck);
        commands.put(c.getName(), c);
        c = new Show(ck);
        commands.put(c.getName(), c);
        c = new RemoveByID(ck);
        commands.put(c.getName(), c);
        c = new Clear(ck);
        commands.put(c.getName(), c);
        c = new Head(ck);
        commands.put(c.getName(), c);
        c = new SumOfWeight(ck);
        commands.put(c.getName(), c);
        c = new RemoveByPass(ck);
        commands.put(c.getName(), c);
        c = new CountLessPass(ck);
        commands.put(c.getName(), c);
        c = new AddIfMax(ck);
        commands.put(c.getName(), c);
        c = new AddIfMin(ck);
        commands.put(c.getName(), c);
        c = new Update(ck);
        commands.put(c.getName(), c);
        c = new ExecuteScript(ck, this);
        commands.put(c.getName(), c);
    }

    public CommandHandler() {
        DocumentHandler doc = new DocumentHandler(ck);
        try {
            doc.setRead();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Command> getCommands() {
        return commands;
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
        String[] args;
        String[] elements = input.split(" +");
        if (elements.length > 1) {
            args = new String[elements.length - 1];
            System.arraycopy(elements, 1, args, 0, args.length);
            return args;
        } else return null;
    }
}

