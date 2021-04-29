package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;

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
    public ServerResponse execute(List<String> args) {
        if (args == null) {
            LinkedList<Person> people = dc.getPeople();
            people.clear();
            return ServerResponse.builder().message("Коллекция успешно очищена.").command("clear").build();
        } else {
            return ServerResponse.builder().error("У команды clear нет аргументов. Введите команду снова.").build();
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
