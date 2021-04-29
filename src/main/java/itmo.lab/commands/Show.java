package itmo.lab.commands;

import itmo.lab.other.Person;
import itmo.lab.other.CollectionsKeeper;
import itmo.lab.other.ServerResponse;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Команда выводит в консоль все элементы коллекции в строковом представлении
 */
public class Show extends Command {

    /**
     * Конструктор - создание нового объекта
     *
     * @param dc - обработчик команд
     */
    public Show(CollectionsKeeper dc) {
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
        String response="Коллекция People:\n";
        if (args == null) {
            LinkedList<Person> people = dc.getPeople();
            if (people.size() == 0) return ServerResponse.builder().message("Коллекция People пуста.").command("show").build();
            else {
                Collections.sort(people);
                for (Person p : people) {
                    response+=p+"\n";
                }
            }
            return ServerResponse.builder().message(response).command("show").build();
        } else {
            return ServerResponse.builder().error("У команды show нет аргументов. Введите команду снова.").command("show").build();
        }
    }

    /**
     * Возвращает имя команды
     *
     * @return имя
     */
    @Override
    public String getName() {
        return "show";
    }

    /**
     * Возвращает описание команды
     *
     * @return описание
     */
    @Override
    public String getDescription() {
        return "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
