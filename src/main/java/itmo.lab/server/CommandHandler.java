package itmo.lab.server;

import itmo.lab.commands.AddIfMax;
import itmo.lab.commands.AddIfMin;
import itmo.lab.commands.Clear;
import itmo.lab.commands.Command;
import itmo.lab.commands.CountLessPass;
import itmo.lab.commands.ExecuteScript;
import itmo.lab.commands.Head;
import itmo.lab.commands.Help;
import itmo.lab.commands.Info;
import itmo.lab.commands.RemoveByID;
import itmo.lab.commands.Show;
import itmo.lab.commands.SimpleAdd;
import itmo.lab.commands.SumOfWeight;
import itmo.lab.commands.Update;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler {
    private final String commandName;
    private final String[] commandArgs;
    private final Map<String, Command> namesAndCommands = new HashMap<>();
    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionsKeeper ck;

    {
        ck = new CollectionsKeeper();
    }

    public CommandHandler(String commandName, String [] commandArgs){
        this.commandArgs=commandArgs;
        this.commandName=commandName;
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

    private void run(){
        switch (commandName){
            case "add_if_max":
                Command c = new AddIfMax(ck);
                c.execute(commandArgs);
                break;
            case "add_if_min":
                c = new AddIfMin(ck);
                c.execute(commandArgs);
                break;
            case "clear":
                c = new Clear(ck);
                c.execute(commandArgs);
                break;
            case "count_less_than_passport_id":
                c = new CountLessPass(ck);
                c.execute(commandArgs);
                break;
            case "execute_script":
                c = new ExecuteScript(ck, this);
                c.execute(commandArgs);
                break;
            case "head":
                c = new Head(ck);
                c.execute(commandArgs);
                break;
            case "help":
                c = new Help(ck, this);
                c.execute(commandArgs);
                break;
            case "info":
                c = new Info(ck);
                c.execute(commandArgs);
                break;
            case "remove_by_id":
                c = new RemoveByID(ck);
                c.execute(commandArgs);
                break;
            case "show":
                c = new Show(ck);
                c.execute(commandArgs);
                break;
            case "sum_of_weight":
                c = new SumOfWeight(ck);
                c.execute(commandArgs);
                break;
            case "update":
                c = new Update(ck);
                c.execute(commandArgs);
                break;
            case "add":
                c = new SimpleAdd(ck, commandArgs);
                c.execute();
                break;
        }
    }
}
