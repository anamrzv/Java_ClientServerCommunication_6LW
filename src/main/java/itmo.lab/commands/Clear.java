package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.LinkedList;

/**
 * Команда очищает коллекцию
 */
public class Clear extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Clear(CollectionsKeeper dc) {
        super(dc);
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    @Override
    public boolean execute(String... args) {
        if (args == null) {
            LinkedList<Person> people = dc.getPeople();
            people.clear();
            System.out.println("Коллекция успешно очищена.");
            return true;
        } else {
            System.out.println("У команды clear нет аргументов. Введите команду снова.");
            return false;
        }
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "clear";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "clear : очистить коллекцию";
    }
}
