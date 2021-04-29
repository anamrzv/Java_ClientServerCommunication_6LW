package itmo.lab.commands;

import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.Person;
import itmo.lab.other.ServerResponse;

import java.util.LinkedList;
import java.util.List;

/**
 * Команда добавляет новый элемент в коллекцию, если его id превышает значение id наибольшего элемента.
 */
public class AddIfMax extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public AddIfMax(CollectionsKeeper dc) {
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
            return ServerResponse.builder().error("У команды add_if_max должен быть аргумент - слово 'Person' или строка формата json. Введите команду снова.").command("add_if_max").build();
        } else {
            if (people.size() == 1)
                return ServerResponse.builder().message("Объект добавлен в коллекцию, т.к. коллекция была пуста.").command("add_if_max").build();
            else {

                if (true) {
                    return ServerResponse.builder().error("Объект не добавлен в коллекцию, т.к. его id меньше наибольшего имеющегося.").command("add_if_max").build();
                } else
                    return ServerResponse.builder().message("Объект добавлен в коллекцию, т.к. его id больше прежнего максимального.").command("add_if_max").build();
            }
        }
    }


    /**
     * Возвращает имя команды 4832
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "add_if_max";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "add_if_max {element} : добавить новый элемент в коллекцию, если его id превышает значение id наибольшего элемента этой коллекции";
    }
}
