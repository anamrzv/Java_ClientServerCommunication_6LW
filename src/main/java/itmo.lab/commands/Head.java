package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.LinkedList;

/**
 * Команда выводит первый элемент коллекции
 */
public class Head extends Command {

    /**
     * Поле - связный список объектов Person
     */
    LinkedList<Person> people;

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Head(CollectionsKeeper dc) {
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
            people = dc.getPeople();
            if (people.size() == 0) System.out.println("Коллекция пуста, нельзя вывести первый элемент");
            else {
                System.out.println("Первый элемент отсортированной коллекции: ");
                System.out.println(people.get(0));
            }
            return true;
        } else {
            System.out.println("У команы head нет аргументов. Введите команду снова.");
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
        return "head";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "head : вывести первый элемент коллекции";
    }
}
