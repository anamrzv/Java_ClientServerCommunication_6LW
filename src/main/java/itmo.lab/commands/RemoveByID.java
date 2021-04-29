package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.ServerResponse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


/**
 * Команда удаляет объект с данным значением id
 */
public class RemoveByID extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public RemoveByID(CollectionsKeeper dc) {
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
        if (args != null) {
            if (args.size() != 1) {
                return ServerResponse.builder().error("У команды remove_by_id должен быть ровно один аргумент - ID персоны. Введите команду снова.").command("remove_by_id").build();
            }
            Long id;
            boolean result = false;
            try {
                id = Long.parseLong(args.get(0));
                if (id < 0) return ServerResponse.builder().error("ID не может быть отрицательным числом. Введите команду снова.").command("count_less_than_passport_id").build();
            } catch (Exception e) {
                return ServerResponse.builder().error("В качестве аргумента должна быть передана строка из цифр. Введите команду снова.").command("count_less_than_passport_id").build();
            }
            LinkedList<Person> people = dc.getPeople();
            Iterator<Person> iter = people.iterator();
            while (iter.hasNext()) {
                if (iter.next().getID().equals(id)) {
                    iter.remove();
                    result = true;
                }
            }
            if (!result) return ServerResponse.builder().message("Элемента с таким ID нет в коллекции.").command("count_less_than_passport_id").build();
            else return ServerResponse.builder().message("Объект с ID " + id + " успешно удален из коллекции.").command("count_less_than_passport_id").build();
        } else {
            return ServerResponse.builder().error("У команды remove_by_id должен быть один аргумент - ID персоны. Введите команду снова.").command("count_less_than_passport_id").build();
        }
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "remove_by_id";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "remove_by_id id : удалить элемент из коллекции по его id";
    }
}
