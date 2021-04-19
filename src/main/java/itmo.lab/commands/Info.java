package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Команда выводит информацию о коллекции в консоль
 */
public class Info extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Info(CollectionsKeeper dc) {
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
            Collections.sort(people);
            System.out.println("Тип коллекции: " + people.getClass());
            System.out.println("Тип элементов: Person");
            System.out.println("Количество элементов: " + people.size());
            if (people.size() != 0) {
                System.out.println("Дата инициализации: " + people.get(0).getParsedTime());
            } else {
                System.out.println("Дата инициализации: -");
            }
            return true;
        } else {
            System.out.println("У команды info нет аргументов. Введите команду снова.");
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
        return "info";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }
}
