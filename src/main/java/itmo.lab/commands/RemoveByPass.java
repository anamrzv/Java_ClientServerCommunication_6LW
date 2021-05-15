package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.ServerResponse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Команда удаляет из коллекции все объекты с passport id меньше заданного
 */
public class RemoveByPass extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public RemoveByPass(CollectionsKeeper dc) {
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
                return ServerResponse.builder().error("У команды remove_all_by_passport_id должен быть ровно один аргумент - ID паспорта. Введите команду снова.").command("remove_by_passport_id").build();
            }
            Long id;
            boolean result = false;
            try {
                id = Long.parseLong(args.get(0));
                if (id < 0) return ServerResponse.builder().error("ID не может быть отрицательным числом. Введите команду снова").command("remove_by_passport_id").build();
            } catch (Exception e) {
                return ServerResponse.builder().error("В качестве аргумента должна быть передана строка из цифр. Введите команду снова.").command("remove_by_passport_id").build();
            }
            LinkedList<Person> people = dc.getPeople();
            int count=0;
            do{
                Person person = people.stream()
                        .filter(x -> x.getPassportAsLong().equals(id))
                        .findFirst()
                        .orElse(null);
                if (person!=null) {
                    people.remove(person);
                    count++;
                }
                else break;
            }while (true);
            if (count!=0) return ServerResponse.builder().message("Объекты с PassportID " + id + " успешно удалены из коллекции.").command("remove_by_passport_id").build();
            return ServerResponse.builder().message("Элементов с таким PassportID нет в коллекции.").command("remove_by_passport_id").build();
        } else {
            return ServerResponse.builder().error("У команды remove_all_by_passport_id должен быть один аргумент - ID паспорта. Введите команду снова.").command("remove_by_passport_id").build();
        }
    }


    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "remove_all_by_passport_id";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "remove_all_by_passport_id passportID : удалить из коллекции все элементы, значение поля passportID которого эквивалентно заданному";
    }
}
