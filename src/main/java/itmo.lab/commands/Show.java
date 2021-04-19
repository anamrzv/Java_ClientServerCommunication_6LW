package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Команда выводит в консоль все элементы коллекции в строковом представлении
 */
public class Show extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Show(CollectionsKeeper dc) {
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
            if (people.size() == 0) System.out.println("Коллекция People пуста.");
            else {
                Collections.sort(people);
                System.out.println("Коллекция People:");
                for (Person p : people) {
                    System.out.println(p);
                }
            }
            return true;
        } else {
            System.out.println("У команды show нет аргументов. Введите команду снова.");
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
        return "show";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
