package itmo.lab.commands;

import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Person;
import itmo.lab.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;

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
    public SimpleAdd(CollectionsKeeper dc, Person person) {
        super(dc);
        this.person = person;
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    public ServerResponse execute(List<String> args) {
        people.add(person);
        return ServerResponse.builder().message("Объект успешно добавлен").command("add").build();
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "add person: добавить новый элемент в коллекцию, ввод вручную\nadd json_element : добавить новый элемент в коллекцию, автоматическая обработка строки json";
    }
}
