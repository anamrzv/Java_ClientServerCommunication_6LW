package itmo.lab.commands;

import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.CreatePerson;
import itmo.lab.other.Person;
import itmo.lab.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * Команда добавляет новый элемент в коллекцию, если его id меньше значения id наименьшего элемента.
 */
public class AddIfMin extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public AddIfMin(CollectionsKeeper dc) {
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
        LinkedList<Person> people = dc.getPeople();
        if (args == null || args.size() != 1) {
            return ServerResponse.builder().error("У команды add_if_min должен быть аргумент - слово 'Person' или строка формата json. Введите команду снова.").command("add_if_min").build();
        } else {
            if (people.size() == 0) {
                return ServerResponse.builder().message("Объект добавлен в коллекцию, т.к. коллекция была пуста.").command("add_if_min").build();
            } else {
                Person maybePerson;
                CreatePerson create = new CreatePerson(dc, this);
                maybePerson = create.setCreation(args);
                if (maybePerson.getName().compareTo(people.getLast().getName()) < 1) {
                    return ServerResponse.builder().error("Объект не добавлен в коллекцию, т.к. его имя выше по алфавиту, чем последнее").command("add_if_min").build();
                } else {
                    people.add(maybePerson);
                    return ServerResponse.builder().message("Объект добавлен в коллекцию, т.к. его имя ниже по алфавиту, чем последнее, или совпадает с ним").command("add_if_min").build();
                }
            }
        }
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "add_if_min";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "add_if_min {element} :добавить новый элемент в коллекцию, если его id меньше, чем у наименьшего id  этой коллекции";
    }
}
