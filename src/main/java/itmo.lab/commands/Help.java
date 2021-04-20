package itmo.lab.commands;

import itmo.lab.server.CollectionsKeeper;
import itmo.lab.server.CommandHandler;

import java.util.Map;

/**
 * Команда выводит описание доступных команд в консоль
 */
public class Help extends Command {

    private final CommandHandler ch;

    /**
     * Конструктор - создание нового объекта
     *
     * @param ch - обработчик команд
     */
    public Help(CollectionsKeeper dc, CommandHandler ch) {
        super(dc);
        this.ch = ch;
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    public boolean execute(String... args) {
        if (args == null) {
            Map<String, Command> commands = ch.getCommands();
            System.out.println("Доступные вам команды:");
            for (Command c : commands.values()) {
                System.out.println(c.getDescription());
            }
            System.out.println("add {element} : добавить новый элемент в коллекцию");
            return true;
        } else {
            System.out.println("У команды help нет аргументов. Введите команду снова.");
            return false;
        }
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    public String getName() {
        return "help";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    public String getDescription() {
        return "help : вывести справку по доступным командам";
    }
}
