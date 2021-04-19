package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.Iterator;
import java.util.LinkedList;


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
    public boolean execute(String... args) {
        if (args != null) {
            if (args.length != 1) {
                System.out.println("У команды remove_by_id должен быть ровно один аргумент - ID персоны. Введите команду снова.");
                return false;
            }
            Long id;
            boolean result = false;
            try {
                id = Long.parseLong(args[0]);
                if (id < 0) return false;
            } catch (Exception e) {
                System.out.println("В качестве аргумента должна быть передана строка из цифр. Введите команду снова.");
                return false;
            }
            LinkedList<Person> people = dc.getPeople();
            Iterator<Person> iter = people.iterator();
            while (iter.hasNext()) {
                if (iter.next().getID().equals(id)) {
                    iter.remove();
                    result = true;
                }
            }
            if (!result) System.out.println("Элемента с таким ID нет в коллекции.");
            else System.out.println("Объект с ID " + id + " успешно удален из коллекции.");
            return true;
        } else {
            System.out.println("У команды remove_by_id должен быть один аргумент - ID персоны. Введите команду снова.");
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
