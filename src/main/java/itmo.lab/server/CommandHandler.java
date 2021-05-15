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
import itmo.lab.commands.RemoveByPass;
import itmo.lab.commands.Show;
import itmo.lab.commands.SimpleAdd;
import itmo.lab.commands.SumOfWeight;
import itmo.lab.commands.Update;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Person;
import itmo.lab.other.ServerResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandHandler {
    private final String commandName;
    private final List<String> commandArgs;

    private final Map<String, Command> commands = new HashMap<>();
    private final CollectionsKeeper ck;

    public CommandHandler(String commandName, List<String> commandArgs, CollectionsKeeper collectionsKeeper){
        ck = collectionsKeeper;
        this.commandArgs=commandArgs;
        this.commandName=commandName;
        Command c = new SimpleAdd(ck, null);
        commands.put("add",c);
        c = new Update(ck, null);
        commands.put("update",c);
        createCommandCollection();
    }

    public CommandHandler(String commandName, Person newPerson, CollectionsKeeper collectionsKeeper){
        ck = collectionsKeeper;
        this.commandArgs=null;
        this.commandName=commandName;
        Command c = new SimpleAdd(ck, newPerson);
        commands.put("add",c);
        c = new Update(ck, null);
        commands.put("update",c);
        createCommandCollection();
    }

    public CommandHandler(String commandName,List<String> commandArgs, Person newPerson, CollectionsKeeper collectionsKeeper){
        ck = collectionsKeeper;
        this.commandArgs=commandArgs;
        this.commandName=commandName;
        Command c = new Update(ck, newPerson);
        commands.put("update",c);
        c = new SimpleAdd(ck, null);
        commands.put("add",c);
        createCommandCollection();
    }

    private void createCommandCollection(){
        Command c = new AddIfMax(ck);
        commands.put("add_if_max",c);
        c = new AddIfMin(ck);
        commands.put("add_if_min",c);
        c = new Clear(ck);
        commands.put("clear",c);
        c = new CountLessPass(ck);
        commands.put("count_less_than_passport_id",c);
        c = new ExecuteScript(ck, this);
        commands.put("execute_script",c);
        c = new Head(ck);
        commands.put("head",c);
        c = new Help(ck, this);
        commands.put("help",c);
        c = new Info(ck);
        commands.put("info",c);
        c = new RemoveByID(ck);
        commands.put("remove_by_id",c);
        c = new RemoveByPass(ck);
        commands.put("remove_all_by_passport_id",c);
        c = new Show(ck);
        commands.put("show",c);
        c = new SumOfWeight(ck);
        commands.put("sum_of_weight",c);
    }

    public Map<String, Command> getCommands() {
        return commands;
    }

    private ServerResponse run(){
        Command c;
        switch (commandName){
            case "add_if_max":
                c = commands.get("add_if_max");
                return c.execute(commandArgs);
            case "add_if_min":
                c = commands.get("add_if_min");
                return c.execute(commandArgs);
            case "remove_all_by_passport_id":
                c = commands.get("remove_all_by_passport_id");
                return c.execute(commandArgs);
            case "clear":
                c = commands.get("clear");
                return c.execute(commandArgs);
            case "count_less_than_passport_id":
                c = commands.get("count_less_than_passport_id");
                return c.execute(commandArgs);
            case "execute_script":
                c = commands.get("execute_script");
                return c.execute(commandArgs);
            case "head":
                c = commands.get("head");
                return c.execute(commandArgs);
            case "help":
                c = commands.get("help");
                return c.execute(commandArgs);
            case "info":
                c = commands.get("info");
                return c.execute(commandArgs);
            case "remove_by_id":
                c = commands.get("remove_by_id");
                return c.execute(commandArgs);
            case "show":
                c = commands.get("show");
                return c.execute(commandArgs);
            case "sum_of_weight":
                c = commands.get("sum_of_weight");
                return c.execute(commandArgs);
            case "update":
                c = commands.get("update");
                return c.execute(commandArgs);
            case "add":
                c = commands.get("add");
                return c.execute(null);
            default:
                return ServerResponse.builder().error("Команды не существует.").build();
        }
    }

    public ServerResponse setRun(){
        return run();
    }

}