package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.server.CollectionsKeeper;

import java.util.LinkedList;

/**
 * Команда выводит сумму поля 'weight' всех объектов коллекции
 */
public class SumOfWeight extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public SumOfWeight(CollectionsKeeper dc) {
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
            LinkedList<Person> people = dc.getPeople();
            long sum = 0;
            for (Person p : people) {
                sum += p.getWeight();
            }
            System.out.println(sum + " - сумма значений поля weight всех элементов коллекции");
            return true;
        } else {
            System.out.println("У команды sum_of_weight нет аргументов. Введите команду еще раз.");
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
        return "sum_of_weight";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "sum_of_weight : вывести сумму значений поля weight для всех элементов коллекции";
    }
}
