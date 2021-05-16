package itmo.lab.commands;

import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Person;
import itmo.lab.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * Команда обновляет значения полей объекта коллекции с заданным id
 */
public class Update extends Command {

    private final Person person;
    private final LinkedList<Person> people = dc.getPeople();

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Update(CollectionsKeeper dc, Person person) {
        super(dc);
        this.person = person;
    }

    /**
     * Главный метод класса, запускает команду
     *
     * @param args Параметры командной строки
     * @return true/false Успешно ли завершилась команда
     */
    @Override
    public ServerResponse execute(List<String> args) {
        int index = Integer.parseInt(args.get(0));
        people.set(index, person);
        return ServerResponse.builder().message("Объект успешно обновлен").command("update").build();
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "update";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "update id {element} : обновить значение элемента коллекции, id которого равен заданному";
    }
}
