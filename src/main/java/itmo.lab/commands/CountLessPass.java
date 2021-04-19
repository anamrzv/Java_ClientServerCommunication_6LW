package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.LinkedList;

/**
 * Команда считает кол-во объектов со значением passport id меньше заданного
 */
public class CountLessPass extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public CountLessPass(CollectionsKeeper dc) {
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
                System.out.println("У команды count_less_than_passport_id должен быть ровно один аргумент - ID паспорта. Введите команду снова.");
                return false;
            }
            Long id;
            try {
                id = Long.parseLong(args[0]);
                if (id < 0) {
                    System.out.println("ID паспорта не может быть отрицательным числом. Введите команду снова.");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("В качестве аргумента должна быть передана строка из цифр.\n Если строка составлена правильно, то передано слишком большое число. Введите команду снова.");
                return false;
            }
            LinkedList<Person> people = dc.getPeople();
            int res = 0;
            for (Person p : people) {
                if (p.getPassportAsLong() < id) res++;
            }
            System.out.println(res + " - число элементов, значение поля passportID которых меньше " + id);
            return true;
        } else {
            System.out.println("У команды count_less_than_passport_id должен быть один аргумент - ID паспорта. Введите команду снова.");
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
        return "count_less_than_passport_id";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "count_less_than_passport_id passportID : вывести количество элементов, значение поля passportID которых меньше заданного";
    }
}
