package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Команда добавляет элемент в коллекцию либо через меню выбора, либо интерпретируя строку json
 */
public class SimpleAdd extends Command {

    private final Person person;

    private final LinkedList<Person> people = dc.getPeople();

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public SimpleAdd(CollectionsKeeper dc, String[] args) {
        super(dc);
        CreatePerson cp = new CreatePerson(dc);
        person = cp.setCreation(args);
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    public boolean execute(String... args) {
        people.add(person);
        Collections.sort(people);
        int i = people.indexOf(person);
        dc.setLastPersonNum(i);
        return true;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return null;
    }
}
